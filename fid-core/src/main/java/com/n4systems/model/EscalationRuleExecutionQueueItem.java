package com.n4systems.model;

import javax.persistence.*;
import java.util.Date;

/**
 * This entity represents the relationship between an Event and an individual rule.  This helps to ensure the correct
 * rules are executed at the correct time, and provides a temporary view into the timeline of rule execution.
 *
 * This data is, however, erased as soon as an event is closed or completed.
 *
 * Created by Jordan Heath on 2015-08-23.
 */
@Entity
@Table(name = "escalation_rule_execution_queue")
public class EscalationRuleExecutionQueueItem extends BaseEntity {

	@Column(name = "event_id")
	private Long eventId;

	@ManyToOne
	@JoinColumn(name = "rule_id")
	private AssignmentEscalationRule rule;

	@Column(name = "rule_has_run")
	private boolean ruleHasRun;

	@Column(name = "notify_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date notifyDate;

	@Column(name = "event_mod_date")
	private Date eventModDate;

	/**
	 * This is where we hold a cache of the map that will feed the email.  If there's been no modification of the event,
	 * then we fill the email body from this map.  Otherwise, we'll have to generate a new one on the fly.  This should
	 * help save us some time.
	 */
	@Column(name = "map_json")
	private String mapJson;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public AssignmentEscalationRule getRule() {
		return rule;
	}

	public void setRule(AssignmentEscalationRule rule) {
		this.rule = rule;
	}

	public boolean isRuleHasRun() {
		return ruleHasRun;
	}

	public void setRuleHasRun(boolean ruleHasRun) {
		this.ruleHasRun = ruleHasRun;
	}

	public Date getNotifyDate() {
		return notifyDate;
	}

	public void setNotifyDate(Date notifyDate) {
		this.notifyDate = notifyDate;
	}

	public Date getEventModDate() {
		return eventModDate;
	}

	public void setEventModDate(Date eventModDate) {
		this.eventModDate = eventModDate;
	}

	public String getMapJson() {
		return mapJson;
	}

	public void setMapJson(String mapJson) {
		this.mapJson = mapJson;
	}
}
