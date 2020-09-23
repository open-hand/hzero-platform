package org.hzero.platform.infra.mapper;

import java.util.List;

import io.choerodon.mybatis.common.BaseMapper;
import org.hzero.platform.domain.entity.Config;

/**
 * <p>
 * 系统配置mapper
 * </p>
 *
 * @author yunxiang.zhou01@hand-china.com 2018/06/19 11:42
 */
public interface ConfigMapper extends BaseMapper<Config> {

    /**
     * 根据租户id查询租户级的配置，如果没有配置则引用平台级的配置，如果有则采用自己的
     *
     * @param tenantId 租户id
     * @return 系统配置list
     */
    List<Config> selectConfigByTenantId(Long tenantId);
}
