package com.n4systems.fieldid.service.warningtemplates;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.warningtemplate.WarningTemplate;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * This is the Hibernate Service class for the <b>warning_templates</b> Table.  Here, you can add, delete or update
 * Warning Templates for use with ProcedureDefinitions.
 *
 * Created by Jordan Heath on 14-11-20.
 */
public class WarningTemplateService extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(WarningTemplateService.class);

    /**
     * Grab all Warning Templates for the current Tenant, ordered by warning_templates.name.
     *
     * @return A <b>List</b> populated with <b>WarningTemplate</b>s, representing all of the Tenant's existing Warning Templates.
     */
    public List<WarningTemplate> getAllTemplatesForTenant() {
        QueryBuilder<WarningTemplate> builder = createTenantSecurityBuilder(WarningTemplate.class);
        builder.addOrder("name");

        return persistenceService.findAll(builder);
    }

    public WarningTemplate save(WarningTemplate template) {
        Long id = persistenceService.save(template);

        return persistenceService.find(WarningTemplate.class, id);
    }

    public WarningTemplate update(WarningTemplate template) {
        return persistenceService.update(template);
    }

    public void delete(WarningTemplate template) {
        persistenceService.remove(template);
    }

    public WarningTemplate loadById(Long id) {
        return persistenceService.find(WarningTemplate.class, id);
    }
}
