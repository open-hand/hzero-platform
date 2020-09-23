package org.hzero.platform.app.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.platform.app.service.DatasourceServiceService;
import org.hzero.platform.domain.entity.Database;
import org.hzero.platform.domain.entity.Datasource;
import org.hzero.platform.domain.entity.DatasourceService;
import org.hzero.platform.domain.repository.DatabaseRepository;
import org.hzero.platform.domain.repository.DatabaseTenantRepository;
import org.hzero.platform.domain.repository.DatasourceRepository;
import org.hzero.platform.domain.repository.DatasourceServiceRepository;
import org.hzero.platform.infra.mapper.DatasourceServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 服务数据源关系应用服务默认实现
 *
 * @author like.zhang@hand-china.com 2018-09-13 14:10:12
 */
@Service
public class DatasourceServiceServiceImpl implements DatasourceServiceService {

    @Autowired
    private DatasourceServiceRepository datasourceServiceRepository;

    @Autowired
    private DatasourceRepository datasourceRepository;

    @Autowired
    private DatasourceServiceMapper datasourceServiceMapper;

    @Autowired
    private DatabaseRepository databaseRepository;

    @Autowired
    private DatabaseTenantRepository databaseTenantRepository;

    @Autowired
    private RedisHelper redisHelper;


    @Override
    public Page<DatasourceService> pageDatasourceService(PageRequest pageRequest, Long datasourceId,
                    String serviceName) {
        return PageHelper.doPage(pageRequest.getPage(), pageRequest.getSize(),
                        () -> datasourceServiceMapper.selectDatasourceService(datasourceId, serviceName));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DatasourceService createDatasourceService(DatasourceService datasourceService) {
        datasourceService.validate(datasourceServiceRepository);
        Datasource datasource = datasourceRepository.selectByPrimaryKey(datasourceService.getDatasourceId());
        Assert.notNull(datasource, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        datasourceService.setTenantId(datasource.getTenantId());
        datasourceServiceRepository.insertSelective(datasourceService);
        datasource.refreshCache(datasourceServiceRepository, redisHelper);

        // 更新database缓存
        List<Database> databases = databaseRepository.selectDatabaseByDatasourceId(datasource.getDatasourceId());
        if (CollectionUtils.isNotEmpty(databases)) {
            databases.forEach(database -> database.refreshCache(databaseTenantRepository, redisHelper,
                            datasourceServiceRepository));
        }

        return datasourceService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DatasourceService updateDatasourceService(DatasourceService datasourceService) {

        Assert.notNull(datasourceService.getDatasourceId(), BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        Datasource datasource = datasourceRepository.selectByPrimaryKey(datasourceService.getDatasourceId());
        Assert.notNull(datasource, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        datasourceServiceRepository.updateOptional(datasourceService, DatasourceService.FIELD_SERVICE_NAME);

        datasource.refreshCache(datasourceServiceRepository, redisHelper);
        return datasourceService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDatasourceServiceId(Long datasourceServiceId) {
        DatasourceService datasourceService = datasourceServiceRepository.selectByPrimaryKey(datasourceServiceId);
        datasourceServiceRepository.deleteByPrimaryKey(datasourceServiceId);
        Datasource datasource = datasourceRepository.selectByPrimaryKey(datasourceService.getDatasourceId());

        // 删除数据源缓存
        redisHelper.delKey(Datasource.generateKey(datasourceService.getServiceName(), datasource.getDsPurposeCode(), datasource.getTenantId(), datasource.getDatasourceCode()));

        // 删除database缓存
        List<Database> databases = databaseRepository.selectDatabaseByDatasourceId(datasourceService.getDatasourceId());
        if (CollectionUtils.isNotEmpty(databases)) {
            databases.forEach(database -> {
                database.clearCacheAllByDatabaseId(redisHelper, databaseTenantRepository, datasourceServiceRepository);
                database.refreshCache(databaseTenantRepository, redisHelper, datasourceServiceRepository);
            });
        }

    }
}
