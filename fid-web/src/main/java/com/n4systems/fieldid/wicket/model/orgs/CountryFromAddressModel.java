package com.n4systems.fieldid.wicket.model.orgs;

import com.n4systems.model.AddressInfo;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class CountryFromAddressModel extends Model<Country> {
    private final IModel<AddressInfo> addressModel;

    public CountryFromAddressModel(IModel<AddressInfo> model) {
        super();
        this.addressModel = model;
    }

    @Override
    public Country getObject() {
        return CountryList.getInstance().getCountryByName(addressModel.getObject().getCountry());
    }

    @Override
    public void setObject(Country country) {
        addressModel.getObject().setCountry(country.getName());
    }
}