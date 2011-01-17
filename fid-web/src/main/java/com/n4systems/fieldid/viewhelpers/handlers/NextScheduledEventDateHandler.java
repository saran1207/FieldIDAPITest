package com.n4systems.fieldid.viewhelpers.handlers;

import java.util.TimeZone;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.eventschedule.NextEventScheduleLoader;
import com.n4systems.util.DateTimeDefinition;
import com.n4systems.util.FieldidDateFormatter;

public class NextScheduledEventDateHandler extends WebOutputHandler implements
		DateTimeDefinition {

	protected NextScheduledEventDateHandler(AbstractAction action) {
		super(action);
	}

	@Override
	public String handleWeb(Long entityId, Object value) {		
		EventSchedule nextScheduledEvent = getNextScheduledEvent(entityId);
		if(nextScheduledEvent != null)
			return new FieldidDateFormatter(nextScheduledEvent.getNextDate(), this, false, false).format();
		else
			return "";
	}

	private EventSchedule getNextScheduledEvent(Long entityId) {
		NextEventScheduleLoader loader = new NextEventScheduleLoader();
		loader.setAssetId(entityId);
		return loader.load();
	}
	
	@Override
	public String getDateFormat() {
		return action.getSessionUser().getDateFormat();
	}

	@Override
	public String getDateTimeFormat() {
		return action.getSessionUser().getDateTimeFormat();
	}

	@Override
	public TimeZone getTimeZone() {
		return action.getSessionUser().getTimeZone();
	}

	@Override
	public Object handleExcel(Long entityId, Object value) {
		return value;
	}

}
