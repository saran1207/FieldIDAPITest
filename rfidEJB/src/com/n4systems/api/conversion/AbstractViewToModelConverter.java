package com.n4systems.api.conversion;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.model.api.Exportable;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.GlobalIdLoader;

public abstract class AbstractViewToModelConverter<M extends Exportable, V extends ExternalModelView> implements ViewToModelConverter<M, V> {
	private final GlobalIdLoader<M> externalIdLoader;
	protected Transaction transaction;
	
	public AbstractViewToModelConverter(GlobalIdLoader<M> externalIdLoader) {
		this.externalIdLoader = externalIdLoader;
	}
	
	@Override
	public M toModel(V view, Transaction transaction) throws ConversionException {
		this.transaction = transaction;
		
		boolean edit = !view.isNew();
		M model = (edit) ? loadModel(view.getGlobalId()) : createModelBean();
		copyProperties(view, model, edit);
		
		this.transaction = null;
		return model;
	}
	
	private M loadModel(String externalId) throws ConversionException {
		M model = externalIdLoader.setGlobalId(externalId).load(transaction);
		
		if (model == null) {
			throw new ConversionException("No model found for external id [" + externalId + "]");
		}
		
		return model;
	}
	
	protected abstract M createModelBean();
	public abstract void copyProperties(V from, M to, boolean isEdit) throws ConversionException;
	
}
