package org.hzero.platform.api.controller.v1;

import java.util.List;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.platform.app.service.CustomizeRangeService;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.entity.CustomizeRange;
import org.hzero.platform.domain.entity.CustomizeRangePoint;
import org.hzero.platform.domain.entity.CustomizeRangeRule;
import org.hzero.platform.domain.repository.CustomizeRangeRepository;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * API个性化范围 管理 API
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
@Api(tags = PlatformSwaggerApiConfig.CUSTOMIZE_RANGE)
@RestController("customizeRangeController.v1")
@RequestMapping("/v1/{organizationId}/customize-ranges")
public class CustomizeRangeController extends BaseController {

    private final CustomizeRangeRepository customizeRangeRepository;
    private final CustomizeRangeService customizeRangeService;

    public CustomizeRangeController(CustomizeRangeRepository customizeRangeRepository, CustomizeRangeService customizeRangeService) {
        this.customizeRangeRepository = customizeRangeRepository;
        this.customizeRangeService = customizeRangeService;
    }

    @ApiOperation(value = "API个性化范围列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<CustomizeRange>> list(@PathVariable Long organizationId,
                                                     CustomizeRange customizeRange,
                                                     PageRequest pageRequest) {
        customizeRange.setTenantId(organizationId);
        return Results.success(customizeRangeRepository.pageRange(pageRequest, customizeRange));
    }

    @ApiOperation(value = "API个性化范围明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{rangeId}")
    public ResponseEntity<CustomizeRange> detail(@PathVariable Long organizationId,
                                                 @PathVariable @Encrypt Long rangeId) {
        CustomizeRange customizeRange = customizeRangeRepository.selectRangeDetail(rangeId);
        return Results.success(customizeRange);
    }

    @ApiOperation(value = "创建API个性化范围")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<CustomizeRange> create(@PathVariable Long organizationId,
                                                 @RequestBody CustomizeRange customizeRange) {
        customizeRange.setTenantId(organizationId);
        validObject(customizeRange);
        customizeRangeService.createRange(customizeRange);
        return Results.success(customizeRange);
    }

    @ApiOperation(value = "修改API个性化范围")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<CustomizeRange> update(@PathVariable Long organizationId,
                                                 @RequestBody @Encrypt CustomizeRange customizeRange) {
        customizeRange.setTenantId(organizationId);
        SecurityTokenHelper.validToken(customizeRange, false);
        customizeRangeService.updateRange(customizeRange);
        return Results.success(customizeRange);
    }

    @ApiOperation(value = "删除API个性化范围")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity<?> remove(@PathVariable Long organizationId,
                                    @RequestBody @Encrypt CustomizeRange customizeRange) {
        SecurityTokenHelper.validToken(customizeRange, false);
        customizeRangeRepository.removeRange(customizeRange);
        return Results.success();
    }

    @ApiOperation(value = "API个性化范围-切入点列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{rangeId}/points")
    public ResponseEntity<List<CustomizeRangePoint>> listRangePoints(@PathVariable Long organizationId,
                                                          @PathVariable @Encrypt Long rangeId) {
        return Results.success(customizeRangeRepository.selectRangePoints(rangeId));
    }

    @ApiOperation(value = "API个性化范围-规则列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{rangeId}/rules")
    public ResponseEntity<List<CustomizeRangeRule>> listRangeRules(@PathVariable Long organizationId,
                                                          @PathVariable @Encrypt Long rangeId) {
        return Results.success(customizeRangeRepository.selectRangeRules(rangeId));
    }

    @ApiOperation(value = "API个性化范围-批量删除切入点")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping("/{rangeId}/points")
    public ResponseEntity<Void> deleteRangePoints(@PathVariable Long organizationId,
                                                  @PathVariable @Encrypt Long rangeId,
                                                  @RequestBody @Encrypt List<CustomizeRangePoint> rangePoints) {
        customizeRangeRepository.deleteRangePoints(rangePoints);
        return Results.success();
    }

    @ApiOperation(value = "API个性化范围-批量删除规则")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping("/{rangeId}/rules")
    public ResponseEntity<Void> deleteRangeRules(@PathVariable Long organizationId,
                                                 @PathVariable @Encrypt Long rangeId,
                                                 @RequestBody @Encrypt List<CustomizeRangeRule> rangeRules) {
        customizeRangeRepository.deleteRangeRules(rangeRules);
        return Results.success();
    }

    @ApiOperation(value = "API个性化范围-应用规则")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/{rangeId}/apply-rules")
    public ResponseEntity<Void> applyCustomizeRule(@PathVariable Long organizationId,
                                                   @PathVariable @Encrypt Long rangeId) {
        customizeRangeService.applyCustomizeRule(rangeId);
        return Results.success();
    }

}
