package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.eventschedule.NextEventScheduleLoader;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldIdDateFormatter;

import java.util.TimeZone;

public class NextScheduledEventDateHandler extends WebOutputHandler implements
		DateTimeDefinition {

	public NextScheduledEventDateHandler(TableGenerationContext action) {
		super(action);
	}

	@Override
	public String handleWeb(Long entityId, Object value) {		
		return getNextScheduledEventDate(entityId);
	}
	
	@Override
	public Object handleExcel(Long entityId, Object value) {
		return getNextScheduledEventDate(entityId);
	}

	@Override
	public String getDateFormat() {
		return contextProvider.getDateFormat();
	}

	@Override
	public String getDateTimeFormat() {
		return contextProvider.getDateTimeFormat();
	}

	@Override
	public TimeZone getTimeZone() {
		return contextProvider.getTimeZone();
	}

	private String getNextScheduledEventDate(Long entityId) {
		ThingEvent nextOpenEvent = new NextEventScheduleLoader().setAssetId(entityId).load();
		if (nextOpenEvent != null)
			return new FieldIdDateFormatter(nextOpenEvent.getDueDate(), this, false, false).format();
		else
			return "";
	}

}
