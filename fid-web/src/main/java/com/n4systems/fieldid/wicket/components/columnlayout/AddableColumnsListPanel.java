package com.n4systems.fieldid.wicket.components.columnlayout;

import com.n4systems.model.columns.ColumnMapping;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

public class AddableColumnsListPanel extends Panel {

    public AddableColumnsListPanel(String id, IModel<List<ColumnMapping>> colsModel) {
        super(id);

        add(new CreateCustomColumnPanel("addPanel") {
            @Override
            protected void onNewCustomColumnAdded(AjaxRequestTarget target, String name) {
                AddableColumnsListPanel.this.onNewCustomColumnAdded(target, name);
            }
        });

        add(new AvailableReportColumnGroupPanel("listView", colsModel) {
            @Override
            protected void onReportColumnAdded(AjaxRequestTarget target, ColumnMapping column) {
                AddableColumnsListPanel.this.onReportColumnAdded(target, column);
            }

            @Override
            protected void onReportColumnCreated(ReportColumnPanel panel) {
                panel.setDeleteAlwaysVisible(true);
            }

            @Override
            protected void onReportColumnRemoved(AjaxRequestTarget target, ColumnMapping columnMapping) {
                AddableColumnsListPanel.this.onReportColumnRemoved(target, columnMapping);
            }
        });
    }

    protected void onReportColumnRemoved(AjaxRequestTarget target, ColumnMapping columnMapping) {
    }

    protected void onReportColumnAdded(AjaxRequestTarget target, ColumnMapping column) {
    }

    protected void onNewCustomColumnAdded(AjaxRequestTarget target, String name) {
    }

}
