package com.n4systems.exporting;

import java.util.HashMap;
import java.util.Map;

import com.n4systems.taskscheduling.task.CustomerImportTask;

public class ImportTaskRegistry {
	private static final Map<String, CustomerImportTask> taskMap = new HashMap<String, CustomerImportTask>();
	
	public ImportTaskRegistry() {}
	
	public void register(CustomerImportTask task) {
		taskMap.put(task.getId(), task);
	}
	
	public CustomerImportTask get(String taskId) {
		return taskMap.get(taskId);
	}
	
	public void remove(String taskId) {
		taskMap.remove(taskId);
	}
	
	public void clear() {
		taskMap.clear();
	}
}
