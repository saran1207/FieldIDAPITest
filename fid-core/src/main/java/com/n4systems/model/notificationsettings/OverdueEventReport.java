package com.n4systems.model.notificationsettings;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class OverdueEventReport implements Serializable {
	@Column(nullable=false)
	public boolean includeOverdue;

	public OverdueEventReport() {
	}
}