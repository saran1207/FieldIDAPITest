package com.n4systems.exporting;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import com.n4systems.taskscheduling.task.CustomerImportTask;

public class ImportTaskRegistryTest {
	private ImportTaskRegistry registry = new ImportTaskRegistry();
	
	@After
	public void clearRegistry() {
		registry.clear();
	}
	
	@Test
	public void register_get_return_task() {
		CustomerImportTask task = new CustomerImportTask(null, null, null);
		
		registry.register(task);
		
		assertSame(task, registry.get(task.getId()));
	}
	
	@Test
	public void remove_removes_task() {
		CustomerImportTask task = new CustomerImportTask(null, null, null);
		registry.register(task);
		registry.remove(task.getId());
		
		assertNull(registry.get(task.getId()));
	}
	
	@Test
	public void clear_removes_all_tasks() {
		CustomerImportTask task = new CustomerImportTask(null, null, null);
		registry.register(task);
		
		String id1 = task.getId();
		
		task = new CustomerImportTask(null, null, null);
		registry.register(task);
		
		String id2 = task.getId();
		
		registry.clear();
		
		assertNull(registry.get(id1));
		assertNull(registry.get(id2));
	}
	
	@Test
	public void registry_is_statically_backed() {
		CustomerImportTask task = new CustomerImportTask(null, null, null);
		registry.register(task);
		
		ImportTaskRegistry registry2 = new ImportTaskRegistry();
		
		assertSame(registry.get(task.getId()), registry2.get(task.getId()));
	}
}
