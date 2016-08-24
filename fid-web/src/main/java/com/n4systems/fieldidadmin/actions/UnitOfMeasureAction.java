package com.n4systems.fieldidadmin.actions;

import com.n4systems.model.UnitOfMeasure;

import java.util.Collection;

public class UnitOfMeasureAction extends AbstractAdminAction {
	private static final long serialVersionUID = 1L;

	
	private Long id;
	private UnitOfMeasure unitOfMeasure;
	private Collection<UnitOfMeasure> unitOfMeasures;

	public String doList() {
		unitOfMeasures = persistenceEJBContainer.findAll( UnitOfMeasure.class );

		return SUCCESS;
	}

	public String doFormInput() {
		if (id != null) {
			unitOfMeasure = persistenceEJBContainer.find( UnitOfMeasure.class, id );
		}

		unitOfMeasures = persistenceEJBContainer.findAll( UnitOfMeasure.class );

		return INPUT;
	}

	public String doSave() {
		if (id != null) {
			unitOfMeasure.setId(id);
			persistenceEJBContainer.update( unitOfMeasure );
		} else {
			persistenceEJBContainer.save( unitOfMeasure );
		}

		return SUCCESS;
	}

	

	

	public Collection<UnitOfMeasure> getUnitOfMeasures() {
		return unitOfMeasures;
	}

	public UnitOfMeasure getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure( UnitOfMeasure unitOfMeasure ) {
		this.unitOfMeasure = unitOfMeasure;
	}


	
}
