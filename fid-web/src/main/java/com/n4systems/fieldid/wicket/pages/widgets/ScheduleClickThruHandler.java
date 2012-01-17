package com.n4systems.fieldid.wicket.pages.widgets;



// clickthru handler that goes to struts page.
// this is very brittle because if struts URL changes we're in trouble but since this page is being refactored
// in very near future it's only a temporary kludge.  
// this is the starting point for WEB-2628

@SuppressWarnings("serial")
public class ScheduleClickThruHandler extends SimpleClickThruHandler {

	public ScheduleClickThruHandler(Long id) {
		super(id);
    }

	@Override
	public String getUrl() {
		return "/fieldid/scheduleResults.action?wdf="+id;
	}

}
