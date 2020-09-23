package org.hzero.platform.api.controller.v1;

import io.choerodon.core.exception.CommonException;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.platform.app.service.LovViewHeaderService;
import org.hzero.platform.config.PlatformSwaggerApiConfig;
import org.hzero.platform.domain.vo.LovViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 值集视图API v1
 *
 * @author gaokuo.dai@hand-china.com    2018年6月8日上午10:07:33
 */
@Api(tags = PlatformSwaggerApiConfig.LOV_VIEW)
@RestController("lovViewController.v1")
@RequestMapping("/v1")
public class LovViewController extends BaseController {

    @Autowired
    private LovViewHeaderService lovViewHeaderService;

    /**
     * 获取值集视图信息
     *
     * @param viewCode 视图代码
     * @param tenantId 租户ID(可空)
     * @return 值集视图信息
     */
    @ApiOperation("获取值集视图信息")
    @Permission(permissionLogin = true)
    @GetMapping("/lov-view/info")
    public ResponseEntity<LovViewVO> queryLovViewInfo(@ApiParam(value = "视图代码", required = true) @RequestParam("viewCode") String viewCode,
                                                      @ApiParam("租户ID") @RequestParam(name = "tenantId", required = false) Long tenantId) {
        return commonResult(this.lovViewHeaderService.queryLovViewInfo(viewCode, tenantId));
    }

    /**
     * 获取值集视图信息
     *
     * @param viewCode 视图代码
     * @param tenantId 租户ID(可空)
     * @return 值集视图信息
     */
    @ApiOperation("获取值集视图信息")
    @Permission(permissionLogin = true)
    @GetMapping("/{organizationId}/lov-view/info")
    public ResponseEntity<LovViewVO> queryOrgLovViewInfo(@ApiParam("租户ID") @PathVariable("organizationId") long organizationId,
                                                         @ApiParam(value = "视图代码", required = true) @RequestParam("viewCode") String viewCode,
                                                         @ApiParam(value = "租户ID") @RequestParam(required = false) Long tenantId) {
        if (tenantId != null) {
            organizationId = tenantId;
        }
        return commonResult(this.lovViewHeaderService.queryLovViewInfo(viewCode, organizationId));
    }

    /**
     * 通用返回值处理程序
     *
     * @param payload 载荷
     * @return ResponseEntity 返回值
     */
    private <T> ResponseEntity<T> commonResult(T payload) {
        if (payload == null) {
            throw new CommonException(BaseConstants.ErrorCode.FORBIDDEN);
        } else {
            return Results.success(payload);
        }
    }

}
