package org.hzero.platform.api.controller.v1;


import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.platform.api.dto.DatabaseDTO;
import org.hzero.platform.app.service.DatabaseService;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.entity.Database;
import org.hzero.platform.domain.repository.DatabaseRepository;
import org.hzero.platform.infra.constant.Constants;
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
@Api(tags = {PlatformSwaggerApiConfig.DATABASE})
@RestController("databaseController.v1")
@RequestMapping("/v1/{organizationId}/databases")
public class DatabaseController extends BaseController {

    @Autowired
    private DatabaseRepository databaseRepository;

    @Autowired
    private DatabaseService databaseService;

    @ApiOperation(value = "数据库列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    @CustomPageRequest
    public ResponseEntity<Page<DatabaseDTO>> pageDatabase(@PathVariable("organizationId") Long organizationId,
                    DatabaseDTO database, @ApiIgnore PageRequest pageRequest) {
        Page<DatabaseDTO> page = databaseRepository.pageDatabase(database, pageRequest);
        return Results.success(page);
    }

    @ApiOperation(value = "创建数据库")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<Database> createDatabase(@PathVariable("organizationId") Long organizationId,
                    @RequestBody Database database) {
        database.setTenantId(organizationId);
        validObject(database);
        database.setLevelFlag(Constants.TENANT_LEVEL_UPPER_CASE);
        database = databaseService.createDatabase(database);
        return Results.success(database);
    }

    @ApiOperation(value = "修改数据库")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<Database> updateDatabase(@PathVariable("organizationId") Long organizationId,
                    @RequestBody @Encrypt Database database) {
        database.setTenantId(organizationId);
        SecurityTokenHelper.validTokenIgnoreInsert(database);
        validObject(database);
        database.setLevelFlag(Constants.TENANT_LEVEL_UPPER_CASE);
        return Results.success(databaseService.updateDatabase(database));
    }

    @ApiOperation(value = "删除数据库,同时删除数据库下的关系")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity removeByDatabaseId(@PathVariable("organizationId") Long organizationId,
                    @RequestBody @Encrypt Database database) {
        database.setTenantId(organizationId);
        SecurityTokenHelper.validTokenIgnoreInsert(database);
        databaseService.removeByDatabaseId(database.getDatabaseId());
        return Results.success();
    }

}
