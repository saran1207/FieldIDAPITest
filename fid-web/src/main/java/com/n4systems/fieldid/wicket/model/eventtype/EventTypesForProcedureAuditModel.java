package com.n4systems.fieldid.wicket.model.eventtype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.EventType;
import com.n4systems.model.ProcedureAuditEventType;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrana on 2014-06-12.
 */
public class EventTypesForProcedureAuditModel extends FieldIDSpringModel<List<? extends EventType>> {

    private IModel<ProcedureDefinition> procedureDefinitionIModel;

    public EventTypesForProcedureAuditModel(IModel<ProcedureDefinition> orgModel) {
        this.procedureDefinitionIModel = orgModel;
    }

    @Override
    protected List<ProcedureAuditEventType> load() {

        if (procedureDefinitionIModel.getObject() == null) {
            return Lists.newArrayList();
        }

        List<ProcedureAuditEventType> eventTypes = new ArrayList<ProcedureAuditEventType>();

        return eventTypes;
    }

}
