package com.n4systems.api.conversion;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Exportable;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.GlobalIdLoader;
import com.n4systems.services.TenantCache;

public abstract class AbstractViewModelConverter<M extends Exportable, V extends ExternalModelView> implements ViewModelConverter<M, V> {
	protected final SecurityFilter filter;
	private final GlobalIdLoader<M> externalIdLoader;
	private final Class<M> modelClass;
	private final Class<V> viewClass;
	
	public AbstractViewModelConverter(SecurityFilter filter, Class<M> modelClass, Class<V> viewClass) {
		this(new GlobalIdLoader<M>(filter, modelClass), filter, modelClass, viewClass);
	}

	public AbstractViewModelConverter(GlobalIdLoader<M> externalIdLoader, SecurityFilter filter, Class<M> modelClass, Class<V> viewClass) {
		this.filter = filter;
		this.externalIdLoader = externalIdLoader;
		this.modelClass = modelClass;
		this.viewClass = viewClass;
	}
	
	@Override
	public V toView(M model) throws ConversionException {
		V view = createViewBean();
		copyProperties(model, view);
		return view;
	}
	
	@Override
	public M toModel(V view) throws ConversionException {
		boolean edit = !view.isNew();
		M model = (edit) ? loadModel(view.getGlobalId()) : createModelBean();
		copyProperties(view, model, edit);
		return model;
	}
	
	private M loadModel(String externalId) throws ConversionException {
		M model = externalIdLoader.setGlobalId(externalId).load();
		
		if (model == null) {
			throw new ConversionException("No [" + modelClass.getName() + "] found for external id [" + externalId + "]");
		}
		
		return model;
	}
	
	private M createModelBean() throws ConversionException {
		try {
			M model = modelClass.newInstance();
			
			// force the tenant directly from the security filter
			model.setTenant(findTenant());
			
			return model;
		} catch (Exception e) {
			throw new ConversionException("Could not create model class [" + modelClass.getName() + "]", e); 
		}
	}
	
	private V createViewBean() throws ConversionException {
		try {
			return viewClass.newInstance();
		} catch (Exception e) {
			throw new ConversionException("Could not create view class [" + modelClass.getName() + "]", e); 
		}
	}
	
	private Tenant findTenant() {
		return TenantCache.getInstance().findTenant(filter.getTenantId());
	}
	
	public abstract void copyProperties(V from, M to, boolean isEdit) throws ConversionException;
	public abstract void copyProperties(M from, V to) throws ConversionException;
	
}
