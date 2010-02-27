package com.n4systems.taskscheduling.task;


import static org.easymock.EasyMock.*;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.exporting.ImportException;
import com.n4systems.exporting.Importer;
import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.testutils.DummyTransaction;

public class ImportTaskTest {

	@Test
	public void constructor_generates_id() {
		ImportTask task = new ImportTask(null, null, null, null, null);
		
		assertNotNull(task.getId());
		assertEquals(ImportTask.Status.PENDING, task.getStatus());
		assertFalse(task.isCompleted());
	}
	
	@Test
	public void test_run_success() throws ImportException {
		Importer importer = createMock(Importer.class);
		Notifier notifier = createMock(Notifier.class);
		TransactionManager transMan = createMock(TransactionManager.class);
		ImportSuccessNotification success = createMock(ImportSuccessNotification.class);
		ImportFailureNotification failure = createMock(ImportFailureNotification.class);
		
		Transaction trans = new DummyTransaction();
		
		ImportTask task = new ImportTask(transMan, notifier, importer, success, failure);
		
		expect(transMan.startTransaction()).andReturn(trans);
		
		expect(importer.runImport(trans)).andReturn(5);
		
		transMan.finishTransaction(trans);
		
		success.setTotalImported(5);
		expect(notifier.notify(success)).andReturn(true);
		
		replay(transMan);
		replay(importer);
		replay(notifier);
		replay(success);
		replay(failure);
		
		task.run();
		
		assertTrue(task.isCompleted());
		assertEquals(ImportTask.Status.SUCCESSFUL, task.getStatus());
		
		verify(transMan);
		verify(importer);
		verify(notifier);
		verify(success);
		verify(failure);
	}
	
	@Test
	public void test_run_failure() throws ImportException {
		Importer importer = createMock(Importer.class);
		Notifier notifier = createMock(Notifier.class);
		TransactionManager transMan = createMock(TransactionManager.class);
		ImportSuccessNotification success = createMock(ImportSuccessNotification.class);
		ImportFailureNotification failure = createMock(ImportFailureNotification.class);
		
		Transaction trans = new DummyTransaction();
		
		ImportTask task = new ImportTask(transMan, notifier, importer, success, failure);
		
		expect(transMan.startTransaction()).andReturn(trans);
		
		expect(importer.runImport(trans)).andThrow(new ImportException());
		
		transMan.rollbackTransaction(trans);
		
		expect(notifier.notify(failure)).andReturn(true);
				
		replay(transMan);
		replay(importer);
		replay(notifier);
		replay(success);
		replay(failure);
		
		task.run();
		
		verify(transMan);
		verify(importer);
		verify(notifier);
		verify(success);
		verify(failure);
	}
	
	@Test
	public void get_current_and_total_rows_passes_on_to_importer() throws ImportException {
		Importer importer = createMock(Importer.class);

		ImportTask task = new ImportTask(createMock(TransactionManager.class), createMock(Notifier.class), importer, createMock(ImportSuccessNotification.class), createMock(ImportFailureNotification.class));
		
		expect(importer.getCurrentRow()).andReturn(5);
		expect(importer.getTotalRows()).andReturn(10);
		
		replay(importer);
		
		assertEquals(5, task.getCurrentRow());
		assertEquals(10, task.getTotalRows());
		
		verify(importer);
	}
}
