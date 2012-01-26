package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.Date;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModelWithOwner;

public class ApiEvent extends ApiReadWriteModelWithOwner {
	private Long typeId;
	private Long assetId;
	private Date date;
	private Long assignedUserId;
	private Long performedById;
	private Long eventBookId;
	private Long assetStatusId;
	private Long predefinedLocationId;
	private String freeformLocation;
	private boolean printable;
	private String comments;
	private Long gpsLatitude;
	private Long gpsLongitude;
}
