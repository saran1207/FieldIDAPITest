package com.n4systems.model.notificationsettings;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class FailedEventReport implements Serializable {

	@Column(nullable=false)
	public boolean smartFailure;		
	
	@Column(nullable=false)
	public boolean includeFailed;

	public FailedEventReport() {
	}

}
