package org.hzero.platform.infra.repository.impl;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.platform.domain.entity.DatasourceService;
import org.hzero.platform.domain.repository.DatasourceServiceRepository;
import org.hzero.platform.infra.mapper.DatasourceServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务数据源关系 资源库实现
 *
 * @author like.zhang@hand-china.com 2018-09-13 14:10:12
 */
@Component
public class DatasourceServiceRepositoryImpl extends BaseRepositoryImpl<DatasourceService> implements DatasourceServiceRepository {

    @Autowired
    private DatasourceServiceMapper datasourceServiceMapper;

    @Override
    public List<String> selectServiceNamesByDatasourceId(Long datasourceId) {
        return datasourceServiceMapper.selectServiceNamesByDatasourceId(datasourceId);
    }
}
