package org.hzero.platform.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.common.HZeroService;
import org.hzero.core.util.Regexs;
import org.hzero.starter.keyencrypt.core.Encrypt;

/**
 * API个性化规则
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
@ApiModel("API个性化规则")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hpfm_customize_rule")
public class CustomizeRule extends AuditDomain {

    public static final String FIELD_RULE_ID = "ruleId";
    public static final String FIELD_RULE_CODE = "ruleCode";
    public static final String FIELD_RULE_NAME = "ruleName";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_TYPE_CODE = "typeCode";
    public static final String FIELD_TYPE_VALUE = "typeValue";
    public static final String FIELD_RULE_POSITION = "rulePosition";
    public static final String FIELD_SYNC_FLAG = "syncFlag";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_DESCRIPTION = "description";

    public static final String CACHE_KEY = HZeroService.Platform.CODE + ":customize:rule:";


    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    @Encrypt
    private Long ruleId;
    @ApiModelProperty(value = "规则编码")
    @NotBlank
    @Length(max = 30)
    @Pattern(regexp = Regexs.CODE_UPPER)
    private String ruleCode;
    @ApiModelProperty(value = "规则名称")
    @NotBlank
    @Length(max = 120)
    private String ruleName;
    @ApiModelProperty(value = "租户ID,hpfm_tenant.tenant_id")
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "规则类别编码，HPFM.CUSTOMIZE_RULE_TYPE")
    @NotBlank
    @LovValue("HPFM.CUSTOMIZE_RULE_TYPE")
    private String typeCode;
    @ApiModelProperty(value = "规则类别对应的值")
    @NotBlank
    private String typeValue;
    @ApiModelProperty(value = "规则位置，HPFM.CUSTOMIZE_RULE_POSITION")
    @NotBlank
    @LovValue("HPFM.CUSTOMIZE_RULE_POSITION")
    private String rulePosition;
    @ApiModelProperty(value = "是否同步调用 1：同步；0异步；默认1；")
    private Integer syncFlag;
    @ApiModelProperty(value = "是否启用。1启用，0未启用")
    private Integer enabledFlag;
    @ApiModelProperty(value = "描述")
    @Length(max = 480)
    private String description;

    //
    // 非数据库字段
    // ------------------------------------------------------------------------------


    @Transient
    private String tenantNum;
    @Transient
    private String tenantName;
    @Transient
    private String typeCodeMeaning;
    @Transient
    private String rulePositionMeaning;

    @Transient
    private String excludeRangeId;

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    /**
     * @return 表ID，主键，供其他表做外键
     */
    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    /**
     * @return 规则编码
     */
    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    /**
     * @return 规则名称
     */
    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
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
     * @return 规则类别编码，HPFM.CUSTOMIZE_RULE_TYPE
     */
    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * @return 规则类别对应的值
     */
    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    /**
     * @return 规则位置，HPFM.CUSTOMIZE_RULE_POSITION
     */
    public String getRulePosition() {
        return rulePosition;
    }

    public void setRulePosition(String rulePosition) {
        this.rulePosition = rulePosition;
    }

    /**
     * @return 是否同步调用 1：同步；0异步；默认1；
     */
    public Integer getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(Integer syncFlag) {
        this.syncFlag = syncFlag;
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

    public String getTypeCodeMeaning() {
        return typeCodeMeaning;
    }

    public void setTypeCodeMeaning(String typeCodeMeaning) {
        this.typeCodeMeaning = typeCodeMeaning;
    }

    public String getRulePositionMeaning() {
        return rulePositionMeaning;
    }

    public void setRulePositionMeaning(String rulePositionMeaning) {
        this.rulePositionMeaning = rulePositionMeaning;
    }

    public String getExcludeRangeId() {
        return excludeRangeId;
    }

    public void setExcludeRangeId(String excludeRangeId) {
        this.excludeRangeId = excludeRangeId;
    }
}
