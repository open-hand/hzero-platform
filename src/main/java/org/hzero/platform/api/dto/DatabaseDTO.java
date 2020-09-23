package org.hzero.platform.api.dto;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

import org.hzero.core.util.Regexs;
import org.hzero.mybatis.domian.SecurityToken;
import org.hzero.platform.domain.entity.Database;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

/**
 * 数据库DTO
 *
 * @author like.zhang@hand-china.com 2018/09/07 11:45
 */
@ApiModel("数据库DTO")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DatabaseDTO extends Database {

    @ApiModelProperty("数据库id")
    @Id
    @GeneratedValue
    @Encrypt
    private Long databaseId;
    @ApiModelProperty(value = "数据库代码")
    private String databaseCode;
    @ApiModelProperty(value = "数据库名称")
    private String databaseName;
    @ApiModelProperty(value = "数据源id")
    @Encrypt
    private Long datasourceId;
    @Pattern(regexp = Regexs.CODE)
    @ApiModelProperty(value = "数据源代码")
    private String datasourceCode;
    @ApiModelProperty(value = "启用标识")
    private Integer enabledFlag;
    private Long objectVersionNumber;
    private String description;
    private Integer publicFlag;
    private String tablePrefix;

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return (Class<? extends SecurityToken>) this.getClass().getSuperclass();
    }

    @Override
    public Long getDatabaseId() {
        return databaseId;
    }

    @Override
    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    @Override
    public String getDatabaseCode() {
        return databaseCode;
    }

    @Override
    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public Long getDatasourceId() {
        return datasourceId;
    }

    @Override
    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }

    public String getDatasourceCode() {
        return datasourceCode;
    }

    public void setDatasourceCode(String datasourceCode) {
        this.datasourceCode = datasourceCode;
    }

    @Override
    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    @Override
    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    @Override
    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    @Override
    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Integer getPublicFlag() {
        return publicFlag;
    }

    @Override
    public void setPublicFlag(Integer publicFlag) {
        this.publicFlag = publicFlag;
    }

    @Override
    public String getTablePrefix() {
        return tablePrefix;
    }

    @Override
    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DatabaseDTO [databaseId=");
        builder.append(databaseId);
        builder.append(", databaseCode=");
        builder.append(databaseCode);
        builder.append(",databaseName=");
        builder.append(databaseName);
        builder.append(", datasourceCode=");
        builder.append(datasourceCode);
        builder.append(", datasourceId=");
        builder.append(datasourceId);
        builder.append(", enabledFlag=");
        builder.append(enabledFlag);
        builder.append(", objectVersionNumber=");
        builder.append(objectVersionNumber);
        builder.append("]");
        return builder.toString();
    }

}
