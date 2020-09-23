package org.hzero.platform.domain.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.helper.LanguageHelper;

import org.hzero.common.HZeroCacheKey;
import org.hzero.common.HZeroConstant;
import org.hzero.common.HZeroService;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.mybatis.common.Criteria;
import org.hzero.platform.api.dto.LovValueDTO;
import org.hzero.platform.app.service.LovValueService;
import org.hzero.platform.app.service.impl.LovServiceImpl;
import org.hzero.platform.domain.entity.Lov;
import org.hzero.platform.domain.entity.LovValue;
import org.hzero.platform.domain.repository.LovRepository;
import org.hzero.platform.domain.repository.LovValueRepository;
import org.hzero.platform.domain.service.LovDomainService;
import org.hzero.platform.infra.constant.FndConstants;
import org.hzero.platform.infra.constant.HpfmMsgCodeConstants;
import org.hzero.platform.infra.properties.PlatformProperties;
import org.hzero.platform.infra.util.JsonUtils;

/**
 * 值集逻辑统一处理Service实现类
 *
 * @author xiaoyu.zhao@hand-china.com 2019/05/21 10:01
 */
@Component
public class LovDomainServiceImpl implements LovDomainService {

    @Autowired
    private LovRepository lovRepository;
    @Autowired
    private LovValueRepository lovValueRepository;
    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LovValueService lovValueService;
    @Autowired
    private PlatformProperties platformProperties;
    @Autowired
    private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(LovServiceImpl.class);

    @Override
    public Lov queryLovInfo(String lovCode, Long tenantId, boolean onlyPublic) {
        Lov bestMatch;
        List<Lov> cacheResults;
        if (tenantId == null) {
            tenantId = BaseConstants.DEFAULT_TENANT_ID;
        }
        String lang = this.getCurrentLanguage();
        // 先从缓存中查询
        cacheResults = this.lovRepository.queryLovFromCacheByTenant(lovCode, tenantId, lang);
        bestMatch = cacheResults.get(cacheResults.size() - 1);
        cacheResults.remove(bestMatch);

        Assert.notNull(bestMatch, BaseConstants.ErrorCode.NOT_NULL);
        if (bestMatch.getAccessStatus() == Lov.LovAccessStatus.FORBIDDEN) {
            // 该租户ID存在于租户局黑名单中,拒绝访问
            this.logger.debug("lov illegal access: code [{}] tenantId [{}]", lovCode, tenantId);
        } else if (bestMatch.getAccessStatus() == Lov.LovAccessStatus.NOT_FOUND) {
            // 如果租户级缓存没有命中且没有触发黑名单,则根据租户ID去数据库中查询一次
            bestMatch = this.queryLovInfoFromDb(lovCode, tenantId, lang);
            Assert.notNull(bestMatch, BaseConstants.ErrorCode.NOT_NULL);
        }
        // 仅允许访问公共值集标记开启时，校验值集状态
        if (onlyPublic && !BaseConstants.Flag.YES.equals(bestMatch.getPublicFlag())) {
            bestMatch = new Lov().setAccessStatus(Lov.LovAccessStatus.FORBIDDEN);
        }
        // 租户级缓存未命中且传入租户ID不为0时,从全局级缓存中查询
        if (bestMatch.getAccessStatus() != Lov.LovAccessStatus.ACCESS && cacheResults.size() > 0) {
            bestMatch = cacheResults.get(0);
            Assert.notNull(bestMatch, BaseConstants.ErrorCode.NOT_NULL);
            if (bestMatch.getAccessStatus() == Lov.LovAccessStatus.FORBIDDEN) {
                // 该租户ID存在于全局级黑名单中,拒绝访问
                this.logger.warn("lov illegal access: code [{}] tenantId [{}]", lovCode, tenantId);
            } else if (bestMatch.getAccessStatus() == Lov.LovAccessStatus.NOT_FOUND) {
                // 全局级缓存也未命中,访问数据库查询全局数据
                bestMatch = this.queryLovInfoFromDb(lovCode, BaseConstants.DEFAULT_TENANT_ID, lang);
                Assert.notNull(bestMatch, BaseConstants.ErrorCode.NOT_NULL);
            }
        }
        return bestMatch.getAccessStatus() == Lov.LovAccessStatus.ACCESS ? bestMatch : null;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Map<String, Object>> queryLovData(String lovCode, Long tenantId, String tag, Integer page, Integer size, Map<String, String> params, boolean onlyPublic) {
        if (StringUtils.isEmpty(lovCode)) {
            return Collections.emptyList();
        }
        // 获取值集信息
        Lov lov = this.queryLovInfo(lovCode, tenantId, onlyPublic);
        if (lov == null) {
            return Collections.emptyList();
        }
        // 根据值集信息获取不同的处理方式
        if (Objects.equals(lov.getLovTypeCode(), FndConstants.LovTypeCode.INDEPENDENT)) {
            // 独立值集,直接利用先用的方法
            if (params.containsKey(LovValue.FIELD_TENANT_ID)) {
                tenantId = Long.parseLong(params.remove(LovValue.FIELD_TENANT_ID));
            }
            List<LovValueDTO> lovValues = this.lovValueService.queryLovValue(lovCode, tenantId, tag, lov);
            if (CollectionUtils.isEmpty(lovValues)) {
                return Collections.emptyList();
            }
            return lovValues.stream().map(LovValueDTO::toMap).collect(Collectors.toList());
        } else {
            // 非独立值集,读取url信息,利用RestTemplate进行查询
            if (params == null) {
                params = new HashMap<>(16);
            }
            // 补充一个organizationId参数
            if (!params.containsKey("organizationId")) {
                params.put("organizationId", String.valueOf(tenantId));
            }
            // 处理分页
            this.processPageInfo(params, page, size, Objects.equals(lov.getMustPageFlag(), BaseConstants.Flag.YES));
            String trueUrl = lov.convertTrueUrl();
            String json = this.restTemplate.getForObject(this.platformProperties.getFullHttpProtocol() + HZeroService.getRealName(HZeroService.Gateway.NAME) + this.preProcessUrlParam(trueUrl, params), String.class, params);

            if (StringUtils.isBlank(json)) {
                return Collections.emptyList();
            }
            JsonNode jsonNode = JsonUtils.checkResultStatus(json, this.objectMapper);
            if (jsonNode == null) {
                return Collections.emptyList();
            }

            List<Map<String, Object>> resultMaps;
            if (jsonNode.isArray()) {
                List<Map> tempList = JsonUtils.checkAndParseList(jsonNode, this.objectMapper, Map.class);
                resultMaps = new ArrayList<>(tempList.size());
                for (Map map : tempList) {
                    resultMaps.add(map);
                }
            } else {
                resultMaps = JsonUtils.checkAndParseObject(jsonNode, this.objectMapper, Page.class);
            }
            return this.processValueAndMeaning(resultMaps, lov);
        }
    }

    @Override
    public String queryLovSql(String lovCode, Long tenantId, boolean onlyPublic) {
        if (StringUtils.isEmpty(lovCode)) {
            return null;
        }
        if (tenantId == null) {
            tenantId = BaseConstants.DEFAULT_TENANT_ID;
        }
        Lov lov = this.queryLovInfo(lovCode, tenantId, onlyPublic);
        if (lov == null || !Objects.equals(lov.getLovTypeCode(), FndConstants.LovTypeCode.SQL)) {
            return null;
        }
        // 通过权限检查和类型检查
        String sql = this.redisHelper.hshGet(HZeroCacheKey.Lov.SQL_KEY_PREFIX + lovCode, lovRepository.hashKey(lov.getTenantId()));
        if (StringUtils.isEmpty(sql)) {
            return null;
        }
        return sql;
    }

    @Override
    public String queryLovTranslationSql(String lovCode, Long tenantId, boolean onlyPublic) {
        if (StringUtils.isEmpty(lovCode)) {
            return null;
        }
        Lov lov = this.queryLovInfo(lovCode, tenantId == null ? BaseConstants.DEFAULT_TENANT_ID : tenantId, onlyPublic);
        if (lov == null || Objects.equals(lov.getLovTypeCode(), FndConstants.LovTypeCode.INDEPENDENT)) {
            return null;
        }
        // 通过权限检查和类型检查
        String sql = this.redisHelper.hshGet(HZeroCacheKey.Lov.TRANSLATION_SQL_KEY_PREFIX + lovCode, lovRepository.hashKey(lov.getTenantId()));
        if (StringUtils.isEmpty(sql)) {
            return null;
        }
        return sql;
    }

    @Override
    public Lov addLov(Lov lov) {
        if (lov == null) {
            return null;
        }
        lov.validate(this.lovRepository);
        if (FndConstants.LovTypeCode.INDEPENDENT.equals(lov.getLovTypeCode())
                && StringUtils.isNotEmpty(lov.getParentLovCode()) && lov.getParentTenantId() != null) {
            // 独立值集复制的时候，若存在父值集则将父值集的租户Id替换为当前租户Id
            lov.setParentTenantId(lov.getTenantId());
        }
        this.lovRepository.insertSelective(lov);
        LanguageHelper.languages().forEach(language ->
                this.lovRepository.cleanCache(lov.getLovCode(), lov.getTenantId(), language.getCode())
        );
        return lov;
    }

    @Override
    public Lov updateLov(Lov lov) {
        if (lov == null) {
            return null;
        }
        lov.validate(this.lovRepository);
        this.lovRepository.updateLov(lov);
        this.lovValueRepository.updateLovValueByHeaderInfo(lov);
        return lov;
    }

    @Override
    public void copyLov(Long tenantId, String lovCode, Long lovId, Integer siteFlag) {
        Lov dbLov = lovRepository.selectByPrimaryKey(lovId);
        // 校验
        Assert.notNull(dbLov, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        if (siteFlag.equals(BaseConstants.Flag.NO)) {
            // 租户级，仅可以复制预定义的，判断当前租户Id与要复制的值集的租户Id是否相同，相同则报错
            Assert.isTrue(!dbLov.getTenantId().equals(tenantId), HpfmMsgCodeConstants.ERROR_LOV_TENANT_REPEAT);
        } else {
            // 平台级，可以将自己的值集复制给指定租户，判断复制的值集是否是当前租户的，并且传入的租户也与数据库中不同
            if (!dbLov.getTenantId().equals(DetailsHelper.getUserDetails().getTenantId()) ||
                    dbLov.getTenantId().equals(tenantId)) {
                throw new CommonException(HpfmMsgCodeConstants.ERROR_LOV_SITE_COPY_FAIL);
            }
        }
        dbLov.setTenantId(tenantId);
        dbLov.setLovId(null);
        this.copyDuplicateLovWithDifferentTenant(dbLov, lovId, tenantId);
    }


    //-----------------------------------------------私有方法-------------------------------------------------------

    /**
     * 复制值集，关联复制值集值，值集多语言
     */
    private void copyDuplicateLovWithDifferentTenant(Lov dbLov, Long lovId, Long tenantId) {
        // 校验并复制值集
        Lov resultLov = this.addLov(dbLov);
        // 成功复制值集头之后复制值集值
        LovValue lovValue = new LovValue();
        lovValue.setLovId(lovId);
        List<LovValue> lovValues = lovValueRepository.selectOptional(lovValue,
                new Criteria().where(LovValue.FIELD_LOV_ID));
        if (CollectionUtils.isNotEmpty(lovValues)) {
            lovValues.forEach(lv -> {
                lv.setLovValueId(null);
                lv.setLovId(resultLov.getLovId());
                lv.setTenantId(tenantId);
                lovValueService.addLovValue(lv);
            });
        }
    }

    /**
     * 获取当前上下文语言<br>
     * <i>默认{@link org.hzero.core.base.BaseConstants#DEFAULT_LOCALE_STR}</i>
     *
     * @return 当前上下文语言
     */
    private String getCurrentLanguage() {
        CustomUserDetails user = DetailsHelper.getUserDetails();
        String lang = BaseConstants.DEFAULT_LOCALE_STR;
        if (user != null && user.getLanguage() != null) {
            lang = user.getLanguage();
        }
        return lang;
    }

    /**
     * 从数据库中加载Lov
     *
     * @param lovCode  值集代码
     * @param tenantId 租户ID
     * @param lang     语言
     * @return Lov 带可访问性标志的lov
     */
    private Lov queryLovInfoFromDb(String lovCode, Long tenantId, String lang) {
        if (tenantId == null) {
            tenantId = BaseConstants.DEFAULT_TENANT_ID;
        }
        Lov lov = this.lovRepository.selectByPrimaryKeyOrCode(new Lov(null, lovCode, tenantId));
        if (lov == null) {
            // 数据库中未命中,设置防击穿缓存
            this.redisHelper.hshPut(HZeroCacheKey.Lov.HEADER_KEY_PREFIX + lovCode,
                    lovRepository.hashKey(tenantId, lang), Lov.LovAccessStatus.FORBIDDEN.name());
            return new Lov(Lov.LovAccessStatus.NOT_FOUND);
        }
        // 数据库数据命中,检查权限
        // 有权访问,组织缓存
        // 去掉大段的文本单独缓存,提高效率
        Lov vo = lov.convertToCacheVo();
        this.redisHelper.hshPut(HZeroCacheKey.Lov.HEADER_KEY_PREFIX + lovCode,
                lovRepository.hashKey(lov.getTenantId(), lang), this.redisHelper.toJson(vo));
        this.redisHelper.setExpire(HZeroCacheKey.Lov.HEADER_KEY_PREFIX + lovCode, HZeroConstant.Lov.Cache.EXPIRE);
        // 值集sql
        if (StringUtils.isNotEmpty(lov.getCustomSql())) {
            this.redisHelper.hshPut(HZeroCacheKey.Lov.SQL_KEY_PREFIX + lovCode,
                    lovRepository.hashKey(lov.getTenantId()), lov.getCustomSql());
            this.redisHelper.setExpire(HZeroCacheKey.Lov.SQL_KEY_PREFIX + lovCode, HZeroConstant.Lov.Cache.EXPIRE);
        }
        // 值集翻译sql
        if (StringUtils.isNotEmpty(lov.getTranslationSql())) {
            this.redisHelper.hshPut(HZeroCacheKey.Lov.TRANSLATION_SQL_KEY_PREFIX + lovCode,
                    lovRepository.hashKey(lov.getTenantId()), lov.getTranslationSql());
            this.redisHelper.setExpire(HZeroCacheKey.Lov.TRANSLATION_SQL_KEY_PREFIX + lovCode, HZeroConstant.Lov.Cache.EXPIRE);
        }
        vo.setAccessStatus(Lov.LovAccessStatus.ACCESS);
        return vo;
    }

    /**
     * 处理分页信息
     *
     * @param params     参数Map
     * @param page       page
     * @param size       size
     * @param isMustPage 是否必须分页
     */
    private void processPageInfo(Map<String, String> params, Integer page, Integer size, boolean isMustPage) {
        if (page == null) {
            // 未传page
            params.put(BaseConstants.PAGE_FIELD_NAME, HZeroConstant.Lov.LOV_DATA_DEFAULT_PAGE);
        } else {
            // 传了page
            params.put(BaseConstants.PAGE_FIELD_NAME, String.valueOf(page));
        }
        if (size == null) {
            // 未传size
            if (isMustPage) {
                // 必须分页
                params.put(BaseConstants.SIZE_FIELD_NAME, HZeroConstant.Lov.LOV_DATA_DEFAULT_SIZE);
            } else {
                // 非必须分页
                params.put(BaseConstants.SIZE_FIELD_NAME, HZeroConstant.Lov.LOV_DATA_MAX_SIZE);
            }
        } else {
            // 传了size
            params.put(BaseConstants.SIZE_FIELD_NAME, String.valueOf(Integer.min(size, Integer.parseInt(HZeroConstant.Lov.LOV_DATA_MAX_SIZE))));
        }
    }

    /**
     * 处理返回值,渲染Value和Meaning
     *
     * @param resultMaps 结果集
     * @param lov        值集配置
     * @return 处理后的结果集
     */
    private List<Map<String, Object>> processValueAndMeaning(List<Map<String, Object>> resultMaps, final Lov lov) {
        if (CollectionUtils.isEmpty(resultMaps)) {
            return Collections.emptyList();
        }
        final String valueField = lov.getValueField();
        final String displayField = lov.getDisplayField();
        final boolean valueFieldEmptyFlag = StringUtils.isBlank(valueField);
        final boolean displayFieldEmptyFlag = StringUtils.isBlank(displayField);
        if (valueFieldEmptyFlag && displayFieldEmptyFlag) {
            return resultMaps;
        }
        return resultMaps.parallelStream().peek(map -> {
            if (!valueFieldEmptyFlag && map.get(valueField) != null) {
                map.put(Lov.FIELD_VALUE, map.get(valueField));
            }
            if (!displayFieldEmptyFlag || map.get(displayField) != null) {
                map.put(Lov.FIELD_MEANING, map.get(displayField));
            }
        }).collect(Collectors.toList());
    }

    /**
     * 预处理url,将参数的占位符拼接好
     *
     * @param url    源url
     * @param params 参数
     * @return 处理好的url
     */
    private String preProcessUrlParam(String url, Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder(url);
        Set<String> keySet = params.keySet();
        if (CollectionUtils.isNotEmpty(keySet)) {
            boolean firstKey = !url.contains("?");
            for (String key : keySet) {
                if (firstKey) {
                    stringBuilder.append('?').append(key).append("={").append(key).append('}');
                    firstKey = false;
                } else {
                    stringBuilder.append('&').append(key).append("={").append(key).append('}');
                }
            }
        }
        return stringBuilder.toString();
    }
}
