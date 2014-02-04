package com.n4systems.fieldid.wicket.components.timezone;

import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import org.apache.wicket.model.IModel;

public class CountryModel implements IModel<Country> {
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
