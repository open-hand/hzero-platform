package org.hzero.platform.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.platform.app.service.CustomizePointService;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.entity.CustomizePoint;
import org.hzero.platform.domain.repository.CustomizePointRepository;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * API个性化切入点 管理 API
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
@Api(tags = PlatformSwaggerApiConfig.CUSTOMIZE_POINT)
@RestController("customizePointController.v1")
@RequestMapping("/v1/{organizationId}/customize-points")
public class CustomizePointController extends BaseController {

    private final CustomizePointRepository customizePointRepository;
    private final CustomizePointService customizePointService;

    public CustomizePointController(CustomizePointRepository customizePointRepository, CustomizePointService customizePointService) {
        this.customizePointRepository = customizePointRepository;
        this.customizePointService = customizePointService;
    }

    @ApiOperation(value = "API个性化切入点列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<CustomizePoint>> list(@PathVariable Long organizationId,
                                                     CustomizePoint customizePoint,
                                                     PageRequest pageRequest) {
        Page<CustomizePoint> list = customizePointRepository.pageCustomizePoint(pageRequest, customizePoint);
        return Results.success(list);
    }

    @ApiOperation(value = "刷新服务切入点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceName", value = "扫描的服务编码", dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "packageNames", value = "扫描的服务包名，多个可用逗号隔开", dataType = "string", paramType = "query", required = true),
    })
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshServicePoint(@PathVariable Long organizationId,
                                                              String serviceName,
                                                              String packageNames) {
        customizePointService.refreshServiceClassMethod(organizationId, serviceName, packageNames);
        return Results.success();
    }

    @ApiOperation(value = "删除API个性化切入点")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<Void> remove(@PathVariable Long organizationId,
                                       @RequestBody @Encrypt List<CustomizePoint> points) {
        SecurityTokenHelper.validToken(points);
        customizePointRepository.batchDeleteByPrimaryKey(points);
        return Results.success();
    }

}
