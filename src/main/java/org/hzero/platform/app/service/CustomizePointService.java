package org.hzero.platform.app.service;

/**
 * API个性化切入点应用服务
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
public interface CustomizePointService {

    /**
     * 刷新服务方法
     * @param serviceName 服务名称
     * @param packageNames 包名，多个包用逗号隔开
     */
    void refreshServiceClassMethod(Long tenantId, String serviceName, String packageNames);
}
