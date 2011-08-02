package com.n4systems.taskscheduling.task;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rfid.ejb.entity.IdentifierCounterBean;

import com.n4systems.ejb.legacy.IdentifierCounter;
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
		Collection<IdentifierCounterBean> identifierCounters = null;
		IdentifierCounter identifierCounterManager = ServiceLocator.getIdentifierCounter();

		identifierCounters = identifierCounterManager.getIdentifierCounters();

		if (identifierCounters != null) {
			for (IdentifierCounterBean identifierCounter : identifierCounters) {

				// Check each serial number counter to see if its reset time has
				// passed.
				Calendar nextReset = Calendar.getInstance();
				nextReset.setTime(identifierCounter.getLastReset());
				nextReset.add(Calendar.DATE, identifierCounter.getDaysToReset().intValue());

				Calendar today = Calendar.getInstance();
				if (today.after(nextReset) || today.equals(nextReset)) {
					// if the reset time has passed or is now, we set the last
					// reset to when it was
					// supposed to have been reset. and set the counter to 1
					identifierCounter.setLastReset(new Date(nextReset.getTimeInMillis()));
					identifierCounter.setCounter(1L);
					identifierCounterManager.updateIdentifierCounter(identifierCounter);
				}
			}
		}
    }

}
