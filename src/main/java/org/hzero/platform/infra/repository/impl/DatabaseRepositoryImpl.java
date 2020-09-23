package org.hzero.platform.infra.repository.impl;

import java.util.List;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.platform.api.dto.DatabaseDTO;
import org.hzero.platform.domain.entity.Database;
import org.hzero.platform.domain.repository.DatabaseRepository;
import org.hzero.platform.infra.mapper.DatabaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 数据库 资源库实现
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
@Component
public class DatabaseRepositoryImpl extends BaseRepositoryImpl<Database> implements DatabaseRepository {

    @Autowired
    private DatabaseMapper databaseMapper;

    @Override
    public Page<DatabaseDTO> pageDatabase(DatabaseDTO databaseDTO, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest, () -> databaseMapper.selectDatabase(databaseDTO));
    }

    @Override
    public List<Database> selectDatabaseByDatasourceId(Long datasourceId) {
        return databaseMapper.selectByDatasourceId(datasourceId);
    }

}
