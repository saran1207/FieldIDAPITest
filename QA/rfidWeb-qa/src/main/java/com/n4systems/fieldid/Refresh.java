package com.n4systems.fieldid;

import watij.runtime.ie.IE;
import com.jniwrapper.win32.shdocvw.IWebBrowser2;

public class Refresh extends Thread implements Runnable {
	IE ie;
	static final int TIMEOUT = 180;	// number of seconds before Watij times out
	private int count = 0;			// defaults to not running immediately
	private int count_max = 100;	// shouldn't queue up more than 100 instances
	boolean running;

	public Refresh(String name, IE ie) {
		super(name);
		this.ie = ie;
		setDaemon(true);
	}
	
	public synchronized void run() {
		try {
			final IWebBrowser2 wb2 = ie.iWebBrowser2();
			int cycle = 0;
			int max = TIMEOUT / 3;
			String previous, current;
			while(running) {
				previous = wb2.getStatusText().toString();
				wait(1000);
				current = wb2.getStatusText().toString();
				if(count > 0 && current.equals(previous)) {
					cycle++;
				} else {
					cycle = 0;
				}
				if(count > 0 && cycle > max) {
					wb2.refresh();
					cycle = 0;
				}
				System.err.println(cycle);
			}
		} catch (Exception e) {
		} finally {
			running = false;
		}
	}
	
	public synchronized void enable() {
		System.err.println("enable");
		if(count < count_max)
			count++;
	}
	
	public synchronized void disable() {
		System.err.println("disable");
		if(count > 0)
			count--;
	}
	
	public synchronized void quit() {
		System.err.println("quit");
		running = false;
	}
	
	public synchronized boolean isRunning() {
		return running;
	}
	
	public synchronized int getCount() {
		return count;
	}
	
	public synchronized void start() {
		super.start();
		System.err.println("start");
		running = true;
	}
}
