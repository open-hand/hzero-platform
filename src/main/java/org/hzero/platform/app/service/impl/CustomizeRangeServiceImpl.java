package org.hzero.platform.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.choerodon.core.exception.CommonException;

import org.hzero.core.base.BaseConstants;
import org.hzero.platform.app.service.CustomizeRangeService;
import org.hzero.platform.domain.entity.CustomizeRange;
import org.hzero.platform.domain.entity.CustomizeRangePoint;
import org.hzero.platform.domain.entity.CustomizeRangeRule;
import org.hzero.platform.domain.entity.CustomizeRule;
import org.hzero.platform.domain.repository.CustomizeRangeRepository;
import org.hzero.platform.domain.repository.CustomizeRuleRepository;
import org.hzero.platform.infra.mapper.CustomizeRangePointMapper;
import org.hzero.platform.infra.mapper.CustomizeRangeRuleMapper;
import org.hzero.platform.infra.remote.api.RemoteCustomizeService;

/**
 * API个性化范围应用服务默认实现
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
@Service
public class CustomizeRangeServiceImpl implements CustomizeRangeService {

    private final CustomizeRangeRuleMapper customizeRangeRuleMapper;
    private final CustomizeRangePointMapper customizeRangePointMapper;
    private final CustomizeRangeRepository customizeRangeRepository;
    private final CustomizeRuleRepository customizeRuleRepository;
    private final RemoteCustomizeService remoteCustomizeService;

    public CustomizeRangeServiceImpl(CustomizeRangePointMapper customizeRangePointMapper,
                                     CustomizeRangeRuleMapper customizeRangeRuleMapper,
                                     CustomizeRangeRepository customizeRangeRepository,
                                     CustomizeRuleRepository customizeRuleRepository,
                                     RemoteCustomizeService remoteCustomizeService) {
        this.customizeRangePointMapper = customizeRangePointMapper;
        this.customizeRangeRuleMapper = customizeRangeRuleMapper;
        this.customizeRangeRepository = customizeRangeRepository;
        this.customizeRuleRepository = customizeRuleRepository;
        this.remoteCustomizeService = remoteCustomizeService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CustomizeRange createRange(CustomizeRange customizeRange) {
        customizeRangeRepository.insertSelective(customizeRange);

        handleRangeRel(customizeRange, false);

        return customizeRange;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRange(CustomizeRange customizeRange) {
        customizeRangeRepository.updateOptional(customizeRange, CustomizeRange.FIELD_ENABLED_FLAG, CustomizeRange.FIELD_DESCRIPTION);

        handleRangeRel(customizeRange, true);
    }

    @Override
    public void applyCustomizeRule(Long rangeId) {
        List<String> serviceNames = customizeRangeRepository.selectRangePoints(rangeId).stream().map(CustomizeRangePoint::getServiceName).collect(Collectors.toList());
        List<String> ruleCodes = customizeRangeRepository.selectRangeRules(rangeId).stream().map(CustomizeRangeRule::getRuleCode).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(serviceNames) && CollectionUtils.isNotEmpty(ruleCodes)) {
            // 通知服务应用
            remoteCustomizeService.applyCustomizeRule(serviceNames, ruleCodes);
        }
    }

    private void handleRangeRel(CustomizeRange customizeRange, boolean update) {
        if (CollectionUtils.isNotEmpty(customizeRange.getRangeRules())) {
            for (CustomizeRangeRule rangeRule : customizeRange.getRangeRules()) {
                CustomizeRule rule = customizeRuleRepository.selectRuleDetail(rangeRule.getRuleId());
                if (rule == null) {
                    throw new CommonException("hpfm.warn.range.ruleNotFound");
                }
                if (!(rule.getTenantId().equals(BaseConstants.DEFAULT_TENANT_ID) ||
                        rule.getTenantId().equals(customizeRange.getTenantId()))) {
                    throw new CommonException("hpfm.warn.range.tenantNotEquals");
                }
                rangeRule.setCustomizeRule(rule);
            }
            for (CustomizeRangeRule rangeRule : customizeRange.getRangeRules()) {
                rangeRule.setRangeId(customizeRange.getRangeId());
                rangeRule.setTenantId(customizeRange.getTenantId());
                customizeRangeRuleMapper.insertSelective(rangeRule);
            }
        }
        if (CollectionUtils.isNotEmpty(customizeRange.getRangePoints())) {
            for (CustomizeRangePoint rangePoint : customizeRange.getRangePoints()) {
                rangePoint.setRangeId(customizeRange.getRangeId());
                rangePoint.setTenantId(customizeRange.getTenantId());
                customizeRangePointMapper.insertSelective(rangePoint);
            }
        }
        if (update) {
            customizeRange.setRangePoints(customizeRangeRepository.selectRangePoints(customizeRange.getRangeId()));
            customizeRange.setRangeRules(customizeRangeRepository.selectRangeRules(customizeRange.getRangeId()));
        }

        // 缓存
        customizeRangeRepository.cacheRange(customizeRange);
    }

}
