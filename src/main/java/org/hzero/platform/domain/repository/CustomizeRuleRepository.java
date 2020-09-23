package org.hzero.platform.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import org.hzero.mybatis.base.BaseRepository;
import org.hzero.platform.domain.entity.CustomizeRule;

/**
 * API个性化规则资源库
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
public interface CustomizeRuleRepository extends BaseRepository<CustomizeRule> {

    Page<CustomizeRule> pageRule(PageRequest pageRequest, CustomizeRule customizeRule);

    CustomizeRule selectRuleDetail(Long ruleId);

    void cacheRule(CustomizeRule customizeRule);

    void removeRule(CustomizeRule customizeRule);
}
