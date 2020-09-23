package org.hzero.platform.domain.entity;

import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.platform.domain.repository.DatabaseTenantRepository;
import org.hzero.platform.infra.constant.FndConstants;
import org.hzero.starter.keyencrypt.core.Encrypt;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 数据权限数据源关系
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
@ApiModel("数据权限数据源关系")
@VersionAudit
@ModifyAudit
@Table(name = "hpfm_database_tenant")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatabaseTenant extends AuditDomain {

    public static final String FIELD_DATABASE_TENANT_ID = "databaseTenantId";
    public static final String FIELD_DATABASE_ID = "databaseId";
    public static final String FIELD_TENANT_ID = "tenantId";
    public static final String FIELD_DATA_SOURCE_ID = "datasourceId";


    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    /**
     * 比较两个关系是否一致
     *
     * @param anotherDatabaseTenant 数据权限数据源关系
     * @throws CommonException 已存在此关系
     */
    public void equalsDatabaseIdAndTenantId(DatabaseTenant anotherDatabaseTenant) {
        if (this.databaseId.equals(anotherDatabaseTenant.getDatabaseId())
                && this.tenantId.equals(anotherDatabaseTenant.getTenantId())) {
            throw new CommonException(BaseConstants.ErrorCode.DATA_EXISTS);
        }
    }

    /**
     * 校验数参数
     *
     * @throws IllegalArgumentException 参数校验不通过
     */
    public void validate(DatabaseTenantRepository databaseTenantRepository) {
        // 根据租户id查询数据权限数据库关系，一个租户在一个数据库下只能对应一个数据源
        DatabaseTenant databaseTenant = new DatabaseTenant();
        databaseTenant.setTenantId(tenantId);
        databaseTenant.setDatabaseId(databaseId);
        int count = databaseTenantRepository.selectCount(databaseTenant);
        if (count != 0) {
            throw new CommonException(BaseConstants.ErrorCode.DATA_EXISTS);
        }
    }

    /**
     * 清除tenantId对应缓存
     *
     * @param redisHelper RedisHelper
     */
    public void clearCache(RedisHelper redisHelper, Long tenantId, List<String> sourceNames) {
        for (String sourceName : sourceNames) {
            redisHelper.delKey(FndConstants.CacheKey.DATABASE_KEY + ":" + tenantId + ":" + sourceName);
        }
    }

    public DatabaseTenant(Long databaseId, Long tenantId, Long datasourceId) {
        this.databaseId = databaseId;
        this.tenantId = tenantId;
        this.datasourceId = datasourceId;
    }

    public DatabaseTenant() {
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("关系id")
    @Id
    @GeneratedValue
    @Encrypt
    private Long databaseTenantId;
    @ApiModelProperty(value = "数据库id", required = true)
    @NotNull
    @Encrypt
    private Long databaseId;
    @ApiModelProperty(value = "租户id", required = true)
    @NotNull
    private Long tenantId;
    @ApiModelProperty(value = "数据源id", required = true)
    @NotNull
    @Encrypt
    private Long datasourceId;


    //
    // 非数据库字段
    // ------------------------------------------------------------------------------

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    public Long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }

    /**
     * @return 表ID，主键，供其他表做外键
     */
    public Long getDatabaseTenantId() {
        return databaseTenantId;
    }

    public void setDatabaseTenantId(Long databaseTenantId) {
        this.databaseTenantId = databaseTenantId;
    }

    /**
     * @return 外键, hpfm_database.database_id
     */
    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    /**
     * @return 外键, hpfm_tenant.tenant_id
     */
    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

}
