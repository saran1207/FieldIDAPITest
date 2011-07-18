package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.fieldid.utils.WebContextProvider;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.eventschedule.NextEventScheduleLoader;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldidDateFormatter;

import java.util.TimeZone;

public class NextScheduledEventDateHandler extends WebOutputHandler implements
		DateTimeDefinition {

	protected NextScheduledEventDateHandler(WebContextProvider action) {
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
		return contextProvider.getSessionUser().getDateFormat();
	}

	@Override
	public String getDateTimeFormat() {
		return contextProvider.getSessionUser().getDateTimeFormat();
	}

	@Override
	public TimeZone getTimeZone() {
		return contextProvider.getSessionUser().getTimeZone();
	}

	private String getNextScheduledEventDate(Long entityId) {
		EventSchedule nextScheduledEvent = new NextEventScheduleLoader().setAssetId(entityId).load();
		if(nextScheduledEvent != null)
			return new FieldidDateFormatter(nextScheduledEvent.getNextDate(), this, false, false).format();
		else
			return "";
	}

}
