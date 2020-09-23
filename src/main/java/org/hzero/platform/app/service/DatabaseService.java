package org.hzero.platform.app.service;

import org.hzero.platform.domain.entity.Database;

/**
 * 数据库应用服务
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
public interface DatabaseService {
    /**
     * 创建数据库
     *
     * @param database 数据库
     * @return
     */
    Database createDatabase(Database database);

    /**
     * 更新数据库
     *
     * @param database 数据库
     * @return
     */
    Database updateDatabase(Database database);

    /**
     * 删除数据库
     *
     * @param databaseId 数据库id not null
     */
    void removeByDatabaseId(Long databaseId);

    /**
     * 应用启动刷新redis缓存
     */
    void initAllData();
}
