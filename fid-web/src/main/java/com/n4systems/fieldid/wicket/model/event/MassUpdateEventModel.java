package com.n4systems.fieldid.wicket.model.event;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.n4systems.model.Event;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.user.User;
import com.n4systems.util.DateHelper;

public class MassUpdateEventModel implements Serializable {

    private Event event;

    private Map<String, Boolean> select;

    public MassUpdateEventModel() {
        event = new Event();
        select = new HashMap<String, Boolean>();
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Map<String, Boolean> getSelect() {
        return select;
    }

    public void setSelect(Map<String, Boolean> select) {
        this.select = select;
    }
    
    public User getAssignedTo() {
    	return event.getAssignedTo() != null ? event.getAssignedTo().getAssignedUser() : null;
    }
    
    public void setAssignedTo(User user) {
    	event.setAssignedTo(AssignedToUpdate.assignAssetToUser(user));
    }
    
   //TODO This needs be revised once WEB-2836 is completed
    public void convertDatePerformedToUTC(TimeZone timeZone) {
    	if(event.getDate() != null) {
    		Date utcDate = DateHelper.convertToUTC(event.getDate(), timeZone);
    		event.setDate(utcDate);
    	}
    	
    }

}
