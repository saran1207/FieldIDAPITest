package com.n4systems.fieldid.service.tenant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.AssetType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.tenant.SystemSettings;
import com.n4systems.model.tenant.TenantSettings;

public class SystemSettingsService extends FieldIdPersistenceService {

    @Autowired private TenantSettingsService tenantSettingsService;
    @Autowired private OrgService orgService;
    @Autowired private ExtendedFeatureService extendedFeatureService;

    @Transactional(readOnly = true)
    public SystemSettings getSystemSettings() {
        SystemSettings systemSettings = new SystemSettings();

        TenantSettings tenantSettings = tenantSettingsService.getTenantSettings();
        PrimaryOrg primaryOrg = getPrimaryOrg();

        systemSettings.setGpsCapture(tenantSettings.isGpsCapture());
        systemSettings.setAssignedTo(primaryOrg.hasExtendedFeature(ExtendedFeature.AssignedTo));
        systemSettings.setManufacturerCertificate(primaryOrg.hasExtendedFeature(ExtendedFeature.ManufacturerCertificate));
        systemSettings.setProofTestIntegration(primaryOrg.hasExtendedFeature(ExtendedFeature.ProofTestIntegration));
        systemSettings.setIdentifierFormat(primaryOrg.getIdentifierFormat());
        systemSettings.setIdentifierLabel(primaryOrg.getIdentifierLabel());
        systemSettings.setDateFormat(primaryOrg.getDateFormat());

        return systemSettings;
    }

    @Transactional
    public void saveSystemSettings(List<AssetType> assetTypes) {
        persistenceService.update(assetTypes);
    }

    @Transactional
    public void saveSystemSettings(SystemSettings settings) {
        tenantSettingsService.updateGpsCapture(settings.isGpsCapture());

        PrimaryOrg primaryOrg = getPrimaryOrg();

        primaryOrg.setIdentifierFormat(settings.getIdentifierFormat());
        primaryOrg.setIdentifierLabel(settings.getIdentifierLabel());
        primaryOrg.setDateFormat(settings.getDateFormat());

        Long tenantId = securityContext.getUserSecurityFilter().getTenantId();

        extendedFeatureService.setExtendedFeatureEnabled(tenantId, ExtendedFeature.AssignedTo, settings.isAssignedTo());
        extendedFeatureService.setExtendedFeatureEnabled(tenantId, ExtendedFeature.ManufacturerCertificate, settings.isManufacturerCertificate());
        extendedFeatureService.setExtendedFeatureEnabled(tenantId, ExtendedFeature.ProofTestIntegration, settings.isProofTestIntegration());

        persistenceService.update(primaryOrg);
    }

    private PrimaryOrg getPrimaryOrg() {
        return orgService.getPrimaryOrgForTenant(securityContext.getUserSecurityFilter().getTenantId());
    }

}
