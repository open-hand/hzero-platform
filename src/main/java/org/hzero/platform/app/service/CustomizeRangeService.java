package org.hzero.platform.app.service;

import org.hzero.platform.domain.entity.CustomizeRange;

/**
 * API个性化范围应用服务
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
public interface CustomizeRangeService {

    CustomizeRange createRange(CustomizeRange customizeRange);

    void updateRange(CustomizeRange customizeRange);

    void applyCustomizeRule(Long rangeId);
}
