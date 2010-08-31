package com.n4systems.fieldid.reporting.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.n4systems.fieldid.viewhelpers.ColumnMapping;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.model.builders.Builder;
import com.n4systems.util.RandomString;

public class ColumnMappingGroupBuilder implements Builder<ColumnMappingGroup> {
	private static final ArrayList<ColumnMapping> EMPTY_MAPPINGS = new ArrayList<ColumnMapping>();

	private final String id;
	private final String label;
	private final int order;
	private final boolean dynamic;
	private final List<ColumnMapping> mappings = new ArrayList<ColumnMapping>();

	public static ColumnMappingGroupBuilder aColumnMappingGroup() {
		return aStaticColumnMappingGroup();
	}
	
	public static ColumnMappingGroupBuilder aStaticColumnMappingGroup() {
		return new ColumnMappingGroupBuilder(randomId(), "group.label", randomOrder(), false, EMPTY_MAPPINGS);
	}
	
	public static ColumnMappingGroupBuilder aDynamicColumnMappingGroup() {
		return aStaticColumnMappingGroup().asDynamic();
	}
	
	private ColumnMappingGroupBuilder asDynamic() {
		return new ColumnMappingGroupBuilder(id, label, order, true, mappings);
	}

	private static String randomId() {
		return RandomString.getString(10);
	}


	private static int randomOrder() {
		return new Random().nextInt();
	}
	
	public ColumnMappingGroupBuilder withMappings(ColumnMapping...mappings) {
		return new ColumnMappingGroupBuilder(id, label, order, true, Arrays.asList(mappings));
	}
	
		
	private ColumnMappingGroupBuilder(String id, String label, int order, boolean dynamic, List<ColumnMapping> mappings) {
		super();
		this.id = id;
		this.label = label;
		this.order = order;
		this.dynamic = dynamic;
		this.mappings.addAll(mappings);
	}
	
	
	public ColumnMappingGroup build() {
		ColumnMappingGroup columnMappingGroup = new ColumnMappingGroup(id, label, order);
		columnMappingGroup.setDynamic(dynamic);
		columnMappingGroup.getMappings().addAll(mappings);
		return columnMappingGroup;
	}

	

	

}
