package org.hzero.platform.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.platform.api.dto.DatabaseTenantDTO;
import org.hzero.platform.app.service.DatabaseTenantService;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.entity.DatabaseTenant;
import org.hzero.platform.domain.repository.DatabaseTenantRepository;
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
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 数据权限数据源关系 管理 API
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
@Api(tags = {PlatformSwaggerApiConfig.DATABASE_TENANT_SITE})
@RestController("databaseSiteTenantController.v1")
@RequestMapping("/v1/database-tenant")
public class DatabaseSiteTenantController extends BaseController {

    @Autowired
    private DatabaseTenantRepository databaseTenantRepository;

    @Autowired
    private DatabaseTenantService databaseTenantService;

    @ApiOperation(value = "数据权限数据源关系列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    @CustomPageRequest
    public ResponseEntity<Page<DatabaseTenantDTO>> pageDatabaseTenant(@Encrypt DatabaseTenantDTO databaseTenant,
                                                                      @ApiIgnore @SortDefault(value = DatabaseTenant.FIELD_DATABASE_TENANT_ID, direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<DatabaseTenantDTO> page = databaseTenantRepository.pageDatabaseTenant(databaseTenant, pageRequest);
        return Results.success(page);
    }

    @ApiOperation(value = "创建数据权限数据源关系")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<DatabaseTenant> createDatabaseTenant(@RequestBody @Encrypt DatabaseTenant databaseTenant) {
        validObject(databaseTenant);
        databaseTenant = databaseTenantService.createDatabaseTenant(databaseTenant);
        return Results.success(databaseTenant);
    }

    @ApiOperation(value = "修改数据权限数据源关系")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<DatabaseTenant> updateDatabaseTenant(@RequestBody @Encrypt DatabaseTenant databaseTenant) {
        SecurityTokenHelper.validTokenIgnoreInsert(databaseTenant);
        validObject(databaseTenant);
        return Results.success(databaseTenantService.updateDatabaseTenant(databaseTenant));
    }

    @ApiOperation(value = "删除数据权限数据源关系")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity removeDatabaseTenantById(@RequestBody @Encrypt DatabaseTenant databaseTenant) {
        SecurityTokenHelper.validTokenIgnoreInsert(databaseTenant);
        databaseTenantService.removeDatabaseTenantById(databaseTenant.getDatabaseTenantId());
        return Results.success();
    }

}
