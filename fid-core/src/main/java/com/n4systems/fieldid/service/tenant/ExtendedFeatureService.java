package com.n4systems.fieldid.service.tenant;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.EventType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.savedreports.SavedReportAssignedToTrimmer;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class ExtendedFeatureService extends FieldIdPersistenceService {

    @Autowired private OrgService orgService;

    @Transactional
    public void setExtendedFeatureEnabled(Long tenantId, ExtendedFeature extendedFeature, boolean enabled) {
        PrimaryOrg primaryOrg = orgService.getPrimaryOrgForTenant(tenantId);

        if (enabled) {
            primaryOrg.getExtendedFeatures().add(extendedFeature);
        } else {
            if (primaryOrg.hasExtendedFeature(extendedFeature)) {
                performFeatureTeardown(primaryOrg, extendedFeature);
            }
            primaryOrg.getExtendedFeatures().remove(extendedFeature);
        }

        persistenceService.save(primaryOrg);
    }

    private void performFeatureTeardown(PrimaryOrg primaryOrg, ExtendedFeature extendedFeature) {
        if (extendedFeature.equals(ExtendedFeature.EmailAlerts)) {
            tearDownEmailAlerts(primaryOrg);
        } else if (extendedFeature.equals(ExtendedFeature.AssignedTo)) {
            tearDownAssignedTo(primaryOrg);
        }
    }

    private void tearDownAssignedTo(PrimaryOrg primaryOrg) {
        QueryBuilder<SavedReport> query = new QueryBuilder<SavedReport>(SavedReport.class, new TenantOnlySecurityFilter(primaryOrg.getTenant()));
        List<SavedReport> reportsToRemove = SavedReportAssignedToTrimmer.extractAssignedToReferences(persistenceService.findAll(query));

        for (SavedReport report : reportsToRemove) {
            persistenceService.remove(report);
        }

        QueryBuilder<EventType> eventTypeQuery = new QueryBuilder<EventType>(EventType.class, new TenantOnlySecurityFilter(primaryOrg.getTenant()));
        List<EventType> eventTypes = persistenceService.findAll(eventTypeQuery);

        for (EventType eventType : eventTypes) {
            eventType.removeAssignedTo();
            persistenceService.save(eventType);
        }
    }

    private void tearDownEmailAlerts(PrimaryOrg primaryOrg) {
        TenantOnlySecurityFilter tenantFilter = new TenantOnlySecurityFilter(primaryOrg.getTenant());
        QueryBuilder<NotificationSetting> query = new QueryBuilder<NotificationSetting>(NotificationSetting.class, tenantFilter);
        List<NotificationSetting> settings = persistenceService.findAll(query);

        for (NotificationSetting setting: settings) {
            persistenceService.remove(setting);
		}
    }

}
