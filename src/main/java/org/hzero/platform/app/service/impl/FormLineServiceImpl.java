package org.hzero.platform.app.service.impl;

import java.util.Collections;
import java.util.List;

import org.hzero.boot.platform.form.domain.repository.BaseFormLineRepository;
import org.hzero.boot.platform.form.domain.vo.FormLineVO;
import org.hzero.core.base.BaseConstants;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.hzero.platform.app.service.FormLineService;
import org.hzero.platform.domain.entity.FormHeader;
import org.hzero.platform.domain.entity.FormLine;
import org.hzero.platform.domain.repository.FormHeaderRepository;
import org.hzero.platform.domain.repository.FormLineRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 表单配置头应用服务默认实现
 *
 * @author xiaoyu.zhao@hand-china.com 2019-07-11 17:50:08
 */
@Service
public class FormLineServiceImpl implements FormLineService {

    private final FormLineRepository formLineRepository;
    private final FormHeaderRepository formHeaderRepository;
    private final BaseFormLineRepository baseFormLineRepository;

    @Autowired
    public FormLineServiceImpl(FormLineRepository formLineRepository, FormHeaderRepository formHeaderRepository,
                    BaseFormLineRepository baseFormLineRepository) {
        this.formLineRepository = formLineRepository;
        this.formHeaderRepository = formHeaderRepository;
        this.baseFormLineRepository = baseFormLineRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FormLine createFormLine(FormLine formLine) {
        // 先校验参数合法性
        formLineRepository.checkRepeat(formLine);
        // 插入数据
        formLineRepository.insertSelective(formLine);
        // 添加缓存
        saveFormLineCache(formLine);
        return formLine;
    }

    @Override
    public FormLine updateFormLine(FormLine formLine) {
        formLineRepository.updateOptional(formLine, FormLine.FIELD_ORDER_SEQ, FormLine.FIELD_DEFAULT_VALUE,
                        FormLine.FIELD_ITEM_NAME, FormLine.FIELD_ITEM_TYPE_CODE, FormLine.FIELD_ENABLED_FLAG,
                        FormLine.FIELD_ITEM_DESCRIPTION, FormLine.FIELD_REQUIRED_FLAG, FormLine.FIELD_UPDATABLE_FLAG,
                        FormLine.FIELD_VALUE_CONSTRAINT);
        // 更新缓存
        saveFormLineCache(formLine);
        return formLine;
    }

    @Override
    public void deleteFormLine(FormLine formLine) {
        // 单条删除表单行信息
        formLineRepository.deleteByPrimaryKey(formLine);
        // 删除缓存
        FormHeader formHeader = formHeaderRepository.selectByPrimaryKey(formLine.getFormHeaderId());
        baseFormLineRepository.deleteFormLineCache(formHeader.getFormCode(), formLine.getTenantId(),
                        formLine.getItemCode());
    }

    @Override
    public List<FormLine> listFormLineByHeaderCode(String formCode, Long tenantId) {
        FormHeader formHeader =
                        new FormHeader().setFormCode(formCode).setTenantId(tenantId)
                                        .setEnabledFlag(BaseConstants.Flag.YES);
        FormHeader resultHeader = formHeaderRepository.selectOne(formHeader);
        if (resultHeader == null) {
            // 查不到租户数据就查平台
            formHeader.setTenantId(BaseConstants.DEFAULT_TENANT_ID);
            resultHeader = formHeaderRepository.selectOne(formHeader);
            if (resultHeader == null) {
                return Collections.emptyList();
            }
        }
        // 查询行数据
        return formLineRepository.selectByCondition(Condition
                        .builder(FormLine.class)
                        .andWhere(Sqls.custom()
                                        .andEqualTo(FormLine.FIELD_FORM_HEADER_ID, resultHeader.getFormHeaderId())
                                        .andEqualTo(FormLine.FIELD_ENABLED_FLAG, BaseConstants.Flag.YES)).build());
    }

    @Override
    public List<FormLine> listFormLineByHeaderId(Long formHeaderId, Long tenantId) {
        return formLineRepository.selectByCondition(Condition
                        .builder(FormLine.class)
                        .andWhere(Sqls.custom().andEqualTo(FormLine.FIELD_FORM_HEADER_ID, formHeaderId)
                                        .andEqualTo(FormLine.FIELD_ENABLED_FLAG, BaseConstants.Flag.YES)).build());
    }

    /**
     * 添加或更新表单配置行缓存
     */
    private void saveFormLineCache(FormLine formLine) {
        FormHeader formHeader = formHeaderRepository.selectByPrimaryKey(formLine.getFormHeaderId());
        FormLineVO formLineVO = new FormLineVO();
        BeanUtils.copyProperties(formLine, formLineVO);
        baseFormLineRepository.saveFormLineCache(formHeader.getFormCode(), formLineVO);
    }
}
