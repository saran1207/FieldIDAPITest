package com.n4systems.model.user;

import com.n4systems.model.lastmodified.LastModified;
import com.n4systems.model.lastmodified.LastModifiedListLoader;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import javax.persistence.EntityManager;
import java.util.List;

public class UserLastModifiedLoader extends LastModifiedListLoader {

    public UserLastModifiedLoader(SecurityFilter filter) {
        super(filter, null);
    }

    @Override
    protected List<LastModified> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<LastModified> builder = new QueryBuilder<LastModified>(User.class, filter);
        builder.setSelectArgument(new NewObjectSelect(LastModified.class, "id", "modified"));
        builder.addWhere(WhereClauseFactory.create("registered", true));

        if (modifiedAfter != null) {
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.GT, "modified", modifiedAfter));
        }

        List<LastModified> mods = builder.getResultList(em);
        return mods;
    }
}
