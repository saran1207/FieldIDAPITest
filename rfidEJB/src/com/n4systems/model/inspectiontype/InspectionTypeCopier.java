package com.n4systems.model.inspectiontype;

import com.n4systems.model.Copier;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;

public class InspectionTypeCopier implements Copier<InspectionType> {
	private final Cleaner<InspectionType> typeCleaner;
	private final FilteredIdLoader<InspectionType> typeLoader;
	private final InspectionTypeSaver typeSaver;
	private final InspectionTypeUniqueNameLoader typeNameLoader;
	
	public InspectionTypeCopier(Cleaner<InspectionType> typeCleaner, FilteredIdLoader<InspectionType> typeLoader, InspectionTypeSaver typeSaver, InspectionTypeUniqueNameLoader typeNameLoader) {
		this.typeCleaner = typeCleaner;
		this.typeLoader = typeLoader;
		this.typeSaver = typeSaver;
		this.typeNameLoader = typeNameLoader;
	}
	
	protected InspectionTypeCopier(Tenant tenant, SecurityFilter filter) {
		this(new InspectionTypeCleaner(tenant), new FilteredIdLoader<InspectionType>(filter, InspectionType.class), new InspectionTypeSaver(), new InspectionTypeUniqueNameLoader(filter));
	}
	
	public InspectionTypeCopier(Tenant tenant) {
		this(tenant, new TenantOnlySecurityFilter(tenant));
	}
	
	@Override
	public InspectionType copy(Long id) {
		// note we don't maintain a transaction here as we don't want our type to be in managed scope.
		// otherwise we would be editing the model as we copied
		InspectionType type = typeLoader.setId(id).setPostFetchFields("sections", "supportedProofTests", "infoFieldNames").load();
		
		typeCleaner.clean(type);

		String newName = typeNameLoader.setName(type.getName()).load();
		type.setName(newName);
		
		typeSaver.save(type);
		
		return type;
	}
	
}
