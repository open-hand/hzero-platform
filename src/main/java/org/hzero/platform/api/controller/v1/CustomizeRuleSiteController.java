package org.hzero.platform.api.controller.v1;

import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.platform.app.service.CustomizeRuleService;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.entity.CustomizeRule;
import org.hzero.platform.domain.repository.CustomizeRuleRepository;
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
 * API个性化规则 管理 API
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
@Api(tags = PlatformSwaggerApiConfig.CUSTOMIZE_RULE_SITE)
@RestController("customizeRuleSiteController.v1")
@RequestMapping("/v1/customize-rules")
public class CustomizeRuleSiteController extends BaseController {

    private final CustomizeRuleRepository customizeRuleRepository;
    private final CustomizeRuleService customizeRuleService;

    public CustomizeRuleSiteController(CustomizeRuleRepository customizeRuleRepository,
                                       CustomizeRuleService customizeRuleService) {
        this.customizeRuleRepository = customizeRuleRepository;
        this.customizeRuleService = customizeRuleService;
    }

    @ApiOperation(value = "API个性化规则列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity<Page<CustomizeRule>> list(CustomizeRule customizeRule,
                                                    PageRequest pageRequest) {
        return Results.success(customizeRuleRepository.pageRule(pageRequest, customizeRule));
    }

    @ApiOperation(value = "API个性化规则明细")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping("/{ruleId}")
    @ProcessLovValue(targetField = BaseConstants.FIELD_BODY)
    public ResponseEntity<CustomizeRule> detail(@PathVariable  @Encrypt Long ruleId) {
        return Results.success(customizeRuleRepository.selectRuleDetail(ruleId));
    }

    @ApiOperation(value = "创建API个性化规则")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<CustomizeRule> create(@RequestBody @Encrypt CustomizeRule customizeRule) {
        validObject(customizeRule);
        customizeRuleService.createRule(customizeRule);
        return Results.success(customizeRule);
    }

    @ApiOperation(value = "修改API个性化规则")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<CustomizeRule> update(@RequestBody @Encrypt CustomizeRule customizeRule) {
        SecurityTokenHelper.validToken(customizeRule);
        validObject(customizeRule);
        customizeRuleService.updateRule(customizeRule);
        return Results.success(customizeRule);
    }

    @ApiOperation(value = "删除API个性化规则")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity<Void> remove(@RequestBody @Encrypt CustomizeRule customizeRule) {
        SecurityTokenHelper.validToken(customizeRule);
        customizeRuleRepository.deleteByPrimaryKey(customizeRule);
        return Results.success();
    }

}
