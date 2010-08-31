package com.n4systems.taskscheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class TaskPool {
	private final String name;
	private final int size;
	private final ThreadPoolExecutor executor;
	
	private List<Class<? extends Runnable>> executableTasks = new ArrayList<Class<? extends Runnable>>();
	
	public TaskPool(String name, int size) {
		this.name = name;
		this.size = size;
		this.executor =  ThreadPoolExecutorFactory.createUnboundExecutor(name, size, new TaskThreadFactory(name));
	}

	@Override
    public String toString() {
		StringBuilder info = new StringBuilder("TaskPool: ");
		
		info.append(name);
		info.append(", size=").append(size);
		info.append(", active=").append(executor.getActiveCount());
		info.append(", queued=").append(executor.getQueue().size());
		info.append(", completed=").append(executor.getCompletedTaskCount());
		
	    return info.toString();
    }
	
	public boolean canExecute(Class<? extends Runnable> runnalbe) {
		return executableTasks.contains(runnalbe);
	}
	
	public String getName() {
    	return name;
    }

	public int getSize() {
    	return size;
    }

	public List<Class<? extends Runnable>> getExecutableTasks() {
    	return executableTasks;
    }

	public void setExecutableTasks(List<Class<? extends Runnable>> taskClasses) {
    	this.executableTasks = taskClasses;
    }
	
	public ThreadPoolExecutor getExecutor() {
		return executor;
	}
}
