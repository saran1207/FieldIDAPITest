package com.n4systems.fieldid.selenium.util;

import org.junit.Assert;

public class ConditionWaiter {
	
	private final Predicate pred;

	public ConditionWaiter(Predicate pred) {
		this.pred = pred;
	}
	
	public void run() {
		run("Condition never evaluated true!");
	}
	
	public void run(String errorMessage) {
		run(errorMessage, 30);
	}

    public void run(String errorMessage, int timeoutInSeconds) {
        run(errorMessage, timeoutInSeconds, 200);
    }

	public void run(String errorOnTimeout, int timeoutInSeconds, long pollIntervalMillis) {
		long startTime = System.currentTimeMillis();
		
		while(true) { 
			if (pred.evaluate()) {
				return;
			}
			
			long elapsedTime = System.currentTimeMillis() - startTime;
			
			if (elapsedTime > (timeoutInSeconds * 1000)) {
				Assert.fail(errorOnTimeout);
			}
			
			try { Thread.sleep(pollIntervalMillis); } catch (InterruptedException e) { }
		}
	}
	

}
