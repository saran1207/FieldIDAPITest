package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.event.ProcedureAuditEventService;
import com.n4systems.model.Asset;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

/**
 * Created by rrana on 2014-06-10.
 */
public class ProcedureAuditDataProvider extends FieldIDDataProvider<ProcedureAuditEvent>{

    @SpringBean
    private ProcedureAuditEventService procedureService;

    private PublishedState state;

    private List<? extends Procedure> results;
    private Long size;

    private String searchTerm;

    private String procedureCode;
    private Asset asset;
    private boolean isProcedureCode;
    private boolean isAsset;

    public ProcedureAuditDataProvider(String order, SortOrder sortOrder, PublishedState state, String procedureCode, Asset asset, boolean isProcedureCode, boolean isAsset) {
        setSort(order, sortOrder);
        this.state = state;
        searchTerm = "";
        this.procedureCode = procedureCode;
        this.asset = asset;
        this.isProcedureCode = isProcedureCode;
        this.isAsset = isAsset;
    }


    @Override
    public Iterator<? extends ProcedureAuditEvent> iterator(int first, int count) {
        List<? extends ProcedureAuditEvent> procedureDefinitionList = null;

        if(isProcedureCode || isAsset) {
            procedureDefinitionList = procedureService.getSelectedAuditProcedures(procedureCode, asset, isAsset, getSort().getProperty(), getSort().isAscending(), first, count);
        } else {
           procedureDefinitionList = procedureService.getAllAuditProcedures(searchTerm, getSort().getProperty(), getSort().isAscending(), first, count);
        }

        return procedureDefinitionList.iterator();
    }

    @Override
    public int size() {
        int size = 0;

        if(isProcedureCode || isAsset) {
            size = procedureService.getSelectedAuditCount(procedureCode, asset, isAsset).intValue();
        } else {
            size = procedureService.getAllAuditCount(searchTerm).intValue();
        }

        return size;
    }

    protected String getTextFilter() {
        return null;
    }

    protected Class<? extends ProcedureAuditEvent> getTypeFilter() {
        return null;
    }

    @Override
    public IModel<ProcedureAuditEvent> model(final ProcedureAuditEvent object) {
        return new AbstractReadOnlyModel<ProcedureAuditEvent>() {
            @Override
            public ProcedureAuditEvent getObject() {
                return object;
            }
        };
    }

    @Override
    public void detach() {
        results = null;
        size = null;
    }

    public void setSearchTerm(String sTerm) {
        searchTerm = sTerm;
    }

    public void resetProcedureCodeFlag(){
        isProcedureCode = false;
    }

    public void resetAssetCodeFlag(){
        isAsset = false;
    }

}