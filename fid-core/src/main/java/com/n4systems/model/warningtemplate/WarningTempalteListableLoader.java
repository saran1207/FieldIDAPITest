package com.n4systems.model.warningtemplate;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * This is the List Loader for the Warning Templates combo box.
 *
 * Created by Jordan Heath on 14-11-20.
 */
public class WarningTempalteListableLoader extends ListLoader<WarningTemplate> {
    public WarningTempalteListableLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<WarningTemplate> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<WarningTemplate> builder = new QueryBuilder<WarningTemplate>(WarningTemplate.class, filter).setOrder("name");

        return builder.getResultList(em);
    }
}
