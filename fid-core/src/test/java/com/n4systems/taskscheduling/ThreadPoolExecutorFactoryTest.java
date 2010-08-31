package com.n4systems.taskscheduling;

import static org.junit.Assert.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.Test;

public class ThreadPoolExecutorFactoryTest {

	@Test
	public void test_create_not_null() {
		assertNotNull(ThreadPoolExecutorFactory.createUnboundExecutor("", 0, Executors.defaultThreadFactory()));
		
		assertNotNull(ThreadPoolExecutorFactory.createUnboundExecutor("", 10, Executors.defaultThreadFactory()));
	}
	
	@Test
	public void test_config() {
		int size = 10;
		
		ThreadPoolExecutor exec = ThreadPoolExecutorFactory.createUnboundExecutor("", size, Executors.defaultThreadFactory());
		
		assertEquals(size, exec.getCorePoolSize());
		
		assertEquals(ThreadPoolExecutorFactory.POOL_MAX_SIZE, exec.getMaximumPoolSize());
		
		assertEquals(ThreadPoolExecutorFactory.POOL_KEEP_ALIVE_TIME, exec.getKeepAliveTime(ThreadPoolExecutorFactory.POOL_KEEP_ALIVE_UNIT));

		assertNotNull(exec.getQueue());
		
	}

}
