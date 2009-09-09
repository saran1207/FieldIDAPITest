package com.n4systems.model.taskconfig;

import com.n4systems.model.parents.AbstractStringIdEntity;
import com.n4systems.taskscheduling.ScheduledTask;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tasks")
public class TaskConfig extends AbstractStringIdEntity {
	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_TASK_GROUP = "default";
	
	@Column(nullable=false)
	private String className;
	
	@Column(nullable=false)
	private String cronExpression;
	
	@Column(nullable=false)
	private Boolean enabled = true;
	private String taskGroup = DEFAULT_TASK_GROUP;
	
	public TaskConfig() {}

	public String getClassName() {
    	return className;
    }

	public void setClassName(String taskClassName) {
    	this.className = taskClassName;
    }
	
	public void setClassName(Class<? extends ScheduledTask> taskClass) {
    	this.className = taskClass.getName();
    }

	public String getTaskGroup() {
    	return taskGroup;
    }

	public void setTaskGroup(String taskGroup) {
    	this.taskGroup = taskGroup;
    }

	public String getCronExpression() {
    	return cronExpression;
    }

	public void setCronExpression(String cronExpression) {
    	this.cronExpression = cronExpression;
    }

	public Boolean isEnabled() {
    	return enabled;
    }

	public void setEnabled(Boolean enabled) {
    	this.enabled = enabled;
    }
	
}
