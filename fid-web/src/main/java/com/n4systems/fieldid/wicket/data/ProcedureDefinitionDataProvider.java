package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.model.Asset;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.PublishedState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

/**
 * Created by rrana on 2014-04-15.
 */
public class ProcedureDefinitionDataProvider extends FieldIDDataProvider<ProcedureDefinition>{

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    private PublishedState state;

    private List<? extends ProcedureDefinition> results;
    private Long size;

    private String searchTerm;

    private String procedureCode;
    private Asset asset;
    private boolean isProcedureCode;
    private boolean isAsset;

    public ProcedureDefinitionDataProvider(String order, SortOrder sortOrder, PublishedState state, String procedureCode, Asset asset, boolean isProcedureCode, boolean isAsset) {
        setSort(order, sortOrder);
        this.state = state;
        searchTerm = "";
        this.procedureCode = procedureCode;
        this.asset = asset;
        this.isProcedureCode = isProcedureCode;
        this.isAsset = isAsset;

    }


    @Override
    public Iterator<? extends ProcedureDefinition> iterator(int first, int count) {
        List<? extends ProcedureDefinition> procedureDefinitionList = null;

        if(isProcedureCode || isAsset) {
            if(state.equals(PublishedState.DRAFT)) {
                procedureDefinitionList = procedureDefinitionService.getSelectedDraftProcedures(procedureCode, asset, isAsset, getSort().getProperty(), getSort().isAscending(), first, count);
            } else if (state.equals(PublishedState.PREVIOUSLY_PUBLISHED)) {
                procedureDefinitionList = procedureDefinitionService.getSelectedPreviouslyPublishedProcedures(procedureCode, asset, isAsset, getSort().getProperty(), getSort().isAscending(), first, count);
            } else if (state.equals(PublishedState.PUBLISHED)) {
                procedureDefinitionList = procedureDefinitionService.getSelectedPublishedProcedures(procedureCode, asset, isAsset, getSort().getProperty(), getSort().isAscending(), first, count);
            }
        } else {
            if (state.equals(PublishedState.DRAFT)) {
                procedureDefinitionList = procedureDefinitionService.getAllDraftProcedures(searchTerm, getSort().getProperty(), getSort().isAscending(), first, count);
            } else if (state.equals(PublishedState.PREVIOUSLY_PUBLISHED)) {
                procedureDefinitionList = procedureDefinitionService.getAllPreviouslyPublishedProcedures(searchTerm, getSort().getProperty(), getSort().isAscending(), first, count);
            } else if (state.equals(PublishedState.PUBLISHED)) {
                procedureDefinitionList = procedureDefinitionService.getAllPublishedProcedures(searchTerm, getSort().getProperty(), getSort().isAscending(), first, count);
            }
        }

        return procedureDefinitionList.iterator();
    }

    @Override
    public int size() {
        int size = 0;

        if(isProcedureCode || isAsset) {
            if(state.equals(PublishedState.DRAFT)) {
                size = procedureDefinitionService.getSelectedDraftCount(procedureCode, asset, isAsset).intValue();
            } else if (state.equals(PublishedState.PREVIOUSLY_PUBLISHED)) {
                size = procedureDefinitionService.getSelectedPreviouslyPublishedCount(procedureCode, asset, isAsset).intValue();
            } else if (state.equals(PublishedState.PUBLISHED)) {
                size = procedureDefinitionService.getSelectedPublishedCount(procedureCode, asset, isAsset).intValue();
            }
        } else {
            if(state.equals(PublishedState.DRAFT)) {
                size = procedureDefinitionService.getDraftCount(searchTerm).intValue();
            } else if (state.equals(PublishedState.PREVIOUSLY_PUBLISHED)) {
                size = procedureDefinitionService.getPreviouslyPublishedCount(searchTerm).intValue();
            } else if (state.equals(PublishedState.PUBLISHED)) {
                size = procedureDefinitionService.getPublishedCount(searchTerm).intValue();
            }
        }

        return size;
    }

    protected String getTextFilter() {
        return null;
    }

    protected Class<? extends ProcedureDefinition> getTypeFilter() {
        return null;
    }

    @Override
    public IModel<ProcedureDefinition> model(final ProcedureDefinition object) {
        return new AbstractReadOnlyModel<ProcedureDefinition>() {
            @Override
            public ProcedureDefinition getObject() {
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