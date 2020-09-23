package org.hzero.platform.app.service.impl;

import org.hzero.core.base.BaseConstants;
import org.hzero.platform.app.service.CommonTemplateService;
import org.hzero.platform.domain.entity.CommonTemplate;
import org.hzero.platform.domain.repository.CommonTemplateRepository;
import org.hzero.platform.infra.mapper.CommonTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import io.choerodon.core.exception.CommonException;

/**
 * 通用模板管理服务实现类
 *
 * @author xiaoyu.zhao@hand-china.com 2019/06/25 15:45
 */
@Service
public class CommonTemplateServiceImpl implements CommonTemplateService {

    @Autowired
    private CommonTemplateRepository templateRepository;
    @Autowired
    private CommonTemplateMapper templateMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonTemplate insertTemplates(CommonTemplate templates) {
        int count = templateMapper.selectTemplateByTenantAndCode(templates.getTemplateCode(), templates.getTenantId());
        if (count != 0) {
            // 当前保存的数据在数据库中已经存在
            throw new CommonException(BaseConstants.ErrorCode.DATA_EXISTS);
        }
        templateRepository.insertSelective(templates);
        return templates;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonTemplate updateTemplate(CommonTemplate templates) {
        // 更新参数
        templateRepository.updateOptional(templates, CommonTemplate.FIELD_ENABLED_FLAG,
                        CommonTemplate.FIELD_TEMPLATE_NAME, CommonTemplate.FIELD_TEMPLATE_AVATAR,
                        CommonTemplate.FIELD_TEMPLATE_PATH, CommonTemplate.FIELD_TEMPLATE_LEVEL_CODE);
        return templates;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeTemplate(CommonTemplate templates) {
        Assert.notNull(templates.getTemplateId(), BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        templateRepository.deleteByPrimaryKey(templates.getTemplateId());
    }

}
