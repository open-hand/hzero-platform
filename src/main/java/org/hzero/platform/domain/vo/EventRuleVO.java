package org.hzero.platform.domain.vo;

import org.hzero.platform.domain.entity.EventRule;

/**
 * 事件规则值对象，存储到缓存
 *
 * @author jiangzhou.bo@hand-china.com 2018/06/12 14:08
 */
public class EventRuleVO {
    private Long eventRuleId;
    private Long eventId;
    private Integer syncFlag;
    private String callType;
    private String beanName;
    private String methodName;
    private String apiUrl;
    private String apiMethod;
    private Integer orderSeq;
    private String matchingRule;
    private Integer resultFlag;
    private Integer enabledFlag;

    public EventRuleVO(EventRule eventRule) {
        this.eventRuleId = eventRule.getEventRuleId();
        this.eventId = eventRule.getEventId();
        this.syncFlag = eventRule.getSyncFlag();
        this.callType = eventRule.getCallType();
        this.beanName = eventRule.getBeanName();
        this.methodName = eventRule.getMethodName();
        this.apiUrl = eventRule.getApiUrl();
        this.apiMethod = eventRule.getApiMethod();
        this.orderSeq = eventRule.getOrderSeq();
        this.matchingRule = eventRule.getMatchingRule();
        this.resultFlag = eventRule.getResultFlag();
        this.enabledFlag = eventRule.getEnabledFlag();
    }

    public Long getEventRuleId() {
        return eventRuleId;
    }

    public void setEventRuleId(Long eventRuleId) {
        this.eventRuleId = eventRuleId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Integer getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(Integer syncFlag) {
        this.syncFlag = syncFlag;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getMatchingRule() {
        return matchingRule;
    }

    public void setMatchingRule(String matchingRule) {
        this.matchingRule = matchingRule;
    }

    public Integer getResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(Integer resultFlag) {
        this.resultFlag = resultFlag;
    }

    public Integer getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(Integer enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EventRuleVO [eventRuleId=");
        builder.append(eventRuleId);
        builder.append(", eventId=");
        builder.append(eventId);
        builder.append(", syncFlag=");
        builder.append(syncFlag);
        builder.append(", callType=");
        builder.append(callType);
        builder.append(", beanName=");
        builder.append(beanName);
        builder.append(", methodName=");
        builder.append(methodName);
        builder.append(", apiUrl=");
        builder.append(apiUrl);
        builder.append(", apiMethod=");
        builder.append(apiMethod);
        builder.append(", orderSeq=");
        builder.append(orderSeq);
        builder.append(", matchingRule=");
        builder.append(matchingRule);
        builder.append(", resultFlag=");
        builder.append(resultFlag);
        builder.append(", enabledFlag=");
        builder.append(enabledFlag);
        builder.append("]");
        return builder.toString();
    }
    
}
