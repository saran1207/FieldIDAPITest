package com.n4systems.fieldid.wicket.components.search.results;

import com.n4systems.fieldid.utils.Predicate;
import com.n4systems.fieldid.wicket.behavior.DisplayNoneIfCondition;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.data.ListableSortableDataProvider;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class SelectionStatusPanel extends Panel {

    private DataTable dataTable;
    private MultiIdSelection selection;
    private ISortableDataProvider dataProvider;
    private boolean justSelectedPage;
    private Integer numJustSelected;

    public SelectionStatusPanel(String id, DataTable table, MultiIdSelection multiSelection, final ListableSortableDataProvider dataProvider) {
        super(id);
        this.dataTable = table;
        this.selection = multiSelection;
        this.dataProvider = dataProvider;

        setOutputMarkupPlaceholderTag(true);

        WebMarkupContainer justSelectedPageContainer = new WebMarkupContainer("justSelectedPageContainer");
        justSelectedPageContainer.add(new DisplayNoneIfCondition(new Predicate() {
            @Override
            public boolean evaluate() {
                return !justSelectedPage;
            }
        }));

        WebMarkupContainer regularStateContainer = new WebMarkupContainer("regularStateContainer");
        regularStateContainer.add(new DisplayNoneIfCondition(new Predicate() {
            @Override
            public boolean evaluate() {
                return justSelectedPage;
            }
        }));

        regularStateContainer.add(new FlatLabel("currentySelectedItems", new PropertyModel<Integer>(selection, "numSelectedIds")));
        regularStateContainer.add(new FlatLabel("totalAvailableItems", new PropertyModel<Integer>(this, "totalRows")));

        justSelectedPageContainer.add(new FlatLabel("numJustSelected", new PropertyModel<Integer>(this, "numJustSelected")));

        AjaxLink clearSelectionLink = new AjaxLink("clearSelectionLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                selection.clear();
                target.addComponent(SelectionStatusPanel.this);
                target.appendJavascript("setTableSelected('"+dataTable.getMarkupId()+"', false);");
                onSelectionChanged(target);
            }
        };
        regularStateContainer.add(clearSelectionLink);

        AjaxLink selectAllLink = null;
        justSelectedPageContainer.add(selectAllLink = new AjaxLink("selectAllLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                selection.clear();
                target.appendJavascript("setTableSelected('"+dataTable.getMarkupId()+"', true);");
                List<Long> idList = dataProvider.getIdList();
                for (Long id : idList) {
                    selection.addId(id);
                }
                target.addComponent(SelectionStatusPanel.this);
                onSelectionChanged(target);
            }
        });

        selectAllLink.add(new FlatLabel("totalAvailableItems", new PropertyModel<Integer>(this, "totalRows")));

        add(regularStateContainer);
        add(justSelectedPageContainer);
    }

    public Integer getTotalRows() {
        return dataProvider.size();
    }

    protected void onSelectionChanged(AjaxRequestTarget target) {}

    @Override
    public boolean isVisible() {
        return selection.getNumSelectedIds() > 0;
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        justSelectedPage = false;
        numJustSelected = null;
    }

    public void justSelectedPageWithElements(int numJustSelected) {
        justSelectedPage = true;
        this.numJustSelected = numJustSelected;
    }

}
