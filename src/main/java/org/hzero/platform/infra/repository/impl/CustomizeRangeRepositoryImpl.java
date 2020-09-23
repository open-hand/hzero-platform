package org.hzero.platform.infra.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.platform.domain.entity.CustomizeRange;
import org.hzero.platform.domain.entity.CustomizeRangePoint;
import org.hzero.platform.domain.entity.CustomizeRangeRule;
import org.hzero.platform.domain.repository.CustomizeRangeRepository;
import org.hzero.platform.infra.mapper.CustomizeRangeMapper;
import org.hzero.platform.infra.mapper.CustomizeRangePointMapper;
import org.hzero.platform.infra.mapper.CustomizeRangeRuleMapper;
import org.hzero.boot.api.customize.commons.repository.ApiCustomizeRepository;
import org.hzero.boot.api.customize.commons.vo.ApiCustomizePoint;

/**
 * API个性化范围 资源库实现
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
@Component
public class CustomizeRangeRepositoryImpl extends BaseRepositoryImpl<CustomizeRange>
                implements CustomizeRangeRepository {

    private final CustomizeRangeMapper customizeRangeMapper;
    private final CustomizeRangePointMapper customizeRangePointMapper;
    private final CustomizeRangeRuleMapper customizeRangeRuleMapper;
    private final ApiCustomizeRepository customizeRepository;

    public CustomizeRangeRepositoryImpl(CustomizeRangeMapper customizeRangeMapper,
                                        CustomizeRangePointMapper customizeRangePointMapper,
                                        CustomizeRangeRuleMapper customizeRangeRuleMapper,
                                        ApiCustomizeRepository customizeRepository) {
        this.customizeRangeMapper = customizeRangeMapper;
        this.customizeRangePointMapper = customizeRangePointMapper;
        this.customizeRangeRuleMapper = customizeRangeRuleMapper;
        this.customizeRepository = customizeRepository;
    }

    @Override
    @ProcessLovValue
    public Page<CustomizeRange> pageRange(PageRequest pageRequest, CustomizeRange customizeRange) {
        return PageHelper.doPage(pageRequest, () -> customizeRangeMapper.selectRange(customizeRange));
    }

    @Override
    public CustomizeRange selectRangeDetail(Long rangeId) {
        CustomizeRange params = new CustomizeRange();
        params.setRangeId(rangeId);
        List<CustomizeRange> list = customizeRangeMapper.selectRange(params);
        return CollectionUtils.isNotEmpty(list) ? list.get(0) : null;
    }

    @Override
    public void cacheRange(CustomizeRange customizeRange) {
        List<CustomizeRangePoint> rangePoints = customizeRange.getRangePoints();
        List<CustomizeRangeRule> rangeRules = customizeRange.getRangeRules();
        if (CollectionUtils.isEmpty(rangePoints) || CollectionUtils.isEmpty(rangeRules)) {
            return;
        }

        List<ApiCustomizePoint> points = rangePoints.stream()
                .map(point -> new ApiCustomizePoint(point.getServiceName(),
                        point.getPackageName(),
                        point.getClassName(),
                        point.getMethodName(),
                        point.getMethodArgs()))
                .collect(Collectors.toList());

        if (Objects.equals(customizeRange.getEnabledFlag(), BaseConstants.Flag.NO)) {
            customizeRepository.deletePointRule(points);
        } else {
            List<String> ruleCodes = rangeRules.stream().map(CustomizeRangeRule::getRuleCode).collect(Collectors.toList());
            customizeRepository.savePointRule(points, ruleCodes);
        }
    }

    @Override
    public List<CustomizeRangePoint> selectRangePoints(Long rangeId) {
        return customizeRangeMapper.selectRangePoints(rangeId);
    }

    @Override
    public List<CustomizeRangeRule> selectRangeRules(Long rangeId) {
        return customizeRangeMapper.selectRangeRules(rangeId);
    }

    @Override
    public void deleteRangePoints(List<CustomizeRangePoint> rangePoints) {
        List<CustomizeRangePoint> dbRangePoints = new ArrayList<>(rangePoints.size());
        for (CustomizeRangePoint rangePoint : rangePoints) {
            CustomizeRangePoint t = customizeRangePointMapper.selectByPrimaryKey(rangePoint.getRangePointId());
            Assert.notNull(t, "range point not found.");
            dbRangePoints.add(t);
        }
        rangePoints.forEach(customizeRangePointMapper::deleteByPrimaryKey);
        customizeRepository.deletePointRule(dbRangePoints.stream()
                .map(p ->
                    new ApiCustomizePoint(
                        p.getServiceName(),
                        p.getPackageName(),
                        p.getClassName(),
                        p.getMethodName(),
                        p.getMethodArgs())
                ).collect(Collectors.toList()));
    }

    @Override
    public void deleteRangeRules(List<CustomizeRangeRule> rangeRules) {
        Long rangeId = rangeRules.get(0).getRangeId();
        CustomizeRange range = customizeRangeMapper.selectByPrimaryKey(rangeId);
        Assert.notNull(range, "customize range not fount.");
        // 删除规则
        rangeRules.forEach(customizeRangeRuleMapper::deleteByPrimaryKey);
        // 查询
        range.setRangeRules(selectRangeRules(rangeId));
        range.setRangePoints(selectRangePoints(rangeId));
        // 缓存
        cacheRange(range);
    }

    @Override
    public void removeRange(CustomizeRange range) {
        range = selectByPrimaryKey(range.getRangeId());
        Assert.notNull(range, "customize range is null.");
        // 删除范围切入点(同时会删除缓存)
        deleteRangePoints(selectRangePoints(range.getRangeId()));
        // 删除范围规则
        selectRangeRules(range.getRangeId()).forEach(customizeRangeRuleMapper::deleteByPrimaryKey);
        // 删除范围
        deleteByPrimaryKey(range);
    }
}
