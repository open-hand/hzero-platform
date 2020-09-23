package org.hzero.platform.app.service.impl;

import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.platform.api.dto.DatabaseTenantDTO;
import org.hzero.platform.app.service.DatabaseService;
import org.hzero.platform.domain.entity.Database;
import org.hzero.platform.domain.entity.DatabaseTenant;
import org.hzero.platform.domain.entity.Datasource;
import org.hzero.platform.domain.repository.DatabaseRepository;
import org.hzero.platform.domain.repository.DatabaseTenantRepository;
import org.hzero.platform.domain.repository.DatasourceRepository;
import org.hzero.platform.domain.repository.DatasourceServiceRepository;
import org.hzero.platform.infra.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数据库应用服务默认实现
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    private DatabaseRepository databaseRepository;

    @Autowired
    private DatabaseTenantRepository databaseTenantRepository;

    @Autowired
    private DatasourceRepository datasourceRepository;

    @Autowired
    private DatasourceServiceRepository datasourceServiceRepository;

    @Autowired
    private RedisHelper redisHelper;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Database createDatabase(Database database) {

        database.validate(databaseRepository, datasourceRepository);
        databaseRepository.insertSelective(database);
        if (Objects.equals(database.getLevelFlag(), Constants.TENANT_LEVEL_UPPER_CASE)) {
            // 是op 层级，自动将当前租户分配到该数据库数据中
            Long tenantId = DetailsHelper.getUserDetails().getTenantId();
            DatabaseTenant databaseTenant = new DatabaseTenant(database.getDatabaseId(), tenantId,
                    database.getDatasourceId());
            databaseTenantRepository.insertSelective(databaseTenant);
        }
        database.refreshCache(databaseTenantRepository, redisHelper, datasourceServiceRepository);
        return database;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Database updateDatabase(Database database) {

        Assert.notNull(database.getDatabaseId(), BaseConstants.ErrorCode.DATA_INVALID);
        Database entity = databaseRepository.selectByPrimaryKey(database);
        Assert.notNull(entity, BaseConstants.ErrorCode.DATA_NOT_EXISTS);

        Long datasourceId = database.getDatasourceId() != null ? database.getDatasourceId() : entity.getDatasourceId();

        if (CollectionUtils.isEmpty(datasourceRepository.select(Datasource.FIELD_DATASOURCE_ID, datasourceId))) {
            throw new CommonException(BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        }

        entity.equalsDatabaseCode(database);
        databaseRepository.updateOptional(database, Database.FIELD_DATABASE_NAME, Database.FIELD_DATASOURCE_ID,
                Database.FIELD_ENABLED_FLAG, Database.FIELD_PUBLIC_FLAG, Database.FIELD_TABLE_PREFIX);
        database.clearCacheAllByDatabaseId(redisHelper, databaseTenantRepository, datasourceServiceRepository);
        database.refreshCache(databaseTenantRepository, redisHelper, datasourceServiceRepository);

        return database;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByDatabaseId(Long databaseId) {
        Database database = databaseRepository.selectByPrimaryKey(databaseId);
        Assert.notNull(database, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        // 获取databaseId 对应的tenantId列表
        List<DatabaseTenantDTO> list = databaseTenantRepository.selectByDatabaseId(databaseId);
        List<Long> tenantIds = list.stream().map(DatabaseTenantDTO::getTenantId).collect(Collectors.toList());
        databaseRepository.deleteByPrimaryKey(databaseId);
        databaseTenantRepository.removeByDatabaseId(databaseId);
        database.clearCacheByTenantIds(redisHelper, tenantIds, database.getDatasourceId(), datasourceServiceRepository);
    }

    @Override
    public void initAllData() {
        try {
            SecurityTokenHelper.close();
            List<DatabaseTenant> databaseTenants = databaseTenantRepository.selectByCondition(Condition.builder(DatabaseTenant.class).build());
            if (CollectionUtils.isNotEmpty(databaseTenants)) {
                for (DatabaseTenant databaseTenant : databaseTenants) {
                    Long tenantId = databaseTenant.getTenantId();
                    Long datasourceId = databaseTenant.getDatasourceId();
                    Database database = new Database();
                    database.setDatabaseId(databaseTenant.getDatabaseId());
                    SecurityTokenHelper.close();
                    database = databaseRepository.selectByPrimaryKey(database);
                    if (database == null) {
                        logger.warn("Primary key {} is not exist in Table database", databaseTenant.getDatasourceId());
                        continue;
                    }
                    if (BaseConstants.Flag.YES.equals(database.getEnabledFlag())) {
                        List<String> keys = Database.generateKey(tenantId, datasourceServiceRepository, datasourceId);
                        Database finalDatabase = database;
                        if (CollectionUtils.isNotEmpty(keys)) {
                            keys.forEach(key -> redisHelper.strSet(key, finalDatabase.getDatabaseCode()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Database cache init not successfully completed", e);
        }
        SecurityTokenHelper.clear();
    }


}
