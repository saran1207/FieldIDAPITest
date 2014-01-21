package com.n4systems.model.api;

import com.n4systems.model.GpsLocation;

import java.io.Serializable;

public interface HasGpsLocation extends Serializable {
    GpsLocation getGpsLocation();
}
