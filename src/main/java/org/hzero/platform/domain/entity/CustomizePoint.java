package org.hzero.platform.domain.entity;

import java.util.StringJoiner;
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
 * API个性化切入点
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
@ApiModel("API个性化切入点")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hpfm_customize_point")
public class CustomizePoint extends AuditDomain {

    public static final String FIELD_POINT_ID = "pointId";
    public static final String FIELD_SERVICE_NAME = "serviceName";
    public static final String FIELD_PACKAGE_NAME = "packageName";
    public static final String FIELD_CLASS_NAME = "className";
    public static final String FIELD_METHOD_NAME = "methodName";
    public static final String FIELD_METHOD_ARGS = "methodArgs";
    public static final String FIELD_METHOD_DESCRIPTION = "methodDescription";


    public interface Insert{}

    public CustomizePoint() {
    }

    public CustomizePoint(String serviceName, String packageName, String className,
                          String methodName, String methodArgs) {
        this.serviceName = serviceName;
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.methodArgs = methodArgs;
    }

    public CustomizePoint(String serviceName, String packageName, String className,
                          String methodName, String methodArgs, Long tenantId) {
        this.serviceName = serviceName;
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.methodArgs = methodArgs;
        this.tenantId = tenantId;
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    @Encrypt
    private Long pointId;
    @ApiModelProperty(value = "服务名")
    @NotBlank
    private String serviceName;
    @ApiModelProperty(value = "包名")
    @NotBlank
    private String packageName;
    @ApiModelProperty(value = "类名")
    @NotBlank
    private String className;
    @ApiModelProperty(value = "方法名")
    @NotBlank
    private String methodName;
    @ApiModelProperty(value = "方法参数列表")
    @NotBlank
    private String methodArgs;
    @ApiModelProperty(value = "方法描述")
    private String methodDescription;
    @NotNull(groups = Insert.class)
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

    public CustomizePoint setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 表ID，主键，供其他表做外键
     */
    public Long getPointId() {
        return pointId;
    }

    public void setPointId(Long pointId) {
        this.pointId = pointId;
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

    /**
     * @return 方法描述
     */
    public String getMethodDescription() {
        return methodDescription;
    }

    public void setMethodDescription(String methodDescription) {
        this.methodDescription = methodDescription;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CustomizePoint.class.getSimpleName() + "[", "]")
                .add("pointId=" + pointId)
                .add("serviceName='" + serviceName + "'")
                .add("packageName='" + packageName + "'")
                .add("className='" + className + "'")
                .add("methodName='" + methodName + "'")
                .add("methodArgs='" + methodArgs + "'")
                .add("methodDescription='" + methodDescription + "'")
                .toString();
    }
}
