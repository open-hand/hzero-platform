package org.hzero.platform.api.dto;

import org.hzero.mybatis.domian.SecurityToken;
import org.hzero.platform.domain.entity.DatabaseTenant;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;

/**
 * 数据权限数据源关系
 *
 * @author like.zhang@hand-china.com 2018/09/07 14:24
 */
@ApiModel("数据权限数据源关系DTO")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DatabaseTenantDTO extends DatabaseTenant {

    private String tenantNum;
    private String tenantName;

    private Long objectVersionNumber;

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return (Class<? extends SecurityToken>) this.getClass().getSuperclass();
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

    @Override
    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    @Override
    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    @Override
    public String toString() {
        return "DatabaseTenantDTO{" + "tenantNum='" + tenantNum + '\'' + ", tenantName='" + tenantName + '\''
                        + ", objectVersionNumber=" + objectVersionNumber + '}';
    }
}
