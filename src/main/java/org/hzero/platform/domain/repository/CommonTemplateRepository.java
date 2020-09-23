package org.hzero.platform.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import org.hzero.platform.domain.entity.CommonTemplate;

/**
 * 通用模板资源库
 *
 * @author yunxiang.zhou01@hand-china.com 2018-08-13 15:37:15
 */
public interface CommonTemplateRepository extends BaseRepository<CommonTemplate> {

    /**
     * 查询模板
     *
     * @param pageRequest 分页
     * @param templates 模板
     * @return 模板list
     */
    Page<CommonTemplate> selectTemplates(PageRequest pageRequest, CommonTemplate templates);


}
