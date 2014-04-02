package com.n4systems.fieldid.wicket.components;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.api.HasGpsLocation;
import com.n4systems.services.search.MappedResults;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

public class GpsModel<T extends HasGpsLocation> extends LoadableDetachableModel<MappedResults<T>> {

    private IModel<MappedResults<T>> model= null;

    public GpsModel() {

    }

    public GpsModel(ArrayList<GpsLocation> locations) {
        this.model = Model.of(new MappedResults<T>(locations));
    }

    public GpsModel(GpsLocation location) {
        this(Lists.newArrayList(location));
    }

    public GpsModel(GpsLocation... location) {
        this(Lists.newArrayList(location));
    }

    public GpsModel(List<? extends HasGpsLocation> entities) {
        ArrayList<GpsLocation> locations = Lists.newArrayList();
        for (HasGpsLocation entity:entities) {
            if (entity.getGpsLocation()!=null) {
                locations.add(entity.getGpsLocation());
            }
        }
        this.model = Model.of(new MappedResults<T>(locations));
    }

    public GpsModel(final IModel<GpsLocation> gpsLocation) {
        model = new Model<MappedResults<T>>() {
            @Override public MappedResults<T> getObject() {
                MappedResults<T> data = new MappedResults();
                if(gpsLocation.getObject() != null) {
                    data.add(Lists.newArrayList(gpsLocation.getObject()));
                }
                return data;
            }

            @Override public void detach() {
                super.detach();
                gpsLocation.detach();
            }
        };
    }

    @Override
    protected MappedResults<T> load() {
        Preconditions.checkNotNull(model,"no model supplied to get GpsLocations.");
        return model.getObject();
    }

    public boolean isEmpty() {
        return getObject().isEmpty();
    }

    @Override
    public void detach() {
        super.detach();
        if (model!=null) {
            model.detach();
        }
    }
}
