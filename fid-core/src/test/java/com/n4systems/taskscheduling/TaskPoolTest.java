package com.n4systems.taskscheduling;

import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TaskPoolTest {

	@Test
	public void test_initialize_executor() {
		TaskPool pool = new TaskPool("name", 5);
		
		assertEquals(5, pool.getExecutor().getCorePoolSize());
		assertEquals(LinkedBlockingQueue.class, pool.getExecutor().getQueue().getClass());
	}

	@Test
	public void empty_class_list_ok() {
		TaskPool pool = new TaskPool("name", 0);
		
		assertFalse(pool.canExecute(Thread.class));
		
	}
}
