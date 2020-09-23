package org.hzero.platform.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.hzero.platform.domain.entity.CommonTemplate;

import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 通用模板Mapper
 *
 * @author yunxiang.zhou01@hand-china.com 2018-08-13 15:37:15
 */
public interface CommonTemplateMapper extends BaseMapper<CommonTemplate> {

    /**
     * 查询模板
     *
     * @param templates 模板条件
     * @return 模板list
     */
    List<CommonTemplate> selectTemplates(CommonTemplate templates);

    /**
     * 使用租户Id和模板编码查询唯一模板
     *
     * @param templateCode 模板编码
     * @param tenantId 租户Id
     * @return 唯一模板
     */
    int selectTemplateByTenantAndCode(@Param("templateCode") String templateCode, @Param("tenantId") Long tenantId);
}
