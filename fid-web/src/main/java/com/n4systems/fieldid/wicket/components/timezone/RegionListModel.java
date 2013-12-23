package com.n4systems.fieldid.wicket.components.timezone;

import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.n4systems.util.timezone.Region;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.ArrayList;
import java.util.List;

public  class RegionListModel extends LoadableDetachableModel<List<Region>> {
    private IModel<Country> countryModel;

    public RegionListModel(IModel<Country> countryModel) {
        this.countryModel = countryModel;
    }

    @Override
    protected List<Region> load() {
        Country country = countryModel.getObject();
        return (country==null) ?
            CountryList.getInstance().getAllRegions() :
            new ArrayList<Region>(country.getRegions());
    }
}
