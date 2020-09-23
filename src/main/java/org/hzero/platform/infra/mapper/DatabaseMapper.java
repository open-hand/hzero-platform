package org.hzero.platform.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.hzero.platform.api.dto.DatabaseDTO;
import org.hzero.platform.domain.entity.Database;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * 数据库Mapper
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
public interface DatabaseMapper extends BaseMapper<Database> {
    /**
     * 查询数据库列表
     *
     * @param database DatabaseDTO
     * @return List<DatabaseDTO>
     */
    List<DatabaseDTO> selectDatabase(DatabaseDTO database);

    /**
     * 根据数据源id查询数据库列表
     * @param datasourceId 数据源id
     * @return 数据库列表
     */
    List<Database> selectByDatasourceId(@Param("datasourceId") Long datasourceId);
}
