package com.n4systems.model.notificationsettings;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FailedEventReport {

	@Column(nullable=false)
	public boolean smartFailure;		
	
	@Column(nullable=false)
	public boolean includeFailed;

	public FailedEventReport() {
	}

}
