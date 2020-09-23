package org.hzero.platform.domain.repository;

import org.hzero.mybatis.base.BaseRepository;
import org.hzero.platform.domain.entity.DatasourceService;

import java.util.List;

/**
 * 服务数据源关系资源库
 *
 * @author like.zhang@hand-china.com 2018-09-13 14:10:12
 */
public interface DatasourceServiceRepository extends BaseRepository<DatasourceService> {
    /**
     * 根据数据源id查询所有关联的服务名称
     * @param datasourceId 数据源id
     * @return serviceNames
     */
    List<String> selectServiceNamesByDatasourceId(Long datasourceId);
}
