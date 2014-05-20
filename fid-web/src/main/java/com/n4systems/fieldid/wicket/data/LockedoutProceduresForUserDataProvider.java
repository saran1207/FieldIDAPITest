package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;

/**
 * Created by rrana on 2014-05-19.
 */
public class LockedoutProceduresForUserDataProvider extends FieldIDDataProvider<Procedure> {

    @SpringBean
    private ProcedureService procedureService;

    private BaseOrg org;

    public LockedoutProceduresForUserDataProvider(BaseOrg org){
        super();
        this.org = org;
    }

    @Override
    public Iterator<? extends Procedure> iterator(int first, int count) {
        return procedureService.getLockedAssignedTo(org).iterator();
    }

    @Override
    public int size() {
        return procedureService.getLockedAssignedTo(org).size();
    }

    @Override
    public IModel<Procedure> model(Procedure project) {
        return new EntityModel<Procedure>(Procedure.class, project.getId());
    }

}
