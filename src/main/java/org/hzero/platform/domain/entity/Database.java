package org.hzero.platform.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.core.util.Regexs;
import org.hzero.platform.api.dto.DatabaseTenantDTO;
import org.hzero.platform.domain.repository.DatabaseRepository;
import org.hzero.platform.domain.repository.DatabaseTenantRepository;
import org.hzero.platform.domain.repository.DatasourceRepository;
import org.hzero.platform.domain.repository.DatasourceServiceRepository;
import org.hzero.platform.infra.constant.FndConstants;
import org.hzero.starter.keyencrypt.core.Encrypt;

import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;

/**
 * 数据库
 *
 * @author like.zhang@hand-china.com 2018-09-07 10:11:10
 */
@ApiModel("数据库")
@VersionAudit
@ModifyAudit
@Table(name = "hpfm_database")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Database extends AuditDomain {

    public static final String FIELD_DATABASE_ID = "databaseId";
    public static final String FIELD_DATABASE_CODE = "databaseCode";
    public static final String FIELD_DATABASE_NAME = "databaseName";
    public static final String FIELD_DATASOURCE_ID = "datasourceId";
    public static final String FIELD_ENABLED_FLAG = "enabledFlag";
    public static final String FIELD_PUBLIC_FLAG = "publicFlag";
    public static final String FIELD_TABLE_PREFIX = "tablePrefix";



    public interface Insert{}


    //
    // 业务方法(按public protected private顺序排列)
    // ------------------------------------------------------------------------------

    /**
     * 缓存key为数据库编码，value为数据库对象<br/>
     * 如果为启用状态，查询出事件规则，并刷新到缓存中<br/>
     * 如果为禁用状态，清除该事件缓存
     *
     * @param databaseTenantRepository DatabaseRepository
     * @param redisHelper              RedisHelper
     */
    public void refreshCache(DatabaseTenantRepository databaseTenantRepository, RedisHelper redisHelper, DatasourceServiceRepository datasourceServiceRepository) {
        if (BaseConstants.Flag.YES.equals(enabledFlag)) {
            //获取databaseId 对应的tenantId列表
            List<Long> tenantIds = databaseTenantRepository.selectTenantIdsByDatabaseId(databaseId);

            if (CollectionUtils.isNotEmpty(tenantIds)) {
                for (Long id : tenantIds) {
                    if (id != null) {
                        List<String> keys = Database.generateKey(id, datasourceServiceRepository, datasourceId);
                        if (CollectionUtils.isNotEmpty(keys)) {
                            keys.forEach(key -> redisHelper.strSet(key, databaseCode));
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据tenantId列表删除缓存
     *
     * @param redisHelper RedisHelper
     * @param tenantIds   租户id列表
     */
    public void clearCacheByTenantIds(RedisHelper redisHelper, List<Long> tenantIds, Long datasourceId, DatasourceServiceRepository datasourceServiceRepository) {
        tenantIds.forEach(tenantId -> {
            List<String> serviceNames = datasourceServiceRepository.selectServiceNamesByDatasourceId(datasourceId);
            if (CollectionUtils.isNotEmpty(serviceNames)) {
                for (String serviceName : serviceNames) {
                    redisHelper.delKey(FndConstants.CacheKey.DATABASE_KEY + ":" + tenantId + "." + serviceName);
                }
            }
        });
    }

    /**
     * 清除databaseId对应缓存
     *
     * @param redisHelper RedisHelper
     */
    public void clearCacheAllByDatabaseId(RedisHelper redisHelper, DatabaseTenantRepository databaseTenantRepository, DatasourceServiceRepository datasourceServiceRepository) {
        List<DatabaseTenantDTO> databaseTenantList = databaseTenantRepository.selectByDatabaseId(this.databaseId);
        List<Long> tenants = databaseTenantList.stream().map(DatabaseTenantDTO::getTenantId).collect(Collectors.toList());
        this.clearCacheByTenantIds(redisHelper, tenants, this.datasourceId, datasourceServiceRepository);
    }

    /**
     * 产生缓存keys 仅缓存数据分发的数据
     *
     * @param tenantId                    租户id
     * @param datasourceServiceRepository DatasourceServiceRepository
     * @param datasourceId                数据源id
     * @return keys
     */
    public static List<String> generateKey(Long tenantId, DatasourceServiceRepository datasourceServiceRepository, Long datasourceId) {

        List<String> keys = new ArrayList<>();

        List<String> serviceNames = datasourceServiceRepository.selectServiceNamesByDatasourceId(datasourceId);
        if (CollectionUtils.isNotEmpty(serviceNames)) {
            for (String serviceName : serviceNames) {
                keys.add(FndConstants.CacheKey.DATABASE_KEY + ":" + tenantId + "." + serviceName);
            }
        }
        return keys;
    }

    /**
     * 比较两个数据库的数据源代码是否一致
     *
     * @param anotherDatabase 数据源
     * @throws CommonException 如果两个事件的编码不一致
     */
    public void equalsDatabaseCode(Database anotherDatabase) {
        if (StringUtils.isNotBlank(anotherDatabase.getDatabaseCode())
                && !StringUtils.equals(this.databaseCode, anotherDatabase.getDatabaseCode())) {
            throw new CommonException(BaseConstants.ErrorCode.DATA_INVALID);
        }
    }

    /**
     * 校验数据库
     *
     * @throws IllegalArgumentException 参数校验不通过
     */
    public void validate(DatabaseRepository databaseRepository, DatasourceRepository datasourceRepository) {
        if (CollectionUtils.isEmpty(datasourceRepository.select(Datasource.FIELD_DATASOURCE_ID, datasourceId))) {
            throw new CommonException(BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        }

        validatePrimaryIndex(databaseRepository);
    }

    /**
     * 验证唯一索引
     *
     * @param databaseRepository DatabaseRepository
     */
    public void validatePrimaryIndex(DatabaseRepository databaseRepository) {
        Database database = new Database();
        database.setDatabaseCode(databaseCode);
        database.setDatasourceId(datasourceId);
        if (databaseRepository.selectCount(database) != 0) {
            throw new CommonException(BaseConstants.ErrorCode.DATA_EXISTS);
        }
    }

    //
    // 数据库字段
    // ------------------------------------------------------------------------------


    @ApiModelProperty("数据库id")
    @Id
    @GeneratedValue
    @Encrypt
    private Long databaseId;
    @ApiModelProperty(value = "数据库代码", required = true)
    @NotBlank
    @Length(max = 150)
    @Pattern(regexp = Regexs.CODE)
    private String databaseCode;
    @ApiModelProperty(value = "数据库名称", required = true)
    @NotBlank
    @Length(max = 150)
    private String databaseName;
    @ApiModelProperty(value = "数据源id", required = true)
    @NotNull
    @Encrypt
    private Long datasourceId;
    @ApiModelProperty(value = "启用标识")
    private Integer enabledFlag;
    @NotNull
    @ApiModelProperty(value = "公共库标识")
    private Integer publicFlag;
    @NotBlank
    @ApiModelProperty(value = "表前缀")
    @Length(max = 30)
    private String tablePrefix;
    @NotNull(groups = Insert.class)
    private Long tenantId;
    //
    // 非数据库字段
    // ------------------------------------------------------------------------------
    @Transient
    @JsonIgnore
    @ApiModelProperty("层级标识，标识是saas还是op")
    private String levelFlag;

    //
    // getter/setter
    // ------------------------------------------------------------------------------

    public Long getTenantId() {
        return tenantId;
    }

    public Database setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public String getLevelFlag() {
        return levelFlag;
    }

    public void setLevelFlag(String levelFlag) {
        this.levelFlag = levelFlag;
    }

    /**
     * @return 表ID，主键，供其他表做外键
     */
    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    /**
     * @return 数据源代码
     */
    public String getDatabaseCode() {
        return databaseCode;
    }

    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    /**
     * @return 数据源名称
     */
    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * @return 数据源名称
     */
    public Long getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(Long datasourceId) {
        this.datasourceId = datasourceId;
    }

    /**
     * @return 启用标识
     */
    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Integer getPublicFlag() {
        return publicFlag;
    }

    public void setPublicFlag(Integer publicFlag) {
        this.publicFlag = publicFlag;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }
}
