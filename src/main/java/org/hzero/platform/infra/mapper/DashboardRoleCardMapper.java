package org.hzero.platform.infra.mapper;

import org.apache.ibatis.annotations.Param;
import org.hzero.platform.domain.entity.DashboardRoleCard;
import io.choerodon.mybatis.common.BaseMapper;

import java.util.List;

/**
 * 角色卡片表Mapper
 *
 * @author xiaoyu.zhao@hand-china.com 2019-01-24 19:58:17
 */
public interface DashboardRoleCardMapper extends BaseMapper<DashboardRoleCard> {

    /**
     * 分页查询角色卡片列表
     *
     * @param dashboardRoleCard 角色卡片实体
     * @return List<DashboardRoleCard>
     */
    List<DashboardRoleCard> selectRoleCardList(DashboardRoleCard dashboardRoleCard);

    /**
     * 查询角色卡片分配信息
     *
     * @param dashboardRoleCard 角色卡片实体
     * @return List<DashboardRoleCard>
     */
    List<DashboardRoleCard> selectRoleAssignCardList(DashboardRoleCard dashboardRoleCard);

    /**
     * 查询获取超级管理员角色Id
     *
     * @return roleCards
     */
    List<Long> selectSuperRoleIds();

    /**
     * 查询所有当前角色的父级角色上设置默认的数据
     *
     * @param parentRoleList 父级Id集合
     * @return 所有数据
     */
    List<DashboardRoleCard> selectParentInitRoleCard(@Param("parentRoleList") List<Long> parentRoleList);

    /**
     * 查询获取当前角色下设置的初始化卡片信息
     *
     * @param roleId 角色Id
     * @return 初始化数据
     */
    List<DashboardRoleCard> selectCurrentRoleCards(@Param("roleId") Long roleId);

    /**
     * 查询角色租户Id信息
     *
     * @param roleId 角色Id标识
     * @return 角色租户Id
     */
    Long selectRoleTenant(@Param("roleId") Long roleId);
}
