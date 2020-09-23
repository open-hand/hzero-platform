package org.hzero.platform.api.controller.v1;

import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.platform.app.service.DatasourceServiceService;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.entity.DatasourceService;
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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 服务数据源关系 管理 API
 *
 * @author like.zhang@hand-china.com 2018-09-13 14:10:12
 */
@Api(tags = {PlatformSwaggerApiConfig.DATASOURCE_SERVICE})
@RestController("datasourceServiceController.v1")
@RequestMapping("/v1/{organizationId}/datasource-services")
public class DatasourceServiceController extends BaseController {

    @Autowired
    private DatasourceServiceService datasourceServiceService;

    @ApiOperation(value = "服务数据源关系列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "datasourceId", value = "数据源id", paramType = "query"),
            @ApiImplicitParam(name = "serviceName", value = "服务名称", paramType = "query")
    })
    @CustomPageRequest
    public ResponseEntity<Page<DatasourceService>> pageDatasourceService(@Encrypt Long datasourceId, String serviceName,
                                                                         @ApiIgnore @SortDefault(value = DatasourceService.FIELD_DATASOURCE_ID, direction = Sort.Direction.DESC) PageRequest pageRequest) {
        Page<DatasourceService> page = datasourceServiceService.pageDatasourceService(pageRequest, datasourceId, serviceName);
        return Results.success(page);
    }

    @ApiOperation(value = "创建服务数据源关系")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<DatasourceService> createDatasourceService(@RequestBody DatasourceService datasourceService) {
        validObject(datasourceService);
        datasourceService = datasourceServiceService.createDatasourceService(datasourceService);
        return Results.success(datasourceService);
    }

    @ApiOperation(value = "修改服务数据源关系")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PutMapping
    public ResponseEntity<DatasourceService> updateDatasourceService(@RequestBody @Encrypt DatasourceService datasourceService) {
        SecurityTokenHelper.validTokenIgnoreInsert(datasourceService);
        validObject(datasourceService);
        datasourceService = datasourceServiceService.updateDatasourceService(datasourceService);
        return Results.success(datasourceService);
    }

    @ApiOperation(value = "删除服务数据源关系")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @DeleteMapping
    public ResponseEntity removeDatasourceServiceById(@RequestBody @Encrypt DatasourceService datasourceService) {
        SecurityTokenHelper.validTokenIgnoreInsert(datasourceService);
        datasourceServiceService.deleteByDatasourceServiceId(datasourceService.getDatasourceServiceId());
        return Results.success();
    }

}
