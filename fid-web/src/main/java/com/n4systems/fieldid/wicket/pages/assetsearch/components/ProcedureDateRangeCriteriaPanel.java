package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.wicket.components.DateRangePicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.search.ProcedureCriteria;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import static ch.lambdaj.Lambda.on;

public class ProcedureDateRangeCriteriaPanel extends Panel {

    public ProcedureDateRangeCriteriaPanel(String id, IModel<ProcedureCriteria> criteria) {
        super(id);

        add(new DateRangePicker("dueRangePicker", new FIDLabelModel("label.due_date"), ProxyModel.of(criteria, on(ProcedureCriteria.class).getDueDateRange())));
        add(new DateRangePicker("completeRangePicker", new FIDLabelModel("label.completed_date"), ProxyModel.of(criteria, on(ProcedureCriteria.class).getDateRange())));
    }

}
