package org.hzero.platform.infra.repository.impl;

import java.util.List;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.platform.api.dto.DatabaseTenantDTO;
import org.hzero.platform.domain.entity.DatabaseTenant;
import org.hzero.platform.domain.repository.DatabaseTenantRepository;
import org.hzero.platform.infra.mapper.DatabaseTenantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

/**
 * 数据权限数据源关系 资源库实现
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
@Component
public class DatabaseTenantRepositoryImpl extends BaseRepositoryImpl<DatabaseTenant>
                implements DatabaseTenantRepository {

    @Autowired
    private DatabaseTenantMapper databaseTenantMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByDatabaseId(Long databaseId) {
        databaseTenantMapper.removeByDatabaseId(databaseId);
    }

    @Override
    public Page<DatabaseTenantDTO> pageDatabaseTenant(DatabaseTenantDTO databaseTenantDTO, PageRequest pageRequest) {
        return PageHelper.doPageAndSort(pageRequest,
                        () -> databaseTenantMapper.selectDatabaseTenant(databaseTenantDTO));
    }

    @Override
    public List<DatabaseTenantDTO> selectByDatabaseId(Long databaseId) {
        DatabaseTenantDTO databaseTenantDTO = new DatabaseTenantDTO();
        databaseTenantDTO.setDatabaseId(databaseId);
        return databaseTenantMapper.selectDatabaseTenant(databaseTenantDTO);
    }

    @Override
    public List<Long> selectTenantIdsByDatabaseId(Long databaseId) {
        return databaseTenantMapper.selectTenantIdsByDatabaseId(databaseId);
    }
}
