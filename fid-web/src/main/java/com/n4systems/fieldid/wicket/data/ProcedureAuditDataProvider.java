package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.event.ProcedureAuditEventService;
import com.n4systems.model.Asset;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.date.DateService;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rrana on 2014-06-10.
 */
public class ProcedureAuditDataProvider extends FieldIDDataProvider<ProcedureAuditEvent>{

    @SpringBean
    private ProcedureAuditEventService procedureService;

    @SpringBean
    private DateService dateService;

    private PublishedState state;

    private List<? extends Procedure> results;
    private Long size;

    private String searchTerm;

    private String procedureCode;
    private Asset asset;
    private boolean isProcedureCode;
    private boolean isAsset;

    private DateRange dateRange;
    private BaseOrg owner;

    public ProcedureAuditDataProvider(String order, SortOrder sortOrder, PublishedState state, String procedureCode, Asset asset, boolean isProcedureCode, boolean isAsset, DateRange dateRange, BaseOrg owner) {
        setSort(order, sortOrder);
        this.state = state;
        searchTerm = "";
        this.procedureCode = procedureCode;
        this.asset = asset;
        this.isProcedureCode = isProcedureCode;
        this.isAsset = isAsset;
        this.dateRange = dateRange;
        this.owner = owner;
    }


    @Override
    public Iterator<? extends ProcedureAuditEvent> iterator(int first, int count) {
        List<? extends ProcedureAuditEvent> procedureDefinitionList = null;

        if(isProcedureCode || isAsset) {
            procedureDefinitionList = procedureService.getSelectedAuditProcedures(procedureCode, asset, isAsset, getFromDate(), getToDate(), getSort().getProperty(), getSort().isAscending(), first, count, owner);
        } else {
           procedureDefinitionList = procedureService.getAllAuditProcedures(searchTerm, getFromDate(), getToDate(), getSort().getProperty(), getSort().isAscending(), first, count, owner);
        }

        return procedureDefinitionList.iterator();
    }

    private Date getFromDate() {
        return dateRange.getFrom() != null ? dateService.calculateFromDate(dateRange): null;
    }

    private Date getToDate() {
        return dateRange.getTo() != null ? dateService.calculateInclusiveToDate(dateRange) : null;
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

    public void setDueDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }
}