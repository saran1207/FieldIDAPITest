package com.n4systems.fieldid.service;

import com.n4systems.model.Tenant;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.user.User;
import com.newrelic.api.agent.NewRelic;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class FieldIdPersistenceService extends FieldIdService {

    @Autowired
    protected PersistenceService persistenceService;

    @Autowired
    private ApplicationContext applicationContext;


    @PersistenceContext EntityManager _entityManager;

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
    
    protected Tenant getCurrentTenant() {
        return persistenceService.findNonSecure(Tenant.class, securityContext.getTenantSecurityFilter().getTenantId());
    }
    
    protected User getCurrentUser() {
        return persistenceService.find(User.class, securityContext.getUserSecurityFilter().getUserId());
    }
    
    protected <T extends AbstractEntity> Long getId(T entity) {
        if (entity == null) {
            return null;
        }
        return entity.getId();
    }

    // Needed to get legacy savers into service layer
    // We need to remove the savers completely by refactoring them into services themselves
    protected EntityManager getEntityManager() {
        return _entityManager;
    }

    public void flush() {
        persistenceService.flush();
    }

    public void setNewRelicCustomParameters() {
        NewRelic.addCustomParameter("Tenant", getCurrentTenant().getName());
        NewRelic.addCustomParameter("User", getCurrentUser().getUserID());
    }

    public void setNewRelicWithAppInfoParameters() {
        setNewRelicCustomParameters();
        NewRelic.addCustomParameter("Device", getHttpRequest().getFirstHeader("X-APPINFO-DEVICE").toString());
        NewRelic.addCustomParameter("Device Type", getHttpRequest().getFirstHeader("X-APPINFO-DEVICETYPE").toString());
        NewRelic.addCustomParameter("Platform", getHttpRequest().getFirstHeader("X-APPINFO-PLATFORM").toString());
        NewRelic.addCustomParameter("OS Version", getHttpRequest().getFirstHeader("X-APPINFO-OSVERSION").toString());
        NewRelic.addCustomParameter("AppInfo", getHttpRequest().getFirstHeader("X-APPINFO-APPVERSION").toString());
    }

    protected HttpRequest getHttpRequest() {
        return applicationContext.getBean(HttpRequest.class);
    }
}
