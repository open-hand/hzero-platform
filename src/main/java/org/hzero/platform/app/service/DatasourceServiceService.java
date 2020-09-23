package org.hzero.platform.app.service;

import org.hzero.platform.domain.entity.DatasourceService;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 服务数据源关系应用服务
 *
 * @author like.zhang@hand-china.com 2018-09-13 14:10:12
 */
public interface DatasourceServiceService {

    /**
     * 分页查询服务数据源关系
     * @param pageRequest 分页参数
     * @param datasourceId 数据源id
     * @param serviceName 服务名称
     * @return
     */
    Page<DatasourceService> pageDatasourceService(PageRequest pageRequest, Long datasourceId, String serviceName);

    /**
     * 创建服务数据源关系
     * @param datasourceService 服务数据源关系
     * @return
     */
    DatasourceService createDatasourceService(DatasourceService datasourceService);

    /**
     * 更新服务数据源关系
     *
     * @param datasourceService 服务数据源关系
     * @return
     */
    DatasourceService updateDatasourceService(DatasourceService datasourceService);

    /**
     * 根据主键id删除
     *
     * @param datasourceServiceId 服务数据源关系id
     */
    void deleteByDatasourceServiceId(Long datasourceServiceId);
}
