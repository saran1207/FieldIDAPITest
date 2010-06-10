package com.n4systems.fieldid.reporting.helpers;

import java.util.Random;

import com.n4systems.fieldid.viewhelpers.ColumnMapping;
import com.n4systems.model.builders.Builder;
import com.n4systems.util.RandomString;

public class ColumnMappingBuilder implements Builder<ColumnMapping> {

	private final String id;
	private final String label;
	private final String pathExpression;
	private final String sortExpression;
	private final String outputHandler;
	private final boolean sortable;
	private final boolean onByDefault;
	private final int order;
	private final String requiredExtendedFeature;
	
	
	public static ColumnMappingBuilder aColumnMapping() {
		return new ColumnMappingBuilder(RandomString.getString(10), RandomString.getString(10), "path.path", "path.path", null, true, true, new Random().nextInt(), null);
	}
	
	public ColumnMappingBuilder(String id, String label, String pathExpression, String sortExpression, String outputHandler, boolean sortable, boolean onByDefault, int order,
			String requiredExtendedFeature) {
		super();
		this.id = id;
		this.label = label;
		this.pathExpression = pathExpression;
		this.sortExpression = sortExpression;
		this.outputHandler = outputHandler;
		this.sortable = sortable;
		this.onByDefault = onByDefault;
		this.order = order;
		this.requiredExtendedFeature = requiredExtendedFeature;
	}
	
	public ColumnMapping build() {
		return new ColumnMapping(id, label, pathExpression, sortExpression, outputHandler, sortable, onByDefault, order, requiredExtendedFeature);
	}

	

}
