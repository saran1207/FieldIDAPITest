package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.SecurityContext;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class OrgModel extends FieldIDSpringModel<List<? extends BaseOrg>> {

    private @SpringBean OrgService orgService;
    private @SpringBean SecurityContext securityContext;
    private String pattern;

    @Override
    protected List<? extends BaseOrg> load() {
        return orgService.getAllOrgsLike(pattern);
    }

    public OrgModel setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }
}
