package org.hzero.platform.app.service.impl;


import java.util.List;
import java.util.Map;

import org.hzero.platform.app.service.LovService;
import org.hzero.platform.domain.entity.Lov;
import org.hzero.platform.domain.repository.LovRepository;
import org.hzero.platform.domain.repository.LovValueRepository;
import org.hzero.platform.domain.service.LovDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 值集服务类
 *
 * @author gaokuo.dai@hand-china.com 2018年6月12日下午2:38:19
 */
@Service
public class LovServiceImpl implements LovService {

    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private LovValueRepository lovValueRepository;
    @Autowired
    private LovDomainService lovDomainService;
    
    @Override
    public Lov queryLovInfo(String lovCode, Long tenantId, boolean onlyPublic) {
        return lovDomainService.queryLovInfo(lovCode, tenantId, onlyPublic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteLovHeaderByPrimaryKey(Lov lovHeader) {
        return lovHeader.cascadeDelete(this.lovRepository, this.lovValueRepository);
    }
    
    @Override
    public String queryLovSql(String lovCode, Long tenantId, boolean onlyPublic) {
        return lovDomainService.queryLovSql(lovCode, tenantId, onlyPublic);
    }

    @Override
    public String queryLovTranslationSql(String lovCode, Long tenantId, boolean onlyPublic) {
        return lovDomainService.queryLovTranslationSql(lovCode, tenantId, onlyPublic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Lov addLov(Lov lov) {
        return lovDomainService.addLov(lov);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Lov updateLov(Lov lov) {
        return lovDomainService.updateLov(lov);
    }

    @Override
    public List<Map<String, Object>> queryLovData(String lovCode, Long tenantId, String tag, Integer page, Integer size, Map<String, String> params, boolean onlyPublic) {
        return lovDomainService.queryLovData(lovCode, tenantId, tag, page, size, params, onlyPublic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyLov(Long tenantId, String lovCode, Long lovId, Integer siteFlag) {
        lovDomainService.copyLov(tenantId, lovCode, lovId, siteFlag);
    }

}
