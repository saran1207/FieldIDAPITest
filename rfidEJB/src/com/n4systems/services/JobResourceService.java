package com.n4systems.services;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.EmployeeAlreadyAttachedException;
import com.n4systems.exceptions.NonEmployeeUserException;
import com.n4systems.model.Project;
import com.n4systems.model.user.User;

public class JobResourceService {

	private Project job;
	private PersistenceManager persistenceManager;
	private User modifier;
	
	
	public JobResourceService(Project job, PersistenceManager persistenceManager, User modifier) {
		super();
		this.persistenceManager = persistenceManager;
		this.job = job;
		this.modifier = modifier;
	}
	
	public int attach(User employee) throws NonEmployeeUserException, EmployeeAlreadyAttachedException{
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
	
	
	public int dettach(User employee) throws NonEmployeeUserException {
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
