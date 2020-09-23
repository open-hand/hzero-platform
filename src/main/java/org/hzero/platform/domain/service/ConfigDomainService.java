package org.hzero.platform.domain.service;

import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.platform.domain.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 系统配置domainService
 * </p>
 *
 * @author yunxiang.zhou01@hand-china.com 2018/06/20 16:59
 */
@Component
public class ConfigDomainService {

    @Autowired
    private RedisHelper redisHelper;

    /**
     * 得到平台级系统配置和租户级系统配置信息，如果租户级系统配置信息不存在，则返回平台级系统配置信息
     * 
     * @param tenantId 租户id，如果想使用平台级系统配置，请传入null
     * @param configCode 配置code
     * @return 配置值
     */
    public String getConfigValue(Long tenantId, String configCode) {
        String key = Config.generateCacheKey(configCode, tenantId);
        String value = redisHelper.strGet(key);
        return value == null ? redisHelper.strGet(Config.generateCacheKey(configCode, BaseConstants.DEFAULT_TENANT_ID))
                        : value;
    }
}
