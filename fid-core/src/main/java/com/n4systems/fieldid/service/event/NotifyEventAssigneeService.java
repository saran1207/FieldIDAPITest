package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.model.Event;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.FieldIdDateFormatter;
import com.n4systems.util.mail.TemplateMailMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotifyEventAssigneeService extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(NotifyEventAssigneeService.class);
    private static final String ASSIGNEE_TEMPLATE = "eventAssigned";

    @Autowired private MailService mailService;

    @Transactional
    public void notifyEventAssignee(Event event) {
        try {
            User assignee = event.getAssignee();

            if (assignee == null) {
                return;
            }

            String subject = "Work Assigned: " + event.getType().getName();

            TemplateMailMessage msg = new TemplateMailMessage(subject, ASSIGNEE_TEMPLATE);
            msg.getToAddresses().add(assignee.getEmailAddress());
            Date dueDate = event.getNextDate();
            boolean showTime = !new PlainDate(dueDate).equals(dueDate);

            Map<Long, String> dueDateStringMap = new HashMap<Long,String>();

            String dueDateString = new FieldIdDateFormatter(dueDate, assignee, false, showTime).format();
            dueDateStringMap.put(event.getId(), dueDateString);

            msg.getTemplateMap().put("event", event);
            msg.getTemplateMap().put("dueDateStringMap", dueDateStringMap);

            mailService.sendMessage(msg);
        } catch (Exception e) {
            logger.error("Could not notify assignee", e);
        }
    }

}
