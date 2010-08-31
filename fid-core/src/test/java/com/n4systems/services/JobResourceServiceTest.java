package com.n4systems.services;

import static com.n4systems.model.builders.JobBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;


import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.EmployeeAlreadyAttachedException;
import com.n4systems.exceptions.NonEmployeeUserException;
import com.n4systems.model.Project;
import com.n4systems.model.user.User;

public class JobResourceServiceTest {
	private final User modifier = aUser().build();

	@Test
	public void should_attach_first_employee_resource() throws NonEmployeeUserException, EmployeeAlreadyAttachedException {
		Project job = aJob().build();
		User employee = anEmployee().build();
	
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.reattchAndFetch(job, "resources")).andReturn(job);
		expect(mockPersistenceManager.update(job, modifier)).andReturn(job);
		replay(mockPersistenceManager);
				
		JobResourceService sut = new JobResourceService(job, mockPersistenceManager, modifier);
		
		assertEquals(1, sut.attach(employee));
		verify(mockPersistenceManager);
	}
	
	@Test(expected=NonEmployeeUserException.class)
	public void should_stop_attachment_non_employee() throws NonEmployeeUserException, EmployeeAlreadyAttachedException {
		Project job = aJob().build();
		User customer = aCustomerUser().build();
		JobResourceService sut = new JobResourceService(job, null, modifier);
		
		sut.attach(customer);
	}
	
	@Test(expected=EmployeeAlreadyAttachedException.class)
	public void should_stop_attaching_the_same_employee_twice() throws NonEmployeeUserException, EmployeeAlreadyAttachedException {
		User employeeAssigned = anEmployee().build();
		User employee = anEmployee().build();
		employee.setId(employeeAssigned.getId());
		Project job = aJob().withResources(employeeAssigned).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.reattchAndFetch(job, "resources")).andReturn(job);
		replay(mockPersistenceManager);
		
		JobResourceService sut = new JobResourceService(job, mockPersistenceManager, modifier);

		sut.attach(employee);
		verify(mockPersistenceManager);
	}
	
	
	@Test
	public void should_remove_the_employee_from_job() throws NonEmployeeUserException {
		User employee = anEmployee().build();
		Project job = aJob().withResources(employee).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.reattchAndFetch(job, "resources")).andReturn(job);
		expect(mockPersistenceManager.update(job, modifier)).andReturn(job);
		replay(mockPersistenceManager);
		
		JobResourceService sut = new JobResourceService(job, mockPersistenceManager, modifier);

		sut.dettach(employee);
		verify(mockPersistenceManager);
	}
	
	@Test
	public void should_not_save_the_removal_of_an_employee_not_assigned_to_job() throws NonEmployeeUserException {
		User employee = anEmployee().build();
		Project job = aJob().build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.reattchAndFetch(job, "resources")).andReturn(job);
		replay(mockPersistenceManager);
		
		JobResourceService sut = new JobResourceService(job, mockPersistenceManager, modifier);
		
		sut.dettach(employee);
		verify(mockPersistenceManager);
	}
	
	@Test(expected=NonEmployeeUserException.class)
	public void should_stop_dettachment_of_a_non_employee() throws NonEmployeeUserException {
		Project job = aJob().build();
		User customer = aCustomerUser().build();
		JobResourceService sut = new JobResourceService(job, null, modifier);
		
		sut.dettach(customer);
	}
}
