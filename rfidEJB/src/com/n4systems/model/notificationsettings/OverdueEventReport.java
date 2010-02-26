package com.n4systems.model.notificationsettings;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OverdueEventReport {
	@Column(nullable=false)
	public boolean includeOverdue;

	public OverdueEventReport() {
	}
}