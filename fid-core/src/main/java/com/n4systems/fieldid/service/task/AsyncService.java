
package com.n4systems.fieldid.service.task;

import org.springframework.scheduling.annotation.Async;

import com.n4systems.fieldid.service.task.AsyncTaskFactory.AsyncTask;

public class AsyncService {
	
	// for running generic tasks.
	@Async	
	public <T> void run(AsyncTask<T> task) {
		task.run();
	}
		
}
