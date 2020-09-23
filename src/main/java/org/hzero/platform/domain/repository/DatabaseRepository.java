package org.hzero.platform.domain.repository;

import java.util.List;

import org.hzero.mybatis.base.BaseRepository;
import org.hzero.platform.api.dto.DatabaseDTO;
import org.hzero.platform.domain.entity.Database;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 数据库资源库
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
public interface DatabaseRepository extends BaseRepository<Database> {

    /**
     * 分页查询
     *
     * @param databaseDTO 查询参数
     * @param pageRequest 分页参数
     * @return 分页结果
     */
    Page<DatabaseDTO> pageDatabase(DatabaseDTO databaseDTO, PageRequest pageRequest);

    /**
     * 根据datasourceId查询数据库
     *
     * @param datasourceId 数据源id
     * @return 数据库列表
     */
    List<Database> selectDatabaseByDatasourceId(Long datasourceId);
}
