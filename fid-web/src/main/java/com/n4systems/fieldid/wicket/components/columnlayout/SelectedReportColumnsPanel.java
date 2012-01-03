package com.n4systems.fieldid.wicket.components.columnlayout;

import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.model.columns.ColumnMapping;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;
import org.odlabs.wiquery.ui.sortable.SortableRevert;

import java.util.List;

public class SelectedReportColumnsPanel extends Panel {

    private WebMarkupContainer columnsListContainer;
    private IModel<List<ColumnMapping>> reportColumnsModel;

    public SelectedReportColumnsPanel(String id, IModel<List<ColumnMapping>> model) {
        super(id, model);
        setOutputMarkupId(true);

        this.reportColumnsModel = model;
        add(columnsListContainer = new WebMarkupContainer("columnsListContainer"));
        columnsListContainer.add(new ListView<ColumnMapping>("selectedReportColumnsList", model) {
            @Override
            protected void populateItem(ListItem<ColumnMapping> item) {
                item.add(new ReportColumnPanel("reportColumn", item.getModel(), false) {
                    @Override
                    protected void onRemoveLinkClicked(AjaxRequestTarget target) {
                        doRemoveColumn(target, getColumnModel().getObject());
                    }
                });
                item.setOutputMarkupId(true);
            }
        });
        columnsListContainer.setOutputMarkupId(true);
        columnsListContainer.add(makeSortableBehavior());
    }

    protected SortableAjaxBehavior makeSortableBehavior() {
        SortableAjaxBehavior sortable = new SimpleSortableAjaxBehavior() {
            @Override
            public void onUpdate(Component component, int newIndexOfMovingItem, AjaxRequestTarget target) {
                if (component == null) return;

                target.add(columnsListContainer);
                target.add(component);

                List<ColumnMapping> selectedColumns = getSelectedColumns();

                ReportColumnPanel theReportColumnPanel = (ReportColumnPanel) ((WebMarkupContainer)component).get("reportColumn");
                ColumnMapping reportColumn = theReportColumnPanel.getColumnModel().getObject();
                selectedColumns.remove(reportColumn);
                selectedColumns.add(newIndexOfMovingItem, reportColumn);
                target.add(columnsListContainer);
            }
        };
        sortable.setRevert(new SortableRevert(true));
        return sortable;
    }

    private List<ColumnMapping> getSelectedColumns() {
        return reportColumnsModel.getObject();
    }

    private void doRemoveColumn(AjaxRequestTarget target, ColumnMapping reportColumn) {
        getSelectedColumns().remove(reportColumn);
        target.add(columnsListContainer);
        onColumnRemoved(target, reportColumn);
    }

    protected void onColumnRemoved(AjaxRequestTarget target, ColumnMapping reportColumn) { }

}
