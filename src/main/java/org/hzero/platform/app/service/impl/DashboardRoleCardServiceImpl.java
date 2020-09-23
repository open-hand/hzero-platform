package org.hzero.platform.app.service.impl;

import org.hzero.platform.app.service.DashboardRoleCardService;
import org.hzero.platform.domain.entity.DashboardRoleCard;
import org.hzero.platform.domain.repository.DashboardRoleCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色卡片表应用服务默认实现
 *
 * @author xiaoyu.zhao@hand-china.com 2019-01-24 19:58:17
 */
@Service
public class DashboardRoleCardServiceImpl implements DashboardRoleCardService {

    @Autowired
    private DashboardRoleCardRepository roleCardRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<DashboardRoleCard> batchCreateOrUpdateRoleCard(List<DashboardRoleCard> dashboardRoleCards) {
        Map<Boolean, List<DashboardRoleCard>> updateAndInsertMap = dashboardRoleCards.stream()
                .distinct()
                .collect(Collectors.partitioningBy(roleCard -> roleCard.getObjectVersionNumber() == null));
        List<DashboardRoleCard> insertList = updateAndInsertMap.get(true);
        List<DashboardRoleCard> updateList = updateAndInsertMap.get(false);
        insertList.forEach(roleCard -> {
            Long roleTenantId = roleCardRepository.selectRoleTenant(roleCard.getRoleId());
            roleCard.setTenantId(roleTenantId);
            roleCard.validate(roleCardRepository);
        });
        // 校验完成，保存和更新数据
        roleCardRepository.batchInsertSelective(insertList);
        roleCardRepository.batchUpdateByPrimaryKeySelective(updateList);
        return dashboardRoleCards;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchRemoveRoleCard(List<DashboardRoleCard> dashboardRoleCards) {
        // 直接删除
        roleCardRepository.batchDeleteByPrimaryKey(dashboardRoleCards);
    }
}
