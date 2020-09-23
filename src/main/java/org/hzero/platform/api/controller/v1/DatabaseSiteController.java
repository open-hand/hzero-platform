package org.hzero.platform.api.controller.v1;


import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.platform.api.dto.DatabaseDTO;
import org.hzero.platform.app.service.DatabaseService;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.entity.Database;
import org.hzero.platform.domain.repository.DatabaseRepository;
import org.hzero.starter.keyencrypt.core.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;

import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.CustomPageRequest;
import io.choerodon.swagger.annotation.Permission;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 数据库 管理 API
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
@Api(tags = {PlatformSwaggerApiConfig.DATABASE_SITE})
@RestController("databaseSiteController.v1")
@RequestMapping("/v1/databases")
public class DatabaseSiteController extends BaseController {

    @Autowired
    private DatabaseRepository databaseRepository;

    @Autowired
    private DatabaseService databaseService;

    @ApiOperation(value = "数据库列表")
    @Permission(level = ResourceLevel.SITE)
    @GetMapping
    @CustomPageRequest
    public ResponseEntity<Page<DatabaseDTO>> pageDatabase(DatabaseDTO database, @ApiIgnore PageRequest pageRequest) {
        Page<DatabaseDTO> page = databaseRepository.pageDatabase(database, pageRequest);
        return Results.success(page);
    }

    @ApiOperation(value = "创建数据库")
    @Permission(level = ResourceLevel.SITE)
    @PostMapping
    public ResponseEntity<Database> createDatabase(@RequestBody Database database) {
        database.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        validObject(database);
        database = databaseService.createDatabase(database);
        return Results.success(database);
    }

    @ApiOperation(value = "修改数据库")
    @Permission(level = ResourceLevel.SITE)
    @PutMapping
    public ResponseEntity<Database> updateDatabase(@RequestBody @Encrypt Database database) {
        database.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        SecurityTokenHelper.validTokenIgnoreInsert(database);
        validObject(database);
        return Results.success(databaseService.updateDatabase(database));
    }

    @ApiOperation(value = "删除数据库,同时删除数据库下的关系")
    @Permission(level = ResourceLevel.SITE)
    @DeleteMapping
    public ResponseEntity removeByDatabaseId(@RequestBody @Encrypt Database database) {
        database.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
        SecurityTokenHelper.validTokenIgnoreInsert(database);
        databaseService.removeByDatabaseId(database.getDatabaseId());
        return Results.success();
    }

}
