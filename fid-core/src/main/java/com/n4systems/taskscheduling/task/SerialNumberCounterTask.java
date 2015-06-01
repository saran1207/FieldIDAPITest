package com.n4systems.taskscheduling.task;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.n4systems.fieldid.service.asset.AssetIdentifierService;
import rfid.ejb.entity.IdentifierCounter;

import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ServiceLocator;


/**
 * This will check each counter, every day, to see if it is time to reset it
 * 
 * @author Jesse Miller
 */

@Deprecated  //This task is no longer necessary since we now check if the counter should be reset we generate an id.
public class SerialNumberCounterTask extends ScheduledTask {
	
	public SerialNumberCounterTask() {
	    super(60 * 5, TimeUnit.SECONDS);
    }

	@Override
    protected void runTask() throws Exception {
		Collection<IdentifierCounter> identifierCounters = null;
		AssetIdentifierService assetIdentifierService = ServiceLocator.getAssetIdentifierService();

		identifierCounters = assetIdentifierService.getIdentifierCounters();

		if (identifierCounters != null) {
			for (IdentifierCounter identifierCounter : identifierCounters) {

				// Check each serial number counter to see if its reset time has
				// passed.
				Calendar nextReset = Calendar.getInstance();
				nextReset.setTime(identifierCounter.getLastReset());
				//nextReset.add(Calendar.DATE, identifierCounter.getDaysToReset().intValue());

				Calendar today = Calendar.getInstance();
				if (today.after(nextReset) || today.equals(nextReset)) {
					// if the reset time has passed or is now, we set the last
					// reset to when it was
					// supposed to have been reset. and set the counter to 1
					identifierCounter.setLastReset(new Date(nextReset.getTimeInMillis()));
					identifierCounter.setCounter(1L);
					assetIdentifierService.updateIdentifierCounter(identifierCounter);
				}
			}
		}
    }

}
