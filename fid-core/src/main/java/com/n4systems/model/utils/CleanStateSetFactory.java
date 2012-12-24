package com.n4systems.model.utils;

import com.n4systems.model.Button;
import com.n4systems.model.ButtonGroup;
import com.n4systems.model.Tenant;

public class CleanStateSetFactory {

	private ButtonGroup originalButtonGroup;
	private Tenant targetTenant;
	
	public CleanStateSetFactory(ButtonGroup originalButtonGroup, Tenant targetTenant) {
		super();
		this.originalButtonGroup = originalButtonGroup;
		this.targetTenant = targetTenant;
	}
	
	
	public ButtonGroup clean() {
		originalButtonGroup.setId(null);
		originalButtonGroup.setCreated(null);
		originalButtonGroup.setModified(null);
		originalButtonGroup.setModifiedBy(null);
		cleanStates();
		
		originalButtonGroup.setTenant(targetTenant);
		return originalButtonGroup;
	}
	
	private void cleanStates() {
		originalButtonGroup.setButtons(originalButtonGroup.getAvailableButtons());
		for (Button originalButton : originalButtonGroup.getButtons()) {
			originalButton.setId(null);
			originalButton.setCreated(null);
			originalButton.setModified(null);
			originalButton.setModifiedBy(null);
			originalButton.setTenant(targetTenant);
		}
	}
	
}
