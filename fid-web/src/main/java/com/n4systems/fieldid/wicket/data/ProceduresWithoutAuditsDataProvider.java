package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

public class ProceduresWithoutAuditsDataProvider extends FieldIDDataProvider<ProcedureDefinition>{

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    private BaseOrg org;

    public ProceduresWithoutAuditsDataProvider(BaseOrg org){
        super();
        this.org = org;
    }

    @Override
    public Iterator<? extends ProcedureDefinition> iterator(int pageNumber, int pageSize) {
        List<ProcedureDefinition> proceduresWithoutAudits = procedureDefinitionService.getPublishedProceduresWithoutAudits(org);
        return proceduresWithoutAudits.iterator();
    }

    @Override
    public int size() {
        return procedureDefinitionService.getPublishedProceduresWithoutAudits(org).size();
    }

    @Override
    public IModel<ProcedureDefinition> model(ProcedureDefinition procedureDefinition) {
        return new EntityModel<ProcedureDefinition>(ProcedureDefinition.class, procedureDefinition.getId());
    }

}
