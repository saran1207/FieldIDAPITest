package com.n4systems.taskscheduling;

import static org.junit.Assert.*;

import com.n4systems.taskscheduling.task.PrintAllInspectionCertificatesTask;
import com.n4systems.taskscheduling.task.PrintInspectionSummaryReportTask;

import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

public class TaskPoolTest {

	@Test
	public void test_initialize_executor() {
		TaskPool pool = new TaskPool("name", 5);
		
		assertEquals(5, pool.getExecutor().getCorePoolSize());
		assertEquals(LinkedBlockingQueue.class, pool.getExecutor().getQueue().getClass());
	}
	
	@Test
	public void test_can_execute() {
		TaskPool pool = new TaskPool("name", 0);
		
		pool.getExecutableTasks().add(PrintAllInspectionCertificatesTask.class);
		pool.getExecutableTasks().add(PrintInspectionSummaryReportTask.class);
		
		assertTrue(pool.canExecute(PrintAllInspectionCertificatesTask.class));
		assertTrue(pool.canExecute(PrintInspectionSummaryReportTask.class));
		
		assertFalse(pool.canExecute(Thread.class));
	}

	@Test
	public void empty_class_list_ok() {
		TaskPool pool = new TaskPool("name", 0);
		
		assertFalse(pool.canExecute(Thread.class));
		
	}
}
