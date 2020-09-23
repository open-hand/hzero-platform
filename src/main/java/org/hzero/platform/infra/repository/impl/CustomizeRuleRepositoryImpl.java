package org.hzero.platform.infra.repository.impl;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.platform.domain.entity.CustomizeRule;
import org.hzero.platform.domain.repository.CustomizeRuleRepository;
import org.hzero.platform.infra.mapper.CustomizeRuleMapper;
import org.hzero.boot.api.customize.commons.repository.ApiCustomizeRepository;
import org.hzero.boot.api.customize.commons.vo.ApiCustomizeRule;

/**
 * API个性化规则 资源库实现
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
@Component
public class CustomizeRuleRepositoryImpl extends BaseRepositoryImpl<CustomizeRule> implements CustomizeRuleRepository {

    private final CustomizeRuleMapper customizeRuleMapper;
    private final ApiCustomizeRepository customizeRepository;

    public CustomizeRuleRepositoryImpl(CustomizeRuleMapper customizeRuleMapper,
                                       ApiCustomizeRepository customizeRepository) {
        this.customizeRuleMapper = customizeRuleMapper;
        this.customizeRepository = customizeRepository;
    }


    @Override
    @ProcessLovValue
    public Page<CustomizeRule> pageRule(PageRequest pageRequest, CustomizeRule customizeRule) {
        return PageHelper.doPage(pageRequest, () -> customizeRuleMapper.selectRule(customizeRule));
    }

    @Override
    public CustomizeRule selectRuleDetail(Long ruleId) {
        return customizeRuleMapper.selectRuleDetail(ruleId);
    }

    @Override
    public void cacheRule(CustomizeRule customizeRule) {
        customizeRepository.saveRule(new ApiCustomizeRule(
                customizeRule.getRuleCode(),
                customizeRule.getRuleName(),
                customizeRule.getTenantId(),
                customizeRule.getTypeCode(),
                customizeRule.getTypeValue(),
                customizeRule.getRulePosition(),
                customizeRule.getSyncFlag(),
                customizeRule.getEnabledFlag())
        );
    }

    @Override
    public void removeRule(CustomizeRule customizeRule) {
        customizeRule = selectByPrimaryKey(customizeRule.getRuleId());
        Assert.notNull(customizeRule, "customize rule not found.");
        customizeRuleMapper.deleteByPrimaryKey(customizeRule.getRuleId());
        customizeRepository.deleteRule(customizeRule.getRuleCode());
    }

}
