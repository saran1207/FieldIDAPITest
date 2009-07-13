package com.n4systems.services;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.EmployeeAlreadyAttachedException;
import com.n4systems.exceptions.NonEmployeeUserException;
import com.n4systems.model.Project;

public class JobResourceService {

	private Project job;
	private PersistenceManager persistenceManager;
	private UserBean modifier;
	
	
	public JobResourceService(Project job, PersistenceManager persistenceManager, UserBean modifier) {
		super();
		this.persistenceManager = persistenceManager;
		this.job = job;
		this.modifier = modifier;
	}
	
	public int attach(UserBean employee) throws NonEmployeeUserException, EmployeeAlreadyAttachedException{
		if (!employee.isEmployee()) {
			throw new NonEmployeeUserException("a customer user attempted to be assigned");
		}
		
		persistenceManager.reattchAndFetch(job, "resources");
				
		if (!job.getResources().add(employee)) {
			throw new EmployeeAlreadyAttachedException("the employee is already in the set");
		}
		
		job = persistenceManager.update(job, modifier);
		return job.getResources().size();
	}
	
	
	public int dettach(UserBean employee) throws NonEmployeeUserException {
		if (!employee.isEmployee()) {
			throw new NonEmployeeUserException("a customer user attempted to be detached");
		}
		
		persistenceManager.reattchAndFetch(job, "resources");
		
		if (job.getResources().remove(employee)) {
			job = persistenceManager.update(job, modifier);
		}
		
		return job.getResources().size();
	}
	

	
	
}
