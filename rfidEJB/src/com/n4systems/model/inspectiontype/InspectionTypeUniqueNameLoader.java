package com.n4systems.model.inspectiontype;

import javax.persistence.EntityManager;

import com.n4systems.model.InspectionType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

/**
 * Given a name String, finds a new unique InspectionType name.  Names are generated via the format String.
 * The default for this is "<old name> - <#>".  The number will increment until a non-conflicting name is found.
 */
public class InspectionTypeUniqueNameLoader extends SecurityFilteredLoader<String> {
	public static final int MAX_ITERATIONS = 500;
	private static final String DEFAULT_NAME_FORMAT_STRING = "%s - %d";
	
	private final String nameFormatString; 
	private String name;
	
	public InspectionTypeUniqueNameLoader(SecurityFilter filter, String nameFormatString) {
		super(filter);
		this.nameFormatString = nameFormatString;
	}
	
	public InspectionTypeUniqueNameLoader(SecurityFilter filter) {
		this(filter, DEFAULT_NAME_FORMAT_STRING);
	}

	@Override
	protected String load(EntityManager em, SecurityFilter filter) {
		if (name == null) {
			throw new IllegalStateException("Name must not be null");
		}
		
		int i = 1;
		String newName = null;
		boolean conflicting = true;
		while (conflicting) {
			if (i > MAX_ITERATIONS) {
				// since nameFormatString is configurable, and our stopping condition depends on it, a little infinite loop protection is probably a good idea
				throw new RuntimeException(String.format("Unique product type name not found in %d itterations.  Original Name [%s], Tenant [%d]", MAX_ITERATIONS, name, filter.getTenantId()));
			}
			
			newName = String.format(nameFormatString, name, i);
			
			conflicting = typeWithSameNameExists(em, filter, newName);
			i++;
		}

		return newName;
	}
	
	protected boolean typeWithSameNameExists(EntityManager em, SecurityFilter filter, String name) {
		QueryBuilder<InspectionType> nameExistsBuilder = new QueryBuilder<InspectionType>(InspectionType.class, filter);
		
		nameExistsBuilder.addWhere(WhereClauseFactory.create("name", "name", name));
		
		return nameExistsBuilder.entityExists(em);
	}

	public InspectionTypeUniqueNameLoader setName(String name) { 
		this.name = name;
		return this;
	}
	
}
