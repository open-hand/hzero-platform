package org.hzero.platform.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;
import io.choerodon.mybatis.domain.AuditDomain;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hzero.common.HZeroConstant;
import org.hzero.core.redis.RedisHelper;
import org.hzero.core.util.FileUtils;
import org.hzero.core.util.Regexs;
import org.hzero.platform.infra.constant.Constants;
import org.hzero.platform.infra.constant.FndConstants;
import org.hzero.starter.keyencrypt.core.Encrypt;

/**
 * <p>
 * 系统配置
 * </p>
 *
 * @author yunxiang.zhou01@hand-china.com 2018/06/19 11:32
 */
@VersionAudit
@ModifyAudit
@Table(name = "hpfm_config")
@ApiModel("系统配置")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Config extends AuditDomain {

    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_CONFIG_VALUE = "configValue";


    /**
     * 刷新缓存
     *
     * @param redisHelper redis
     */
    public void refreshCache(RedisHelper redisHelper){
        String key = Config.generateCacheKey(configCode, tenantId);
        redisHelper.strSet(key, configValue);
    }

    /**
     * 注入租户id
     *
     * @param configList 系统配置list
     * @param tenantId 租户id
     */
    public static void injectTenantId(List<Config> configList, Long tenantId) {
        if (CollectionUtils.isNotEmpty(configList)) {
            configList.forEach(config -> config.setTenantId(tenantId));
        }
    }

    /**
     * 生成系统配置的redis缓存key
     *
     * @param configCode 系统配置code
     * @return key
     */
    public static String generateCacheKey(String configCode, Long tenantId) {
        StringBuilder sb = new StringBuilder();
        return sb.append(FndConstants.CacheKey.CONFIG_KEY).append(":").append(configCode).append(".").append(tenantId)
                        .toString();
    }

    // ===============================================================================
    // getter setter
    // ===============================================================================

    @Id
    @GeneratedValue
    @ApiModelProperty("系统配置ID")
    @Encrypt
    private Long configId;
    @NotBlank
    @Pattern(regexp = Regexs.CODE_UPPER)
    @ApiModelProperty("系统配置CODE")
    @Length(max = 30)
    private String configCode;
    @ApiModelProperty("系统配置值")
    @Length(max = 240)
    private String configValue;
    @ApiModelProperty("租户ID")
    private Long tenantId;
    @ApiModelProperty("分类")
    @Length(max = 30)
    private String category;
    @ApiModelProperty("版本号")
    private Long objectVersionNumber;
    @Transient
    private String fileName;

    /**
     * @return 主键id
     */
    public Long getConfigId() {
        return configId;
    }

    /**
     * @return 配置code
     */
    public String getConfigCode() {
        return configCode;
    }

    /**
     * @return 配置值
     */
    public String getConfigValue() {
        return configValue;
    }

    /**
     * @return 配置类别
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return 租户id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    @Override
    @JsonIgnore
    public Date getCreationDate() {
        return super.getCreationDate();
    }

    @Override
    @JsonIgnore
    public Long getCreatedBy() {
        return super.getCreatedBy();
    }

    @Override
    @JsonIgnore
    public Date getLastUpdateDate() {
        return super.getLastUpdateDate();
    }

    @Override
    @JsonIgnore
    public Long getLastUpdatedBy() {
        return super.getLastUpdatedBy();
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getFileName() {
        if(HZeroConstant.Config.CONFIG_CODE_LOGO.equals(configCode)
                || Constants.CONFIG_CODE_FAVICON.equals(configCode)){
            return FileUtils.getFileName(configValue);
        }
        return null;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
