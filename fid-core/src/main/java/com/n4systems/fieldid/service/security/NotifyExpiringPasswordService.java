package com.n4systems.fieldid.service.security;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.model.user.User;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by rrana on 2014-10-09.
 */
@Transactional
public class NotifyExpiringPasswordService extends FieldIdPersistenceService {

    private static final Logger log = Logger.getLogger(com.n4systems.fieldid.service.procedure.NotifyProcedureAuthorizersService.class);

    private static final String NOTIFY_EXPIRING_PASSWORD_TEMPLATE = "expiringPasswordTemplate";

    @Autowired
    protected TenantSettingsService tenantSettingsService;

    @Autowired
    protected UserService userService;

    @Autowired
    private MailService mailService;

    //Every day at noon
    //@Scheduled(cron = "0 0 12 * * ?")

    //Every 1 minute after a successful run
    @Scheduled(fixedDelay = 100000)
    public void notifyExpiringPasswords() {

        //List of all tenants that enforce expiring password policy
        QueryBuilder<TenantSettings> query = new QueryBuilder<>(TenantSettings.class, new OpenSecurityFilter());
        query.addWhere(new WhereParameter(WhereParameter.Comparator.GT, "passwordPolicy.expiryDays", 0));
        query.addSimpleWhere("tenant.disabled", false);
        List<TenantSettings> list = persistenceService.findAll(query);

        //For each tenant, find all users who's passwords will be expiring in the next 10 days.
        for(TenantSettings tenants:list) {

            //Get the list of all users who's passwords are about to expire in the next 10 days.
            List<User> userList = userService.getExpiringPasswordUsersByTenant(tenants.getTenant().getId(), tenants.getPasswordPolicy().getExpiryDays());
            for(User user:userList) {
                if(!user.isResetEmailSent()) {
                    try {
                        sendExpiringPasswordNotification(user);
                        user.setResetEmailSent(true);
                        persistenceService.update(user);
                    } catch (Exception e) {
                        log.error("Could not send password expiry email to: " + user.getEmailAddress(), e);
                    }
                }
            }
        }
    }

    private void sendExpiringPasswordNotification(User user) {
        TemplateMailMessage message = createNotification();
        message.getToAddresses().add(user.getEmailAddress());

        try {
            mailService.sendMessage(message);
        } catch (Exception e) {
            log.error("Could not notify the user", e);
        }

    }

    private TemplateMailMessage createNotification() {
        TemplateMailMessage msg = createMailMessage();
        return msg;
    }

    private TemplateMailMessage createMailMessage() {
        String subject = "Master Lock FieldiD - Password Expiry Notice";
        TemplateMailMessage msg = new TemplateMailMessage(subject, NOTIFY_EXPIRING_PASSWORD_TEMPLATE);
        msg.setSubject(subject);
        return msg;
    }

}
