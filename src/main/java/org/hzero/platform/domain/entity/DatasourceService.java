package org.hzero.platform.domain.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hzero.core.base.BaseConstants;
import org.hzero.platform.domain.repository.DatasourceServiceRepository;

import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.starter.keyencrypt.core.Encrypt;

/**
 * 服务数据源关系
 *
 * @author like.zhang@hand-china.com 2018-09-13 14:10:12
 */
@ApiModel("服务数据源关系")
@VersionAudit
@ModifyAudit
@Table(name = "hpfm_datasource_service")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatasourceService extends AuditDomain {

    public static final String FIELD_DATASOURCE_SERVICE_ID = "datasourceServiceId";
    public static final String FIELD_SERVICE_NAME = "serviceName";
    public static final String FIELD_DATASOURCE_ID = "datasourceId";


    public interface Insert{}

    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    /**
     * 验证数据源 服务名是否存在
     *
     * @param datasourceServiceRepository DatasourceServiceRepository
     */
    public void validate(DatasourceServiceRepository datasourceServiceRepository) {
        //校验数据源-服务 一对多
        if(CollectionUtils.isNotEmpty(datasourceServiceRepository.select(DatasourceService.FIELD_SERVICE_NAME,serviceName))){
            throw new CommonException(BaseConstants.ErrorCode.DATA_EXISTS);
        }
    }
    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("表ID，主键")
    @Id
    @GeneratedValue
    @Encrypt
    private Long datasourceServiceId;
    @ApiModelProperty(value = "服务名称", required = true)
    @NotBlank
    @Length(max = 128)
    private String serviceName;
    @ApiModelProperty(value = "数据源ID", required = true)
    @NotNull
    @Encrypt
    private Long datasourceId;
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

    public DatasourceService setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 表ID，主键
     */
    public Long getDatasourceServiceId() {
        return datasourceServiceId;
    }

    public void setDatasourceServiceId(Long datasourceServiceId) {
        this.datasourceServiceId = datasourceServiceId;
    }

    /**
     * @return 服务名称
     */
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return 数据源ID
     */
    public Long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }


}
