package org.hzero.platform.domain.repository;

import java.util.List;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import org.hzero.mybatis.base.BaseRepository;
import org.hzero.platform.domain.entity.CustomizeRange;
import org.hzero.platform.domain.entity.CustomizeRangePoint;
import org.hzero.platform.domain.entity.CustomizeRangeRule;

/**
 * API个性化范围资源库
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
public interface CustomizeRangeRepository extends BaseRepository<CustomizeRange> {

    Page<CustomizeRange> pageRange(PageRequest pageRequest, CustomizeRange customizeRange);

    CustomizeRange selectRangeDetail(Long rangeId);

    void cacheRange(CustomizeRange customizeRange);

    List<CustomizeRangePoint> selectRangePoints(Long rangeId);

    List<CustomizeRangeRule> selectRangeRules(Long rangeId);

    void deleteRangePoints(List<CustomizeRangePoint> rangePoints);

    void deleteRangeRules(List<CustomizeRangeRule> rangeRules);

    void removeRange(CustomizeRange customizeRange);
}
