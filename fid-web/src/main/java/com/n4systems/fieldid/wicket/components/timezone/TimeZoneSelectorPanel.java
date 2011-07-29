package com.n4systems.fieldid.wicket.components.timezone;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.n4systems.util.timezone.Region;

public class TimeZoneSelectorPanel extends Panel {
	
	public TimeZoneSelectorPanel(String id, IModel<String> timeZoneIdModel) {
		super(id, timeZoneIdModel);
		setRenderBodyOnly(true);
		
		final CountryModel countryModel = new CountryModel(timeZoneIdModel);
		final RegionModel regionModel = new RegionModel(timeZoneIdModel, countryModel);
		
		add(new DropDownChoice<Country>("country", countryModel, new CountryListModel(), new ListableChoiceRenderer<Country>()) {
			@Override
			protected boolean wantOnSelectionChangedNotifications() {
				return true;
			}
			@Override
			protected void onSelectionChanged(Country newCountry) {
				regionModel.setObject(newCountry.getRegions().first());
			}
		});
		
		add(new DropDownChoice<Region>("timeZone", regionModel, new RegionListModel(countryModel), new ListableChoiceRenderer<Region>()));
	}
	
	private class CountryModel implements IModel<Country> {
		private String countryId;
		
		public CountryModel(IModel<String> timeZoneIdModel) {
			setObject(CountryList.getInstance().getCountryByFullName(timeZoneIdModel.getObject()));
		}

		@Override
		public void detach() {}

		@Override
		public Country getObject() {
			Country country = CountryList.getInstance().getCountryById(countryId);
			return country;
		}

		@Override
		public void setObject(Country country) {
			countryId = country.getId();
		}
	}
	
	private class RegionModel implements IModel<Region> {
		private IModel<String> timeZoneIdModel;
		private IModel<Country> countryModel;
		
		public RegionModel(IModel<String> timeZoneIdModel, IModel<Country> countryModel) {
			this.timeZoneIdModel = timeZoneIdModel;
			this.countryModel = countryModel;
		}
		
		@Override
		public void detach() {}

		@Override
		public Region getObject() {
			Region region = CountryList.getInstance().getRegionByFullId(timeZoneIdModel.getObject());
			return region;
		}

		@Override
		public void setObject(Region region) {
			Country country = countryModel.getObject();
			timeZoneIdModel.setObject(country.getFullName(region));
		}
	}
	
	private class CountryListModel extends LoadableDetachableModel<List<Country>> {
		@Override
		protected List<Country> load() {
			SortedSet<Country> countries = CountryList.getInstance().getCountries();
			return new ArrayList<Country>(countries);
		}
	}

	private class RegionListModel extends LoadableDetachableModel<List<Region>> {
		private IModel<Country> countryModel;
		
		public RegionListModel(IModel<Country> countryModel) {
			this.countryModel = countryModel;
		}
		
		@Override
		protected List<Region> load() {
			SortedSet<Region> regions = countryModel.getObject().getRegions();
			return new ArrayList<Region>(regions);
		}
	}
}
