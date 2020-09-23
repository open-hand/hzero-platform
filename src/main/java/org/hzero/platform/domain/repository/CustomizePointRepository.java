package org.hzero.platform.domain.repository;

import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import org.hzero.mybatis.base.BaseRepository;
import org.hzero.platform.domain.entity.CustomizePoint;

/**
 * API个性化切入点资源库
 *
 * @author jiangzhou.bo@hand-china.com 2019-07-03 23:24:23
 */
public interface CustomizePointRepository extends BaseRepository<CustomizePoint> {

    Page<CustomizePoint> pageCustomizePoint(PageRequest pageRequest, CustomizePoint customizePoint);
}
