package org.hzero.platform.app.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.redis.RedisHelper;
import org.hzero.platform.app.service.EventService;
import org.hzero.platform.domain.entity.Event;
import org.hzero.platform.domain.entity.EventRule;
import org.hzero.platform.domain.repository.EventRepository;
import org.hzero.platform.domain.repository.EventRuleRepository;
import org.hzero.platform.infra.constant.HpfmMsgCodeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 事件应用服务默认实现
 *
 * @author jiangzhou.bo@hand-china.com 2018/06/11 16:54
 */
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventRuleRepository eventRuleRepository;
    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Event create(Event event) {
        event.validate(eventRepository);
        eventRepository.insertSelective(event);
        event.refreshCache(eventRuleRepository, redisHelper, objectMapper);
        return event;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Event update(Event event) {
        Event entity = eventRepository.selectByPrimaryKey(event.getEventId());
        entity.equalsEventCode(event);
        eventRepository.updateByPrimaryKeySelective(event);
        event.refreshCache(eventRuleRepository, redisHelper, objectMapper);
        return event;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long eventId) {
        Event event = eventRepository.selectByPrimaryKey(eventId);
        Assert.notNull(event, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        // 删除事件
        eventRepository.remove(eventId);
        // 清除缓存
        event.clearCache(redisHelper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRemove(List<Event> events) {
        Long[] eventIds =
                events.stream().map(Event::getEventId).collect(Collectors.toList())
                        .toArray(ArrayUtils.EMPTY_LONG_OBJECT_ARRAY);
        if (ArrayUtils.isNotEmpty(eventIds)) {
            for (Long eventId : eventIds) {
                remove(eventId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EventRule createEventRule(Long eventId, EventRule eventRule) {
        Event event = eventRepository.selectByPrimaryKey(eventId);
        Assert.notNull(event, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        eventRule.setEventId(eventId);
        eventRule.setTenantId(event.getTenantId());
        eventRule.validate();
        eventRuleRepository.insertSelective(eventRule);
        event.refreshCache(eventRuleRepository, redisHelper, objectMapper);
        return eventRule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EventRule updateEventRule(Long eventId, EventRule eventRule) {
        Event event = eventRepository.selectByPrimaryKey(eventId);
        Assert.notNull(event, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        eventRule.setTenantId(event.getTenantId());
        eventRule.setEventId(eventId);
        eventRule.validate();
        eventRuleRepository.updateByPrimaryKeySelective(eventRule);
        event.refreshCache(eventRuleRepository, redisHelper, objectMapper);
        return eventRule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRemoveEventRule(Long eventId, List<EventRule> eventRules) {
        Event event = eventRepository.selectByPrimaryKey(eventId);
        Assert.notNull(event, BaseConstants.ErrorCode.DATA_NOT_EXISTS);
        // 校验事件ID与事件规则行中事件ID是否一致
        eventRules.forEach(eventRule -> Assert.isTrue(Objects.equals(eventRule.getEventId(), eventId),
                HpfmMsgCodeConstants.ERROR_EVENT_NOT_MATCH));
        eventRuleRepository.batchDeleteByPrimaryKey(eventRules);
        event.refreshCache(eventRuleRepository, redisHelper, objectMapper);
    }

}
