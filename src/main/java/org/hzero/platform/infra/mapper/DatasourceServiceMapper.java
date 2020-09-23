package org.hzero.platform.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.hzero.platform.domain.entity.DatasourceService;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 服务数据源关系Mapper
 *
 * @author like.zhang@hand-china.com 2018-09-13 14:10:12
 */
public interface DatasourceServiceMapper extends BaseMapper<DatasourceService> {

    /**
     * 查询数据源服务关系
     *
     * @param datasourceId 数据源id
     * @param serviceName 服务名
     * @return 数据源关系列表
     */
    List<DatasourceService> selectDatasourceService(@Param("datasourceId") Long datasourceId, @Param("serviceName") String serviceName);

    /**
     * 查询datasourceId下的服务名列表
     * @param datasourceId 数据源id
     * @return 服务名列表
     */
    List<String> selectServiceNamesByDatasourceId(@Param("datasourceId") Long datasourceId);
}
