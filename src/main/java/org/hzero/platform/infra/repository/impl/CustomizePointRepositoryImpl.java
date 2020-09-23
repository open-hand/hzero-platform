package org.hzero.platform.infra.repository.impl;

import org.springframework.stereotype.Component;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.util.Sqls;
import org.hzero.platform.domain.entity.CustomizePoint;
import org.hzero.platform.domain.repository.CustomizePointRepository;

/**
 * API个性化切入点 资源库实现
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
@Component
public class CustomizePointRepositoryImpl extends BaseRepositoryImpl<CustomizePoint> implements CustomizePointRepository {

    @Override
    public Page<CustomizePoint> pageCustomizePoint(PageRequest pageRequest, CustomizePoint customizePoint) {
        return PageHelper.doPage(pageRequest, () ->
                selectByCondition(
                        Condition.builder(CustomizePoint.class)
                        .where(Sqls.custom()
                                .andLike(CustomizePoint.FIELD_SERVICE_NAME, customizePoint.getServiceName(), true)
                                .andLike(CustomizePoint.FIELD_CLASS_NAME, customizePoint.getClassName(), true)
                                .andLike(CustomizePoint.FIELD_PACKAGE_NAME, customizePoint.getPackageName(), true)
                                .andLike(CustomizePoint.FIELD_METHOD_NAME, customizePoint.getMethodName(), true)
                        )
                        .orderByAsc(
                                CustomizePoint.FIELD_SERVICE_NAME,
                                CustomizePoint.FIELD_CLASS_NAME,
                                CustomizePoint.FIELD_PACKAGE_NAME,
                                CustomizePoint.FIELD_METHOD_NAME
                        )
                        .build()
                )
        );
    }
}
