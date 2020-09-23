package org.hzero.platform.api.controller.v1;

import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.platform.app.service.CommonTemplateService;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.entity.CommonTemplate;
import org.hzero.platform.domain.repository.CommonTemplateRepository;
import org.hzero.platform.infra.constant.Constants;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 通用模板管理
 *
 * @author xiaoyu.zhao@hand-china.com 2019/06/25 15:38
 */
@Api(tags = PlatformSwaggerApiConfig.COMMON_TEMPLATE)
@RestController("commonTemplateController.v1")
@RequestMapping("/v1/{organizationId}/common-templates")
public class CommonTemplateController extends BaseController {

    @Autowired
    private CommonTemplateService templateService;
    @Autowired
    private CommonTemplateRepository templateRepository;

    @ApiOperation(value = "模板列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<CommonTemplate>> list(@PathVariable("organizationId") Long tenantId,
                    CommonTemplate templates, @ApiIgnore @SortDefault(value = CommonTemplate.FIELD_TEMPLATE_ID,
                                    direction = Sort.Direction.DESC) PageRequest pageRequest) {
        templates.setTenantId(tenantId);
        templates.setSiteQueryFlag(BaseConstants.Flag.NO);
        // 租户级创建的模板默认设置为全局层
        templates.setTemplateLevelCode(Constants.SITE_LEVEL_UPPER_CASE);
        return Results.success(templateRepository.selectTemplates(pageRequest, templates));
    }

    @ApiOperation(value = "创建模板")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<CommonTemplate> create(@RequestBody CommonTemplate templates,
                    @PathVariable("organizationId") Long tenantId) {
        templates.setTenantId(tenantId);
        templates.setTemplateLevelCode(Constants.SITE_LEVEL_UPPER_CASE);
        validObject(templates);
        return Results.success(templateService.insertTemplates(templates));
    }

    @ApiOperation(value = "修改模板")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<CommonTemplate> update(@RequestBody @Encrypt CommonTemplate templates,
                    @PathVariable("organizationId") Long tenantId) {
        templates.setTenantId(tenantId);
        validObject(templates);
        SecurityTokenHelper.validTokenIgnoreInsert(templates);
        return Results.success(templateService.updateTemplate(templates));
    }

    @ApiOperation(value = "删除模板")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity remove(@RequestBody @Encrypt CommonTemplate templates) {
        SecurityTokenHelper.validTokenIgnoreInsert(templates);
        templateService.removeTemplate(templates);
        return Results.success();
    }

}
