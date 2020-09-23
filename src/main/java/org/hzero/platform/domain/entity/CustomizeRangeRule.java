package org.hzero.platform.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

/**
 * API个性化范围规则关系
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:45:50
 */
@ApiModel("API个性化范围规则关系")
@VersionAudit
@ModifyAudit
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Table(name = "hpfm_customize_range_rule")
public class CustomizeRangeRule extends AuditDomain {


    public interface Insert{}

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    @GeneratedValue
    @Encrypt
    private Long rangeRuleId;
    @ApiModelProperty(value = "范围ID，hpfm_customize_range.range_id", required = true)
    @NotNull
    private Long rangeId;
    @ApiModelProperty(value = "规则ID，hpfm_customize_rule.rule_id", required = true)
    @NotNull
    private Long ruleId;
    @NotNull(groups = Insert.class)
    private Long tenantId;


    @ApiModelProperty(value = "规则编码", required = true)
    @Transient
    private String ruleCode;
    @ApiModelProperty(value = "规则名称", required = true)
    @Transient
    private String ruleName;
    @ApiModelProperty(value = "是否启用。1启用，0未启用")
    @Transient
    private Integer enabledFlag;
    @ApiModelProperty(value = "描述")
    @Transient
    private String description;

    @Transient
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private CustomizeRule customizeRule;

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    public Long getTenantId() {
        return tenantId;
    }

    public CustomizeRangeRule setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public Long getRangeRuleId() {
        return rangeRuleId;
    }

    public void setRangeRuleId(Long rangeRuleId) {
        this.rangeRuleId = rangeRuleId;
    }

    public Long getRangeId() {
        return rangeId;
    }

    public void setRangeId(Long rangeId) {
        this.rangeId = rangeId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CustomizeRule getCustomizeRule() {
        return customizeRule;
    }

    public void setCustomizeRule(CustomizeRule customizeRule) {
        this.customizeRule = customizeRule;
    }
}
