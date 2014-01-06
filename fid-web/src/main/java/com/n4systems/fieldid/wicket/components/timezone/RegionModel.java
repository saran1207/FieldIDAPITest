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
    public void detach() {}

    @Override
    public Region getObject() {
        Region region = CountryList.getInstance().getRegionByFullId(timeZoneIdModel.getObject());
        return region;
    }

    @Override
    public void setObject(Region region) {
        Country country = countryModel.getObject();
        if (country!=null) {
            timeZoneIdModel.setObject(country.getFullName(region));
        } else {
            timeZoneIdModel.setObject(null);
        }
    }

}
