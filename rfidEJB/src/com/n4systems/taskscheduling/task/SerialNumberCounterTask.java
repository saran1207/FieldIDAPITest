package com.n4systems.taskscheduling.task;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rfid.ejb.entity.SerialNumberCounterBean;
import rfid.ejb.session.SerialNumberCounter;

import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;


/**
 * This will check each counter, every day, to see if it is time to reset it
 * 
 * @author Jesse Miller
 */
public class SerialNumberCounterTask extends ScheduledTask {
	
	public SerialNumberCounterTask() {
	    super(60 * 5, TimeUnit.SECONDS);
    }

	@Override
    protected void runTask() throws Exception {
		Collection<SerialNumberCounterBean> serialNumberCounters = null;
		SerialNumberCounter serialNumberCounterManager = ServiceLocator.getSerialNumberCounter();

		serialNumberCounters = serialNumberCounterManager.getSerialNumberCounters();

		if (serialNumberCounters != null) {
			for (SerialNumberCounterBean serialNumberCounter : serialNumberCounters) {

				// Check each serial number counter to see if its reset time has
				// passed.
				Calendar nextReset = Calendar.getInstance();
				nextReset.setTime(serialNumberCounter.getLastReset());
				nextReset.add(Calendar.DATE, serialNumberCounter.getDaysToReset().intValue());

				Calendar today = Calendar.getInstance();
				if (today.after(nextReset) || today.equals(nextReset)) {
					// if the reset time has passed or is now, we set the last
					// reset to when it was
					// supposed to have been reset. and set the counter to 1
					serialNumberCounter.setLastReset(new Date(nextReset.getTimeInMillis()));
					serialNumberCounter.setCounter(1L);
					serialNumberCounterManager.updateSerialNumberCounter(serialNumberCounter);
				}
			}
		}
    }

}
