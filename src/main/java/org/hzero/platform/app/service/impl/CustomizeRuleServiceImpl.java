package org.hzero.platform.app.service.impl;

import org.springframework.stereotype.Service;

import org.hzero.platform.app.service.CustomizeRuleService;
import org.hzero.platform.domain.entity.CustomizeRule;
import org.hzero.platform.domain.repository.CustomizeRuleRepository;

/**
 * API个性化规则应用服务默认实现
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
@Service
public class CustomizeRuleServiceImpl implements CustomizeRuleService {

    private final CustomizeRuleRepository customizeRuleRepository;

    public CustomizeRuleServiceImpl(CustomizeRuleRepository customizeRuleRepository) {
        this.customizeRuleRepository = customizeRuleRepository;
    }

    @Override
    public void createRule(CustomizeRule customizeRule) {
        customizeRuleRepository.insertSelective(customizeRule);
        customizeRuleRepository.cacheRule(customizeRule);
    }

    @Override
    public void updateRule(CustomizeRule customizeRule) {
        customizeRuleRepository.updateOptional(customizeRule,
                CustomizeRule.FIELD_RULE_NAME,
                CustomizeRule.FIELD_RULE_POSITION,
                CustomizeRule.FIELD_SYNC_FLAG,
                CustomizeRule.FIELD_TYPE_CODE,
                CustomizeRule.FIELD_TYPE_VALUE,
                CustomizeRule.FIELD_ENABLED_FLAG,
                CustomizeRule.FIELD_TENANT_ID,
                CustomizeRule.FIELD_DESCRIPTION);
        customizeRuleRepository.cacheRule(customizeRule);
    }
}
