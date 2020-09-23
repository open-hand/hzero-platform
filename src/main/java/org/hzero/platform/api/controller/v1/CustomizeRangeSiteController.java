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
@Api(tags = PlatformSwaggerApiConfig.CUSTOMIZE_RANGE_SITE)
@RestController("customizeRangeSiteController.v1")
@RequestMapping("/v1/customize-ranges")
public class CustomizeRangeSiteController extends BaseController {

    private final CustomizeRangeRepository customizeRangeRepository;
    private final CustomizeRangeService customizeRangeService;

    public CustomizeRangeSiteController(CustomizeRangeRepository customizeRangeRepository, CustomizeRangeService customizeRangeService) {
        this.customizeRangeRepository = customizeRangeRepository;
        this.customizeRangeService = customizeRangeService;
    }

    @ApiOperation(value = "API个性化范围列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<CustomizeRange>> list(CustomizeRange customizeRange,
                                                     PageRequest pageRequest) {
        return Results.success(customizeRangeRepository.pageRange(pageRequest, customizeRange));
    }

    @ApiOperation(value = "API个性化范围明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{rangeId}")
    public ResponseEntity<CustomizeRange> detail(@PathVariable @Encrypt Long rangeId) {
        CustomizeRange customizeRange = customizeRangeRepository.selectRangeDetail(rangeId);
        return Results.success(customizeRange);
    }

    @ApiOperation(value = "创建API个性化范围")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<CustomizeRange> create(@RequestBody CustomizeRange customizeRange) {
        validObject(customizeRange);
        customizeRangeService.createRange(customizeRange);
        return Results.success(customizeRange);
    }

    @ApiOperation(value = "修改API个性化范围")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<CustomizeRange> update(@RequestBody @Encrypt CustomizeRange customizeRange) {
        SecurityTokenHelper.validToken(customizeRange, false);
        customizeRangeService.updateRange(customizeRange);
        return Results.success(customizeRange);
    }

    @ApiOperation(value = "删除API个性化范围")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<?> remove(@RequestBody @Encrypt CustomizeRange customizeRange) {
        SecurityTokenHelper.validToken(customizeRange, false);
        customizeRangeRepository.removeRange(customizeRange);
        return Results.success();
    }

    @ApiOperation(value = "API个性化范围-切入点列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{rangeId}/points")
    public ResponseEntity<List<CustomizeRangePoint>> listRangePoints(@PathVariable @Encrypt Long rangeId) {
        return Results.success(customizeRangeRepository.selectRangePoints(rangeId));
    }

    @ApiOperation(value = "API个性化范围-规则列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{rangeId}/rules")
    public ResponseEntity<List<CustomizeRangeRule>> listRangeRules(@PathVariable @Encrypt Long rangeId) {
        return Results.success(customizeRangeRepository.selectRangeRules(rangeId));
    }

    @ApiOperation(value = "API个性化范围-批量删除切入点")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping("/{rangeId}/points")
    public ResponseEntity<Void> deleteRangePoints(@PathVariable @Encrypt Long rangeId,
                                                  @RequestBody @Encrypt List<CustomizeRangePoint> rangePoints) {
        customizeRangeRepository.deleteRangePoints(rangePoints);
        return Results.success();
    }

    @ApiOperation(value = "API个性化范围-批量删除规则")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping("/{rangeId}/rules")
    public ResponseEntity<Void> deleteRangeRules(@PathVariable @Encrypt Long rangeId,
                                                 @RequestBody @Encrypt List<CustomizeRangeRule> rangeRules) {
        customizeRangeRepository.deleteRangeRules(rangeRules);
        return Results.success();
    }

    @ApiOperation(value = "API个性化范围-应用规则")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{rangeId}/apply-rules")
    public ResponseEntity<Void> applyCustomizeRule(@PathVariable @Encrypt Long rangeId) {
        customizeRangeService.applyCustomizeRule(rangeId);
        return Results.success();
    }

}
