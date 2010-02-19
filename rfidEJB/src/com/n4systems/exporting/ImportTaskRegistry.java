package com.n4systems.exporting;

import java.util.HashMap;
import java.util.Map;

public class ImportTaskRegistry {
	private static final Map<String, ImportTask> taskMap = new HashMap<String, ImportTask>();
	
	public ImportTaskRegistry() {}
	
	public void register(ImportTask task) {
		taskMap.put(task.getId(), task);
	}
	
	public ImportTask get(String taskId) {
		return taskMap.get(taskId);
	}
}
