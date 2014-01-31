package com.n4systems.services.search;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.api.HasGpsLocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MappedResults<T extends HasGpsLocation> implements Serializable {

    private Integer count = 0;
    // TODO : aggregate locations that are within say, 2% of each other.  for this i'd need to know the bounds of all results first.
    // i.e. go through all adding to sorted map, keep a running tab on min/max lat/lng then go through and find all
    private Map<GpsLocation,MappedResult<T>> results = Maps.newHashMap();
    private boolean grouped = false;

    public MappedResults(ArrayList<GpsLocation> locations) {
        add(locations);
    }

    public MappedResults() {
    }

    public Integer getCount() {
        return count;
    }

    protected String getDescription(T entity) {
        return entity==null ? "" : "desc = " + entity.getGpsLocation().toPointString();
    }

    protected String getDescription(Integer groupCount) {
        return groupCount + "";
    }

    public MappedResults<T> add(T hasGpsLocation) {
        Preconditions.checkState(!grouped,"can't add single locations after you have already added grouped result ");
        GpsLocation gpsLocation = hasGpsLocation.getGpsLocation();
        add(gpsLocation, hasGpsLocation);
        return this;
    }

    public MappedResults<T> add(List<GpsLocation> locations) {
        for (GpsLocation location:locations) {
            add(location, null);
        }
        return this;
    }

    public void add(GpsLocation gpsLocation, T entity) {
        Preconditions.checkState(!grouped,"can't add single locations after you have already added grouped result ");
        if (gpsLocation==null) {
            return;
        }
        MappedResult<T> result = results.get(gpsLocation);
        if (result==null) {
            results.put(gpsLocation,result = new MappedResult(gpsLocation));
        }
        if (entity!=null) {
            result.add(entity);
        }
        count++;
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }

    public Collection<GpsLocation> getLocations() {
        return results.keySet();
    }

    public List<T> getEntitiesAtLocation(GpsLocation location) {
        MappedResult<T> result = results.get(location);
        return result==null ? null : result.getEntities();
    }

    public void setGroupedResult(T hasGpsLocation) {
        Preconditions.checkState(results.isEmpty(),"you can't add grouped results after individual results have already been added. " + results.values());
        Preconditions.checkState(!grouped,"a grouped result has already been set" + results.values().iterator().next());
        add(hasGpsLocation.getGpsLocation(), hasGpsLocation);
        this.grouped = true;
    }


    public class MappedResult<E extends HasGpsLocation> implements Serializable {
        List<E> entities = Lists.newArrayList();
        GpsLocation location;

        MappedResult(GpsLocation location) {
            this.location = location;
        }

        MappedResult add(E entity) {
            entities.add(entity);
            return this;
        }

        public List<E> getEntities() {
            return entities;
        }

        public GpsLocation getLocation() {
            return location;
        }

        public String toString() {
            if (entities.size()==0) {
                return null;
            } else if (entities.size()==1) {
                return entities.get(0).toString();
            } else { //if (descs.size()>1) {
                return getDescription(entities.size()) + "\t" + Joiner.on("\t").skipNulls().join(entities);
            }
        }

    }

}
