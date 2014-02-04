package com.n4systems.fieldid.wicket.components.timezone;

import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public  class CountryListModel extends LoadableDetachableModel<List<Country>> {
    @Override
    protected List<Country> load() {
        SortedSet<Country> countries = CountryList.getInstance().getCountries();
        return new ArrayList<Country>(countries);
    }
}

