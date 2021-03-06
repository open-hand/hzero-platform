package org.hzero.platform.api.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.service.ConfigDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;

/**
 * <p>
 * 系统配置controller
 * </p>
 *
 * @author yunxiang.zhou01@hand-china.com 2018/06/19 13:39
 */
@Api(tags = PlatformSwaggerApiConfig.CONFIG)
@RestController("configController.v1")
@RequestMapping("/v1/{organizationId}/config/value")
public class ConfigController extends BaseController {

    @Autowired
    private ConfigDomainService configDomainService;

    /**
     * 获取系统配置信息
     *
     * @return 系统配置信息
     */
    @ApiOperation(value = "获取系统配置信息", notes = "获取系统配置信息")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "租户ID", paramType = "path", required = true),
            @ApiImplicitParam(name = "configCode", value = "系统配置CODE", paramType = "query", required = true)
    })
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<String> getConfigValue(@PathVariable Long organizationId, String configCode) {
        return Results.success(configDomainService.getConfigValue(organizationId, configCode));
    }
}
