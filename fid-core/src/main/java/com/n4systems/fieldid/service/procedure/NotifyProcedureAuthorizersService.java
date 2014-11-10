package com.n4systems.fieldid.service.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.download.SystemUrlUtil;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.services.SecurityContext;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class NotifyProcedureAuthorizersService extends FieldIdPersistenceService {

    private static final Logger log = Logger.getLogger(NotifyProcedureAuthorizersService.class);

    private static final String NOTIFY_PROCEDURE_AUTH_TEMPLATE = "procedureDefinitionApproval";
    private static final String NOTIFY_PROCEDURE_REJECTION_TEMPLATE = "procedureDefinitionRejection";

    @Autowired private UserGroupService userGroupService;
    @Autowired private ProcedureDefinitionService procedureDefinitionService;
    @Autowired private MailService mailService;

    public void notifyProcedureAuthorizers() {
        QueryBuilder<ProcedureDefinition> assigneeQuery = new QueryBuilder<ProcedureDefinition>(ProcedureDefinition.class, new OpenSecurityFilter());
        assigneeQuery.addSimpleWhere("publishedState", PublishedState.WAITING_FOR_APPROVAL);
        assigneeQuery.addSimpleWhere("authorizationNotificationSent", false);
        assigneeQuery.addSimpleWhere("tenant.disabled", false);

        List<ProcedureDefinition> publishedDefsAwaitingAuthorization = persistenceService.findAll(assigneeQuery);

        for (ProcedureDefinition procedureDefinition : publishedDefsAwaitingAuthorization) {
            if(procedureDefinition.getOwner().getPrimaryOrg().getExtendedFeatures().contains(ExtendedFeature.EmailAlerts) ||
                    procedureDefinition.getTenant().getSettings().getApprovalUserOrGroup() == null) {
                try {
                    sendAuthorizationNotification(procedureDefinition);
                } catch (Exception e) {
                    log.error("Could not send auth notification for procedureDef: " + procedureDefinition.getId(), e);
                } finally {
                    persistenceService.update(procedureDefinition);
                }
            }
        }
    }

    public void notifyProcedureRejection(ProcedureDefinition definition, String rejectionMessage) {
        String subject = "Lockout/Tagout Procedure Rejected";
        TemplateMailMessage msg = new TemplateMailMessage(subject, NOTIFY_PROCEDURE_REJECTION_TEMPLATE);
        msg.setSubject(subject);
        msg.getToAddresses().add(definition.getDevelopedBy().getEmailAddress());

        msg.getTemplateMap().put("systemUrl", SystemUrlUtil.getSystemUrl(definition.getTenant()));
        msg.getTemplateMap().put("procedureDefinition", definition);
        msg.getTemplateMap().put("rejector", getCurrentUser());
        msg.getTemplateMap().put("rejectionMessage", rejectionMessage);

        try {
            mailService.sendMessage(msg);
        } catch (Exception e) {
            log.error("Could not notify procedure rejection", e);
        }
    }

    private void sendAuthorizationNotification(ProcedureDefinition procedureDefinition) {
        Tenant tenant = procedureDefinition.getTenant();
        TenantSettings settings = tenant.getSettings();

        if (settings.getApprovalUser() != null) {
            sendNotifications(procedureDefinition, settings.getApprovalUser(), null);
            procedureDefinition.setAuthorizationNotificationSent(true);
        } else if (settings.getApprovalUserGroup() != null) {
            sendNotifications(procedureDefinition, settings.getApprovalUserGroup());
            procedureDefinition.setAuthorizationNotificationSent(true);
        } else {
            // There is no approval user or group (but was one earlier since this was marked waiting for approval)
            // This means we can simply publish the procedure def automatically.
            log.warn("Procedure def waiting for certification but no certifier user or group set: " + procedureDefinition.getId() +". Publishing automatically.");
            securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(tenant.getId()));
            try {
                procedureDefinitionService.publishProcedureDefinition(procedureDefinition);
            } catch (Exception e) {
                log.error("Failed to generate annotated images for Procedure Definition: " + procedureDefinition.getId());
                log.error(e.getMessage());
            }
        }
    }

    private void sendNotifications(ProcedureDefinition procedureDefinition, UserGroup approvalGroup) {
        List<User> usersInGroup = userGroupService.getUsersInGroup(approvalGroup);
        for (User user : usersInGroup) {
            sendNotifications(procedureDefinition, user, approvalGroup);
        }
    }

    private void sendNotifications(ProcedureDefinition procedureDefinition, User approvalUser, UserGroup approvalUserGroup) {
        TemplateMailMessage message = createNotification(procedureDefinition, approvalUserGroup);
        message.getToAddresses().add(approvalUser.getEmailAddress());

        try {
            mailService.sendMessage(message);
        } catch (Exception e) {
            log.error("Could not notify procedure certifier", e);
        }
    }

    private TemplateMailMessage createNotification(ProcedureDefinition procedureDefinition, UserGroup approvalGroup) {
        TemplateMailMessage msg = createMailMessage(procedureDefinition, approvalGroup);

        return msg;
    }

    private TemplateMailMessage createMailMessage(ProcedureDefinition procedureDefinition, UserGroup approvalGroup) {
        String subject = "Certification Requested: Lockout/Tagout Procedure";
        TemplateMailMessage msg = new TemplateMailMessage(subject, NOTIFY_PROCEDURE_AUTH_TEMPLATE);
        msg.setSubject(subject);

        msg.getTemplateMap().put("systemUrl", SystemUrlUtil.getSystemUrl(procedureDefinition.getTenant()));
        msg.getTemplateMap().put("procedureDefinition", procedureDefinition);
        msg.getTemplateMap().put("approvalGroup", approvalGroup);
        return msg;
    }

}
