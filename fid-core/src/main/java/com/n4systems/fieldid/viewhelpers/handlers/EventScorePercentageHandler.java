package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.fieldid.util.EventFormHelper;
import com.n4systems.model.Event;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.eventschedule.EventScheduleByGuidOrIdLoader;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;

import java.text.NumberFormat;

public class EventScorePercentageHandler extends WebOutputHandler {

    private PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();

    private EventScheduleByGuidOrIdLoader eventLoader = new EventScheduleByGuidOrIdLoader(new OpenSecurityFilter());

    private NumberFormat percentFormat = NumberFormat.getPercentInstance();

    public EventScorePercentageHandler(TableGenerationContext contextProvider) {
        super(contextProvider);
        percentFormat.setMaximumFractionDigits(2);
    }

    @Override
    public String handleWeb(Long entityId, Object value) {
        return getEventScorePercentage(value);
    }

    @Override
    public Object handleExcel(Long entityId, Object value) {
        return getEventScorePercentage(value);
    }
    
    private String getEventScorePercentage(Object value) {
        Event event = (Event) value;

        if(event.getWorkflowState() == WorkflowState.COMPLETED) {
            if(event.getScore() != null) {
                String[] fields = new String[] {"eventForm.sections","results"};
                QueryBuilder<Event> builder = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter());
                builder.addSimpleWhere("id",event.getId());
                builder.addPostFetchPaths(fields);
                event = persistenceManager.find(builder);
                return percentFormat.format(new EventFormHelper().getEventFormScorePercentage(event));
            } else
                return "";
        } else
            return "";
    }
}
