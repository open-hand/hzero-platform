package org.hzero.platform.app.service.impl;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.platform.app.service.DatabaseTenantService;
import org.hzero.platform.domain.entity.Database;
import org.hzero.platform.domain.entity.DatabaseTenant;
import org.hzero.platform.domain.repository.DatabaseRepository;
import org.hzero.platform.domain.repository.DatabaseTenantRepository;
import org.hzero.platform.domain.repository.DatasourceServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import io.choerodon.core.exception.CommonException;

/**
 * 数据权限数据源关系应用服务默认实现
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
@Service
public class DatabaseTenantServiceImpl implements DatabaseTenantService {

    @Autowired
    private DatabaseTenantRepository databaseTenantRepository;

    @Autowired
    private DatabaseRepository databaseRepository;

    @Autowired
    private DatasourceServiceRepository datasourceServiceRepository;

    @Autowired
    private RedisHelper redisHelper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DatabaseTenant createDatabaseTenant(DatabaseTenant databaseTenant) {

        databaseTenant.validate(databaseTenantRepository);

        Database database = databaseRepository.selectByPrimaryKey(databaseTenant.getDatabaseId());
        Assert.notNull(database, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        databaseTenant.setDatasourceId(database.getDatasourceId());

        databaseTenantRepository.insertSelective(databaseTenant);
        database.refreshCache(databaseTenantRepository, redisHelper, datasourceServiceRepository);

        return databaseTenant;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DatabaseTenant updateDatabaseTenant(DatabaseTenant databaseTenant) {
        Assert.notNull(databaseTenant.getDatabaseTenantId(), BaseConstants.ErrorCode.DATA_INVALID);
        DatabaseTenant entity = databaseTenantRepository.selectByPrimaryKey(databaseTenant);
        Assert.notNull(entity, BaseConstants.ErrorCode.DATA_NOT_EXISTS);

        entity.equalsDatabaseIdAndTenantId(databaseTenant);
        //如果修改数据库，则验证传入databaseId，否则验证原先的databaseId
        Database database = new Database();
        database.setDatabaseId(ObjectUtils.defaultIfNull(databaseTenant.getDatabaseId(), entity.getDatabaseId()));

        if(databaseRepository.selectCount(database) == 0){
            throw new CommonException(BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        }

        databaseTenant.validate(databaseTenantRepository);

        databaseTenantRepository.updateOptional(databaseTenant, DatabaseTenant.FIELD_DATABASE_ID,
                DatabaseTenant.FIELD_DATA_SOURCE_ID,
                DatabaseTenant.FIELD_TENANT_ID);
        database.clearCacheAllByDatabaseId(redisHelper, databaseTenantRepository, datasourceServiceRepository);
        database.refreshCache(databaseTenantRepository, redisHelper ,datasourceServiceRepository);

        return databaseTenant;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeDatabaseTenantById(Long databaseTenantId) {
        DatabaseTenant databaseTenant = databaseTenantRepository.selectByPrimaryKey(databaseTenantId);
        Assert.notNull(databaseTenant, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        Long tenantId = databaseTenant.getTenantId();
        databaseTenantRepository.deleteByPrimaryKey(databaseTenantId);
        List<String> sourceNames = datasourceServiceRepository.selectServiceNamesByDatasourceId(databaseTenant.getDatasourceId());
        databaseTenant.clearCache(redisHelper, tenantId, sourceNames);
    }
}
