package com.n4systems.services.search;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.api.HasGpsLocation;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MappedResults<T extends HasGpsLocation> implements Serializable {

    private Integer count;
    // TODO : aggregate locations that are within say, 2% of each other.  for this i'd need to know the bounds of all results first.
    // i.e. go through all adding to sorted map, keep a running tab on min/max lat/lng then go through and find all
    private Map<GpsLocation,MappedResult> results = Maps.newHashMap();

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

    public MappedResults<T> addLocations(List<? extends HasGpsLocation> locations) {
        for (HasGpsLocation hasGpsLocation:locations) {
            GpsLocation gpsLocation = hasGpsLocation.getGpsLocation();
            add(gpsLocation, hasGpsLocation.toString());
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

    private void add(GpsLocation gpsLocation, String description) {
        MappedResult result = results.get(gpsLocation);
        if (result==null) {
            results.put(gpsLocation,result = new MappedResult(gpsLocation));
        }
        result.add(description);
    }

    public Iterator<MappedResult> getMappedResults() {
        return results.values().iterator();
    }

    public boolean isEmpty() {
        return results.isEmpty();
    }


    public class MappedResult implements Serializable {
        List<String> descs = Lists.newArrayList();
        GpsLocation location;

        MappedResult(GpsLocation location) {
            this.location = location;
        }

        MappedResult add(String desc) {
            if (StringUtils.isNotBlank(desc)) {
                descs.add(desc);
            }
            return this;
        }

        MappedResult add(T entry) {
            descs.add(getDescription(entry));
            return this;
        }

        public List<String> getDescs() {
            return descs;
        }

        public GpsLocation getLocation() {
            return location;
        }

        public String toString() {
            if (descs.size()==0) {
                return null;
            } else if (descs.size()==1) {
                return descs.get(0);
            } else { //if (descs.size()>1) {
                return Joiner.on("\n").join(getDescription(descs.size()), descs);
            }
        }

    }

}
