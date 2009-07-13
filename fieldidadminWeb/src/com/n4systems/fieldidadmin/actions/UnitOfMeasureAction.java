package com.n4systems.fieldidadmin.actions;

import java.util.Collection;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.UnitOfMeasure;
import com.opensymphony.xwork2.ActionSupport;

public class UnitOfMeasureAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	private PersistenceManager persistenceManager;
	
	private Long id;
	private UnitOfMeasure unitOfMeasure;
	private Collection<UnitOfMeasure> unitOfMeasures;

	public String doList() {
		unitOfMeasures = persistenceManager.findAll( UnitOfMeasure.class );

		return SUCCESS;
	}

	public String doFormInput() {
		if (id != null) {
			unitOfMeasure = persistenceManager.find( UnitOfMeasure.class, id );
		}

		unitOfMeasures = persistenceManager.findAll( UnitOfMeasure.class );

		return INPUT;
	}

	public String doSave() {
		if (id != null) {
			unitOfMeasure.setId(id);
			persistenceManager.update( unitOfMeasure );
		} else {
			persistenceManager.save( unitOfMeasure );
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

	public void setPersistenceManager( PersistenceManager persistenceManager ) {
		this.persistenceManager = persistenceManager;
	}

	
}
