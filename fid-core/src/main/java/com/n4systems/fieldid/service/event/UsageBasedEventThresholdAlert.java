package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.model.Tenant;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.mail.MailMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;

public class UsageBasedEventThresholdAlert {
    
    private static final Logger logger = Logger.getLogger(UsageBasedEventThresholdAlert.class);

    private static final String SUBJECT = " : Usage Based User Warning";
    private static final String BODY = " has %d remaining events for their usage based users.";
    @Autowired
    private ConfigService configService;
    
    @Autowired
    private MailService mailService;

    public void sendAlert(Tenant tenant, int eventCount) {
        if(eventCount == getEventThreshold(tenant)) {
            MailMessage message = createMessage(tenant);
            try {
                mailService.sendMessage(message);
            } catch (MessagingException e) {
                logger.error("Could not send usage based user threshold alert for tenant: " + tenant.getDisplayName());
            }
        }
    }

    private MailMessage createMessage(Tenant tenant) {
        String subject = tenant.getDisplayName() + SUBJECT;
        String body = tenant.getDisplayName() + String.format(BODY, getEventThreshold(tenant));
        String recipient = configService.getString(ConfigEntry.FIELDID_SALES_MANAGERS_EMAIL);
        return new MailMessage(subject, body, recipient);
    }

    private int getEventThreshold(Tenant tenant) {
        return configService.getInteger(ConfigEntry.USAGE_BASED_EVENT_COUNT_THRESHOLD, tenant.getId());
    }
}
