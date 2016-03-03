package com.fieldid.data.service;


import com.n4systems.model.Button;
import com.n4systems.model.ButtonGroup;
import com.n4systems.model.Tenant;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static com.n4systems.util.FunctionalUtils.bind;
import static com.n4systems.util.FunctionalUtils.map;

@Component
public class ButtonGroupMigrator extends DataMigrator<ButtonGroup> {

	public ButtonGroupMigrator() {
		super(ButtonGroup.class);
	}

	@Override
	@Transactional
	protected ButtonGroup copy(ButtonGroup srcGroup, Tenant newTenant, String newName) {
		ButtonGroup dstGroup = new ButtonGroup();
		dstGroup.setTenant(newTenant);
		dstGroup.setName(newName);
		dstGroup.setRetired(srcGroup.isRetired());
		dstGroup.getButtons().addAll(map(srcGroup.getButtons(), bind(this::migrateButton, newTenant)));
		persistenceService.save(dstGroup);
		return dstGroup;
	}

	private Button migrateButton(Tenant newTenant, Button srcButton) {
		Button dstButton = new Button();
		dstButton.setTenant(newTenant);
		dstButton.setRetired(srcButton.isRetired());
		dstButton.setDisplayText(srcButton.getDisplayText());
		dstButton.setEventResult(srcButton.getEventResult());
		dstButton.setButtonName(srcButton.getButtonName());
		return dstButton;
	}

}