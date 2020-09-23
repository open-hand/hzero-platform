package org.hzero.platform.app.service;

import org.hzero.platform.domain.entity.DatabaseTenant;

/**
 * 数据权限数据源关系应用服务
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
public interface DatabaseTenantService {

    /**
     * 创建数据权限数据源关系
     *
     * @param databaseTenant 数据权限数据源关系
     * @return
     */
    DatabaseTenant createDatabaseTenant(DatabaseTenant databaseTenant);

    /**
     * 更新数据权限数据源关系
     * @param databaseTenant 数据权限数据源关系
     * @return
     */
    DatabaseTenant updateDatabaseTenant(DatabaseTenant databaseTenant);

    /**
     * 删除数据权限数据源关系
     * @param databaseTenantId 数据权限数据源关系id
     */
    void removeDatabaseTenantById(Long databaseTenantId);
}
