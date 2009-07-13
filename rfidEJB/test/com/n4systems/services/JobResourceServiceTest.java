package com.n4systems.services;

import static com.n4systems.model.builders.JobBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.EmployeeAlreadyAttachedException;
import com.n4systems.exceptions.NonEmployeeUserException;
import com.n4systems.model.Project;

public class JobResourceServiceTest {

	
	private final UserBean modifier = aUser().build();

	@Test public void should_attach_first_employee_resource() {
		Project job = aJob().build();
		UserBean employee = anEmployee().build();
	
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.reattchAndFetch(job, "resources")).andReturn(job);
		expect(mockPersistenceManager.update(job, modifier)).andReturn(job);
		replay(mockPersistenceManager);
				
		JobResourceService sut = new JobResourceService(job, mockPersistenceManager, modifier);
		
		try {
			assertEquals(1, sut.attach(employee));
		} catch (Exception e) {
			fail("exception thrown");
		}
		verify(mockPersistenceManager);
		
	}
	
	
	@Test public void should_stop_attachment_non_employee() {
		Project job = aJob().build();
		UserBean customer = aCustomerUser().build();
		JobResourceService sut = new JobResourceService(job, null, modifier);
		
		boolean exceptionThrown = false;
		try {
			sut.attach(customer);
		} catch (NonEmployeeUserException e) {
			exceptionThrown = true;
		} catch (EmployeeAlreadyAttachedException e) {
			fail("EmployeeAlreadyAttached was thrown.");
		}
		
		assertTrue("NonEmployeeUserException exception should have been thrown", exceptionThrown);
	}
	
	@Test public void should_stop_attaching_the_same_employee_twice() {
		UserBean employeeAssigned = anEmployee().build();
		UserBean employee = anEmployee().build();
		employee.setUniqueID(employeeAssigned.getId());
		Project job = aJob().withResources(employeeAssigned).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.reattchAndFetch(job, "resources")).andReturn(job);
		replay(mockPersistenceManager);
		
		JobResourceService sut = new JobResourceService(job, mockPersistenceManager, modifier);
		
		boolean exceptionThrown = false;
		try {
			sut.attach(employee);
		} catch (EmployeeAlreadyAttachedException e) {
			exceptionThrown = true;
		} catch (NonEmployeeUserException e) {
			fail("NonEmployeeUserException was thrown.");
		}
		
		assertTrue("EmployeeAlreadyAttached exception should have been thrown", exceptionThrown);
		verify(mockPersistenceManager);
	}
	
	
	@Test public void should_remove_the_employee_from_job() {
		UserBean employee = anEmployee().build();
		Project job = aJob().withResources(employee).build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.reattchAndFetch(job, "resources")).andReturn(job);
		expect(mockPersistenceManager.update(job, modifier)).andReturn(job);
		replay(mockPersistenceManager);
		
		JobResourceService sut = new JobResourceService(job, mockPersistenceManager, modifier);
		
		try {
			sut.dettach(employee);
		} catch (NonEmployeeUserException e) {
			fail("NonEmployeeUserException was thrown.");
		}
		
		verify(mockPersistenceManager);
	}
	
	@Test public void should_not_save_the_removal_of_an_employee_not_assigned_to_job() {
		UserBean employee = anEmployee().build();
		Project job = aJob().build();
		
		PersistenceManager mockPersistenceManager = createMock(PersistenceManager.class);
		expect(mockPersistenceManager.reattchAndFetch(job, "resources")).andReturn(job);
		replay(mockPersistenceManager);
		
		JobResourceService sut = new JobResourceService(job, mockPersistenceManager, modifier);
		
		try {
			sut.dettach(employee);
		} catch (NonEmployeeUserException e) {
			fail("NonEmployeeUserException was thrown.");
		}
		
		verify(mockPersistenceManager);
	}
	
	@Test public void should_stop_dettachment_of_a_non_employee() {
		Project job = aJob().build();
		UserBean customer = aCustomerUser().build();
		JobResourceService sut = new JobResourceService(job, null, modifier);
		
		boolean exceptionThrown = false;
		try {
			sut.dettach(customer);
		} catch (NonEmployeeUserException e) {
			exceptionThrown = true;
		} 
		
		assertTrue("NonEmployeeUserException exception should have been thrown", exceptionThrown);
	}
}
