package com.n4systems.fieldid.wicket.components;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.api.HasGpsLocation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

public class GpsModel extends LoadableDetachableModel<List<GpsLocation>> {

    private IModel<List<GpsLocation>> model= null;
    private IModel<List<? extends HasGpsLocation>> indirectModel = null;

    public GpsModel(ArrayList<GpsLocation> locations) {
        this.model = new Model(locations);
    }

    public GpsModel(GpsLocation location) {
        this(Lists.newArrayList(location));
    }

    public GpsModel(HasGpsLocation entity) {
        this(Lists.newArrayList(entity.getGpsLocation()));
    }

    public GpsModel(List<? extends HasGpsLocation> entities) {
        ArrayList<GpsLocation> locations = Lists.newArrayList();
        for (HasGpsLocation entity:entities) {
            locations.add(entity.getGpsLocation());
        }
        this.model = new Model(locations);
    }

    public GpsModel(IModel<List<? extends HasGpsLocation>> entitiesModel) {
        this.indirectModel = entitiesModel;
        this.model = null;
    }

    @Override
    protected List<GpsLocation> load() {
        if (model!=null) {
            return model.getObject();
        } else {
            Preconditions.checkNotNull(indirectModel);
            ArrayList<GpsLocation> locations = Lists.newArrayList();
            for (HasGpsLocation entity:indirectModel.getObject()) {
                locations.add(entity.getGpsLocation());
            }
            return locations;
        }
    }

    @Override
    public void detach() {
        super.detach();
        if(indirectModel!=null) {
            indirectModel.detach();
        }
    }

    public boolean isEmpty() {
        return getObject().isEmpty();
    }
}
