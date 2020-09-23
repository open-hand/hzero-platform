package org.hzero.platform.domain.entity;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hzero.starter.keyencrypt.core.Encrypt;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * API个性化范围
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
@ApiModel("API个性化范围")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hpfm_customize_range")
public class CustomizeRange extends AuditDomain {

    public static final String FIELD_RANGE_ID = "rangeId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_DESCRIPTION = "description";


    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    @Encrypt
    private Long rangeId;
    @ApiModelProperty(value = "租户ID,hpfm_tenant.tenant_id")
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "是否启用。1启用，0未启用")
    private Integer enabledFlag;
    @ApiModelProperty(value = "描述")
    @NotBlank
    @Length(max = 480)
    private String description;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    @Transient
    private List<CustomizeRangePoint> rangePoints;
    @Transient
    private List<CustomizeRangeRule> rangeRules;

    @Transient
    private String tenantNum;
    @Transient
    private String tenantName;


    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键，供其他表做外键
     */
    public Long getRangeId() {
        return rangeId;
    }

    public void setRangeId(Long rangeId) {
        this.rangeId = rangeId;
    }

    /**
     * @return 租户ID,hpfm_tenant.tenant_id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @return 是否启用。1启用，0未启用
     */
    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    /**
     * @return 描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CustomizeRangePoint> getRangePoints() {
        return rangePoints;
    }

    public void setRangePoints(List<CustomizeRangePoint> rangePoints) {
        this.rangePoints = rangePoints;
    }

    public List<CustomizeRangeRule> getRangeRules() {
        return rangeRules;
    }

    public void setRangeRules(List<CustomizeRangeRule> rangeRules) {
        this.rangeRules = rangeRules;
    }

    public String getTenantNum() {
        return tenantNum;
    }

    public void setTenantNum(String tenantNum) {
        this.tenantNum = tenantNum;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }
}
