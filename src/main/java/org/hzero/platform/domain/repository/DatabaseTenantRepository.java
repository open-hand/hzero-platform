package org.hzero.platform.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.hzero.platform.api.dto.DatabaseTenantDTO;
import org.hzero.platform.domain.entity.DatabaseTenant;

import java.util.List;

/**
 * 数据权限数据源关系资源库
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
public interface DatabaseTenantRepository extends BaseRepository<DatabaseTenant> {

    /**
     * 根据数据库id删除 租户数据库关系
     *
     * @param databaseId 数据源id
     */
    void removeByDatabaseId(Long databaseId);

    /**
     * 分页查询
     *
     * @param databaseTenantDTO 查询参数
     * @param pageRequest 分页参数
     * @return 分页查询结果
     */
    Page<DatabaseTenantDTO> pageDatabaseTenant(DatabaseTenantDTO databaseTenantDTO, PageRequest pageRequest);

    /**
     * 根据数据库id查询所有关系
     *
     * @param databaseId 数据库id
     * @return 该数据库id关联的关系
     */
    List<DatabaseTenantDTO> selectByDatabaseId(Long databaseId);

    /**
     * 根据数据库id查询租户id列表
     * @param databaseId 数据库id
     * @return 租户id列表
     */
    List<Long> selectTenantIdsByDatabaseId(Long databaseId);
}
