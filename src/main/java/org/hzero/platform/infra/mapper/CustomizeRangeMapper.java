package org.hzero.platform.infra.mapper;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;

import org.hzero.platform.domain.entity.CustomizeRange;
import org.hzero.platform.domain.entity.CustomizeRangePoint;
import org.hzero.platform.domain.entity.CustomizeRangeRule;

/**
 * API个性化范围Mapper
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
public interface CustomizeRangeMapper extends BaseMapper<CustomizeRange> {

    List<CustomizeRange> selectRange(CustomizeRange customizeRange);

    List<CustomizeRangePoint> selectRangePoints(Long rangeId);

    List<CustomizeRangeRule> selectRangeRules(Long rangeId);
}
