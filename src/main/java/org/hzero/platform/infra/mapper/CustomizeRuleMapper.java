package org.hzero.platform.infra.mapper;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

import org.hzero.platform.domain.entity.CustomizeRule;

/**
 * API个性化规则Mapper
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
public interface CustomizeRuleMapper extends BaseMapper<CustomizeRule> {

    List<CustomizeRule> selectRule(CustomizeRule customizeRule);

    CustomizeRule selectRuleDetail(Long ruleId);
}
