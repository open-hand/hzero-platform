package org.hzero.platform.infra.mapper;

import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.hzero.platform.api.dto.DatabaseTenantDTO;
import org.hzero.platform.domain.entity.DatabaseTenant;

import java.util.List;

/**
 * 数据权限数据源关系Mapper
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
public interface DatabaseTenantMapper extends BaseMapper<DatabaseTenant> {

    /**
     * 根据数据源id删除 租户数据源关系
     *
     * @param databaseId 数据源id
     */
    void removeByDatabaseId(@Param("databaseId") Long databaseId);

    /**
     * 查询数据权限数据源关系
     *
     * @param databaseTenantDTO databaseTenantDTO
     * @return List<DatabaseTenantDTO>
     */
    List<DatabaseTenantDTO> selectDatabaseTenant(DatabaseTenantDTO databaseTenantDTO);

    /**
     * 根据数据库id查询租户id列表
     * @param databaseId    数据库id
     * @return 租户id列表
     */
    List<Long> selectTenantIdsByDatabaseId(Long databaseId);
}
