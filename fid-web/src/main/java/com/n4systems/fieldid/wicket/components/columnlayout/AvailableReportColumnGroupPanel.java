package com.n4systems.fieldid.wicket.components.columnlayout;

import com.n4systems.model.columns.ColumnMapping;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.util.List;

public class AvailableReportColumnGroupPanel extends Panel {

    private IModel<List<ColumnMapping>> model;

    public AvailableReportColumnGroupPanel(String id, final IModel<List<ColumnMapping>> model) {
        super(id, model);
        setOutputMarkupId(true);
        this.model = model;

        add(new ListView<ColumnMapping>("availableReportColumnsList", model) {
            @Override
            protected void populateItem(final ListItem<ColumnMapping> item) {
                ReportColumnPanel reportColumnPanel = new ReportColumnPanel("reportColumn", item.getModel(), true) {
                    @Override
                    protected void onAddLinkClicked(AjaxRequestTarget target, IModel<ColumnMapping> reportColumnModel) {
                        ColumnMapping reportColumn = reportColumnModel.getObject();
                        target.addComponent(AvailableReportColumnGroupPanel.this);
                        getAvailableColumns().remove(reportColumn);
                        onReportColumnAdded(target, reportColumn);
                    }

                    @Override
                    protected void onRemoveLinkClicked(AjaxRequestTarget target) {
                        target.addComponent(AvailableReportColumnGroupPanel.this);
                        onReportColumnRemoved(target, item.getModel().getObject());
                        // The above just caused the list of columns to load by calling getObject(). We must
                        // detach the model since the list has changed in db (one item removed) so the model's cache must be cleared
                        model.detach();
                    }
                };
                onReportColumnCreated(reportColumnPanel);
                item.add(reportColumnPanel);
            }
        });
    }

    private List<ColumnMapping> getAvailableColumns() {
        return model.getObject();
    }

    protected void onReportColumnAdded(AjaxRequestTarget target, ColumnMapping column) { }

    protected void onReportColumnCreated(ReportColumnPanel panel) {}

    protected void onReportColumnRemoved(AjaxRequestTarget target, ColumnMapping columnMapping) {}

}
