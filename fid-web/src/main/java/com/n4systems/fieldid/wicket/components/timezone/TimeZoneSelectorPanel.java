package com.n4systems.fieldid.wicket.components.timezone;

import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.Region;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class TimeZoneSelectorPanel extends Panel {
	
	public TimeZoneSelectorPanel(String id, IModel<String> timeZoneIdModel) {
		super(id, timeZoneIdModel);
		setRenderBodyOnly(true);
		
		final CountryModel countryModel = new CountryModel(timeZoneIdModel);
		final RegionModel regionModel = new RegionModel(timeZoneIdModel, countryModel);
		
		add(new FidDropDownChoice<Country>("country", countryModel, new CountryListModel(), new ListableChoiceRenderer<Country>()) {
			@Override
			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}
			@Override
			protected void onSelectionChanged(Country newCountry) {
				regionModel.setObject(newCountry.getRegions().first());
			}
		});
		
		add(new FidDropDownChoice<Region>("timeZone", regionModel, new RegionListModel(countryModel), new ListableChoiceRenderer<Region>()));
	}

}
