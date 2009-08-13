package com.n4systems.fieldid;

import watij.runtime.ie.IE;
import com.jniwrapper.win32.shdocvw.IWebBrowser2;

public class Refresh extends Thread implements Runnable {
	IE ie;
	static final int TIMEOUT = 180;	// number of seconds before Watij times out
	boolean running;

	public Refresh(String name, IE ie) {
		super(name);
		this.ie = ie;
		setDaemon(true);
	}
	
	public synchronized void run() {
		try {
			running = true;
			final IWebBrowser2 wb2 = ie.iWebBrowser2();
			int cycle = 0;
			int max = TIMEOUT / 3;
			while(running) {
				String previous = wb2.getStatusText().toString();
				sleep(1000);
				String current = wb2.getStatusText().toString();
				if(current.equals(previous)) {
					cycle++;
				} else {
					cycle = 0;
				}
				if(running && cycle > max) {
					wb2.refresh();
					cycle = 0;
				}
			}
		} catch (Exception e) {
		} finally {
			running = false;
		}
	}
	
	public void quit() {
		running = false;
	}
}
