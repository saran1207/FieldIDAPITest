package com.n4systems.model.builders;

import java.util.Date;

import com.n4systems.model.UnitOfMeasure;
import com.n4systems.model.user.User;

public class UnitOfMeasureBuilder extends AbstractEntityBuilder<UnitOfMeasure> {
	private final String name;
	private final String type;
	private final String shortName;
	private final boolean selectable;
	
	public UnitOfMeasureBuilder(Date created, Date modified, User modifiedBy, String name, String type, String shortName, boolean selectable) {
		super(null, created, modified, modifiedBy);
		this.name = name;
		this.type = type;
		this.shortName = shortName;
		this.selectable = selectable;
	}
	
	public static UnitOfMeasureBuilder aUnitOfMeasure() {
        return new UnitOfMeasureBuilder(null, null, null, null, null, null, false);
    }
	
	public UnitOfMeasureBuilder name(String name) {
		return makeBuilder(new UnitOfMeasureBuilder(created, modified, modifiedBy, name, type, shortName, selectable));
	}
	
	public UnitOfMeasureBuilder type(String type) {
		return makeBuilder(new UnitOfMeasureBuilder(created, modified, modifiedBy, name, type, shortName, selectable));
	}
	
	public UnitOfMeasureBuilder shortName(String shortName) {
		return makeBuilder(new UnitOfMeasureBuilder(created, modified, modifiedBy, name, type, shortName, selectable));
	}
	
	public UnitOfMeasureBuilder selectable(boolean selectable) {
		return makeBuilder(new UnitOfMeasureBuilder(created, modified, modifiedBy, name, type, shortName, selectable));
	}

	@Override
	public UnitOfMeasure createObject() {
		UnitOfMeasure unitOfMeasure = super.assignAbstractFields(new UnitOfMeasure());
		unitOfMeasure.setName(name);
		unitOfMeasure.setType(type);
		unitOfMeasure.setShortName(shortName);
		unitOfMeasure.setSelectable(selectable);
		return unitOfMeasure;
	}

}
