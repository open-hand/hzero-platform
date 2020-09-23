package org.hzero.platform.app.service;

import org.hzero.platform.domain.entity.CustomizeRule;

/**
 * API个性化规则应用服务
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
public interface CustomizeRuleService {

    void createRule(CustomizeRule customizeRule);

    void updateRule(CustomizeRule customizeRule);
}
