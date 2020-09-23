package org.hzero.platform.app.service.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.choerodon.core.exception.CommonException;

import org.hzero.platform.app.service.CustomizePointService;
import org.hzero.platform.domain.entity.CustomizePoint;
import org.hzero.platform.domain.repository.CustomizePointRepository;
import org.hzero.platform.infra.remote.api.RemoteCustomizeService;
import org.hzero.boot.api.customize.commons.vo.MethodMetaData;

/**
 * API个性化切入点应用服务默认实现
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
@Service
public class CustomizePointServiceImpl implements CustomizePointService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizePointServiceImpl.class);

    private final RemoteCustomizeService remoteCustomizeService;
    private final CustomizePointRepository customizePointRepository;

    public CustomizePointServiceImpl(RemoteCustomizeService remoteCustomizeService,
                                     CustomizePointRepository customizePointRepository) {
        this.remoteCustomizeService = remoteCustomizeService;
        this.customizePointRepository = customizePointRepository;
    }

    @Override
    public void refreshServiceClassMethod(Long tenantId, String serviceName, String packageNames) {
        if (StringUtils.isBlank(serviceName)) {
            throw new CommonException("hpfm.warn.refreshClass.serviceNameNotNull");
        }
        if (StringUtils.isBlank(packageNames)) {
            throw new CommonException("hpfm.warn.refreshClass.packageNamesNotNull");
        }
        serviceName = StringUtils.lowerCase(serviceName);
        List<MethodMetaData> methods = remoteCustomizeService.fetchServiceMethods(serviceName, packageNames);
        if (CollectionUtils.isEmpty(methods)) {
            return;
        }
        String finalServiceName = serviceName;
        methods.parallelStream().forEach(method -> {
            CustomizePoint point = new CustomizePoint(finalServiceName, method.getPackageName(), method.getClassName(),
                            method.getMethodName(), method.getMethodArgs(), tenantId);
            if (customizePointRepository.selectCount(point) > 0) {
                LOGGER.debug("CustomizePoint exists, point = {}", point);
            } else {
                customizePointRepository.insertSelective(point);
            }
        });
    }



}
