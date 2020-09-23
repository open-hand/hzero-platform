package org.hzero.platform.app.service;

import org.hzero.platform.domain.entity.CommonTemplate;

/**
 * 通用模板管理服务
 *
 * @author xiaoyu.zhao@hand-china.com 2019/06/25 15:44
 */
public interface CommonTemplateService {

    /**
     * 新建模板
     *
     * @param templates 门户模板
     * @return 门户模板
     */
    CommonTemplate insertTemplates(CommonTemplate templates);

    /**
     * 更新模板
     * 
     * @param templates 更新参数
     * @return 更新结果
     */
    CommonTemplate updateTemplate(CommonTemplate templates);

    /**
     * 删除模板信息
     *
     * @param templates 删除参数
     */
    void removeTemplate(CommonTemplate templates);

}
