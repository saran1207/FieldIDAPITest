package com.n4systems.fieldid.wicket.components.action;

import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.model.CriteriaResult;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ViewActionsListPage extends ActionsListPage {

    public ViewActionsListPage(PageParameters params) {
        super(new EntityModel<CriteriaResult>(CriteriaResult.class,params.get("uniqueID").toLong()), params.get("readOnly")!=null);
    }

    public ViewActionsListPage(IModel<CriteriaResult> criteriaResultModel, boolean readOnly) {
        super(criteriaResultModel, readOnly);
    }

    @Override
    protected void setActionsListResponsePage(IModel<CriteriaResult> criteriaResultModel) {
        setResponsePage(new ViewActionsListPage(criteriaResultModel, readOnly));
    }
}
