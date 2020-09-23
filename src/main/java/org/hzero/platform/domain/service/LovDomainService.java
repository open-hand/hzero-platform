package org.hzero.platform.domain.service;

import org.hzero.platform.domain.entity.Lov;

import java.util.List;
import java.util.Map;

/**
 * 值集逻辑统一处理Service
 *
 * @author xiaoyu.zhao@hand-china.com 2019/05/21 10:00
 */
public interface LovDomainService {

    /**
     * 查询lov基本信息
     *
     * @param lovCode  值集代码
     * @param tenantId 租户ID
     * @return lov基本信息, 无权访问时返回null
     */
    default Lov queryLovInfo(String lovCode, Long tenantId) {
        return queryLovInfo(lovCode, tenantId, false);
    }

    /**
     * 查询lov基本信息
     *
     * @param lovCode  值集代码
     * @param tenantId 租户ID
     * @return lov基本信息, 无权访问时返回null
     */
    Lov queryLovInfo(String lovCode, Long tenantId, boolean onlyPublic);

    /**
     * 集成查询值集数据
     *
     * @param lovCode  值集代码
     * @param tenantId 租户ID
     * @param page     页码
     * @param size     页面大小
     * @param params   查询参数
     * @return 值集数据List, 出现异常时返回空List
     */
    default List<Map<String, Object>> queryLovData(String lovCode, Long tenantId, String tag, Integer page, Integer size, Map<String, String> params) {
        return queryLovData(lovCode, tenantId, tag, page, size, params, false);
    }

    /**
     * 集成查询值集数据
     *
     * @param lovCode  值集代码
     * @param tenantId 租户ID
     * @param page     页码
     * @param size     页面大小
     * @param params   查询参数
     * @return 值集数据List, 出现异常时返回空List
     */
    List<Map<String, Object>> queryLovData(String lovCode, Long tenantId, String tag, Integer page, Integer size, Map<String, String> params, boolean onlyPublic);

    /**
     * 根据lovCode查询SQL
     *
     * @param lovCode  值集代码
     * @param tenantId 租户ID
     * @return 查询结果, 如果无权访问返回null
     */
    default String queryLovSql(String lovCode, Long tenantId) {
        return queryLovSql(lovCode, tenantId, false);
    }

    /**
     * 根据lovCode查询SQL
     *
     * @param lovCode  值集代码
     * @param tenantId 租户ID
     * @return 查询结果, 如果无权访问返回null
     */
    String queryLovSql(String lovCode, Long tenantId, boolean onlyPublic);

    /**
     * 根据lovCode查询SQL
     *
     * @param lovCode  值集代码
     * @param tenantId 租户ID
     * @return 查询结果, 如果无权访问返回null
     */
    default String queryLovTranslationSql(String lovCode, Long tenantId) {
        return queryLovTranslationSql(lovCode, tenantId, false);
    }

    /**
     * 根据lovCode查询SQL
     *
     * @param lovCode  值集代码
     * @param tenantId 租户ID
     * @return 查询结果, 如果无权访问返回null
     */
    String queryLovTranslationSql(String lovCode, Long tenantId, boolean onlyPublic);

    /**
     * 插入Lov
     *
     * @param lov 待插入的Lov
     * @return 插入后的Lov
     */
    Lov addLov(Lov lov);

    /**
     * 更新Lov
     *
     * @param lov 待更新的Lov
     * @return 更新后的Lov
     */
    Lov updateLov(Lov lov);

    /**
     * 复制值集
     *
     * @param tenantId 租户Id
     * @param lovCode  值集编码
     * @param lovId    值集Id
     * @param siteFlag 是否是平台级查询
     */
    void copyLov(Long tenantId, String lovCode, Long lovId, Integer siteFlag);
}
