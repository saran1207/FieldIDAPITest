package com.n4systems.fieldid.actions.event;

import com.n4systems.model.EventSchedule;

public class WebEventSchedule {
	public static WebEventSchedule createAutoScheduled(EventSchedule schedule) {
		WebEventSchedule webEventSchedule = new WebEventSchedule();
		webEventSchedule.setAutoScheduled(true);
		return webEventSchedule;
	}
	

	private Long type;
	private Long job;
	
	private String date;
	
	private String typeName;
	private String jobName;

	private boolean autoScheduled;
	
	
	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getJob() {
		return job;
	}

	public void setJob(Long job) {
		this.job = job;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public boolean isAutoScheduled() {
		return autoScheduled;
	}

	public void setAutoScheduled(boolean autoScheduled) {
		this.autoScheduled = autoScheduled;
	}


	
	
	
	
}
