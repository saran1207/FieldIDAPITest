package com.n4systems.services.search;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.api.HasGpsLocation;

import java.io.Serializable;
import java.util.*;

public class MappedResults<T extends HasGpsLocation> implements Serializable {

    private Integer count;
    // TODO : aggregate locations that are within say, 2% of each other.  for this i'd need to know the bounds of all results first.
    // i.e. go through all adding to sorted map, keep a running tab on min/max lat/lng then go through and find all
    private Map<GpsLocation,MappedResult<T>> results = Maps.newHashMap();

    public MappedResults(ArrayList<GpsLocation> locations) {
        add(locations);
    }

    public MappedResults() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    protected String getDescription(T entity) {
        return entity==null ? "" : "desc = " + entity.getGpsLocation().toPointString();
    }

    protected String getDescription(Integer groupCount) {
        return groupCount + "";
    }

    public MappedResults<T> addLocations(List<T> locations) {
        for (T hasGpsLocation:locations) {
            GpsLocation gpsLocation = hasGpsLocation.getGpsLocation();
            add(gpsLocation, hasGpsLocation);
        }
        setCount(locations.size());
        return this;
    }

    public MappedResults<T> add(List<GpsLocation> locations) {
        for (GpsLocation location:locations) {
            add(location, null);
        }
        return this;
    }

    private void add(GpsLocation gpsLocation, T entity) {
        MappedResult<T> result = results.get(gpsLocation);
        if (result==null) {
            results.put(gpsLocation,result = new MappedResult(gpsLocation));
        }
        result.add(entity);
    }

    public Collection<MappedResult<T>> getMappedResults() {
        return results.values();
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
