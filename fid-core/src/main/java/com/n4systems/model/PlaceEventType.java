package com.n4systems.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="place_event_types")
@PrimaryKeyJoinColumn(name="id")
public class PlaceEventType extends EventType {


}
