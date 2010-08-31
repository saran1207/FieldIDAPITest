package com.n4systems.exporting;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import com.n4systems.taskscheduling.task.ImportTask;

public class ImportTaskRegistryTest {
	private ImportTaskRegistry registry = new ImportTaskRegistry();
	
	@After
	public void clearRegistry() {
		registry.clear();
	}
	
	@Test
	public void register_get_return_task() {
		ImportTask task = new ImportTask(null, null, null, null, null);
		
		registry.register(task);
		
		assertSame(task, registry.get(task.getId()));
	}
	
	@Test
	public void remove_removes_task() {
		ImportTask task = new ImportTask(null, null, null, null, null);
		registry.register(task);
		registry.remove(task.getId());
		
		assertNull(registry.get(task.getId()));
	}
	
	@Test
	public void clear_removes_all_tasks() {
		ImportTask task = new ImportTask(null, null, null, null, null);
		registry.register(task);
		
		String id1 = task.getId();
		
		task = new ImportTask(null, null, null, null, null);
		registry.register(task);
		
		String id2 = task.getId();
		
		registry.clear();
		
		assertNull(registry.get(id1));
		assertNull(registry.get(id2));
	}
	
	@Test
	public void registry_is_statically_backed() {
		ImportTask task = new ImportTask(null, null, null, null, null);
		registry.register(task);
		
		ImportTaskRegistry registry2 = new ImportTaskRegistry();
		
		assertSame(registry.get(task.getId()), registry2.get(task.getId()));
	}
}
