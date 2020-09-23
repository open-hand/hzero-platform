package org.hzero.platform.infra.repository.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.platform.domain.entity.CommonTemplate;
import org.hzero.platform.domain.repository.CommonTemplateRepository;
import org.hzero.platform.infra.mapper.CommonTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 通用模板 资源库实现
 *
 * @author xiaoyu.zhao@hand-china.com 2018-08-13 15:37:15
 */
@Component
public class CommonTemplateRepositoryImpl extends BaseRepositoryImpl<CommonTemplate> implements
                CommonTemplateRepository {

    @Autowired
    private CommonTemplateMapper templateMapper;

    @Override
    @ProcessLovValue
    public Page<CommonTemplate> selectTemplates(PageRequest pageRequest, CommonTemplate templates) {
        return PageHelper.doPageAndSort(pageRequest, () -> templateMapper.selectTemplates(templates));
    }
}
