package com.n4systems.fieldid.wicket.components.timezone;

import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.n4systems.util.timezone.Region;
import org.apache.wicket.model.IModel;

public class RegionModel implements IModel<Region> {
    private IModel<String> timeZoneIdModel;
    private IModel<Country> countryModel;

    public RegionModel(IModel<String> timeZoneIdModel, IModel<Country> countryModel) {
        this.timeZoneIdModel = timeZoneIdModel;
        this.countryModel = countryModel;
    }

    @Override
    public Region getObject() {
        Region region = CountryList.getInstance().getRegionByFullId(timeZoneIdModel.getObject());
        return region;
    }

    @Override
    public void setObject(Region region) {
        Country country = countryModel.getObject();
        if (country!=null) {
            if (region!=null) {
                timeZoneIdModel.setObject(country.getFullName(region));
            } else {
                timeZoneIdModel.setObject(country.getFullName(country.getRegions().first()));
            }
        } else if (region!=null) {
            country = CountryList.getInstance().findCountryForRegion(region);
            timeZoneIdModel.setObject(country.getFullName(region));
        }
    }

    @Override
    public void detach() {
    }
}
