package org.hzero.platform.api.controller.v1;

import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.platform.app.service.CommonTemplateService;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.entity.CommonTemplate;
import org.hzero.platform.domain.repository.CommonTemplateRepository;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;

import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.annotation.SortDefault;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.mybatis.pagehelper.domain.Sort;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 通用模板
 *
 * @author xiaoyu.zhao@hand-china.com 2019/06/26 10:53
 */
@Api(tags = PlatformSwaggerApiConfig.COMMON_TEMPLATE_SITE)
@RestController("commonTemplateSiteController.v1")
@RequestMapping("/v1/common-templates")
public class CommonTemplateSiteController extends BaseController {

    @Autowired
    private CommonTemplateService templateService;
    @Autowired
    private CommonTemplateRepository templateRepository;

    @ApiOperation(value = "门户模板列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    public ResponseEntity list(CommonTemplate template, @ApiIgnore @SortDefault(value = CommonTemplate.FIELD_TEMPLATE_ID,
            direction = Sort.Direction.DESC) PageRequest pageRequest) {
        template.setSiteQueryFlag(BaseConstants.Flag.YES);
        return Results.success(templateRepository.selectTemplates(pageRequest, template));
    }

    @ApiOperation(value = "创建门户模板")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity create(@RequestBody CommonTemplate template) {
        validObject(template);
        return Results.success(templateService.insertTemplates(template));
    }

    @ApiOperation(value = "修改门户模板")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity update(@RequestBody @Encrypt CommonTemplate template) {
        validObject(template);
        SecurityTokenHelper.validTokenIgnoreInsert(template);
        return Results.success(templateService.updateTemplate(template));
    }

    @ApiOperation(value = "删除门户模板")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity remove(@RequestBody @Encrypt CommonTemplate template) {
        SecurityTokenHelper.validTokenIgnoreInsert(template);
        templateService.removeTemplate(template);
        return Results.success();
    }

}
