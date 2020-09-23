package org.hzero.platform.infra.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.hzero.platform.domain.entity.LovViewLine;

import io.choerodon.mybatis.common.BaseMapper;

/**
 * Lov视图行Mapper
 *
 * @author gaokuo.dai@hand-china.com 2018/06/19 14:31
 */
public interface LovViewLineMapper extends BaseMapper<LovViewLine> {
    
    /**
     * 根据视图头ID查询视图行数据
     * @param viewHeaderId
     * @param tenantId
     * @return
     */
    List<LovViewLine> selectLovViewLinesByHeaderId(@Param("viewHeaderId") Long viewHeaderId, @Param("tenantId") Long tenantId);
    
}
