package org.hzero.platform.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

/**
 * API个性化范围切入点关系
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:45:51
 */
@ApiModel("API个性化范围切入点关系")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hpfm_customize_range_point")
public class CustomizeRangePoint extends AuditDomain {



    public interface Insert{}

    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    @Encrypt
    private Long rangePointId;
    @ApiModelProperty(value = "范围ID")
    private Long rangeId;
    @ApiModelProperty(value = "服务名", required = true)
    @NotBlank
    private String serviceName;
    @ApiModelProperty(value = "包名", required = true)
    @NotBlank
    private String packageName;
    @ApiModelProperty(value = "类名")
    private String className;
    @ApiModelProperty(value = "方法名")
    private String methodName;
    @ApiModelProperty(value = "方法参数列表")
    private String methodArgs;
    @NotNull(groups=Insert.class)
    private Long tenantId;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    public Long getTenantId() {
        return tenantId;
    }

    public CustomizeRangePoint setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public Long getRangePointId() {
        return rangePointId;
    }

    public void setRangePointId(Long rangePointId) {
        this.rangePointId = rangePointId;
    }

    public Long getRangeId() {
        return rangeId;
    }

    public void setRangeId(Long rangeId) {
        this.rangeId = rangeId;
    }

    /**
     * @return 服务名
     */
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return 包名
     */
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return 类名
     */
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return 方法名
     */
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * @return 方法参数列表
     */
    public String getMethodArgs() {
        return methodArgs;
    }

    public void setMethodArgs(String methodArgs) {
        this.methodArgs = methodArgs;
    }

}
