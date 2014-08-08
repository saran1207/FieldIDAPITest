package com.n4systems.fieldid.wicket.components.setup.eventbook;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.event.EventBookService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.EventBook;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * This is the list panel for both EventBook List pages (Archived and Active).
 *
 * Created by Jordan Heath on 06/08/14.
 */
public class EventBooksListPanel extends Panel {

    @SpringBean
    protected EventBookService eventBookService;

    public static final int PROCEDURES_PER_PAGE = 20;

    private FieldIDDataProvider<EventBook> dataProvider;


    public EventBooksListPanel(String id, FieldIDDataProvider<EventBook> dataProvider) {
        super(id);

        this.dataProvider = dataProvider;
        add(new SimpleDefaultDataTable<ProcedureDefinition>("eventBookTable", getEventBookTableColumns(), dataProvider, PROCEDURES_PER_PAGE));
    }

    private List<IColumn<EventBook>> getEventBookTableColumns() {
        List<IColumn<EventBook>> columns = Lists.newArrayList();

//        addEditColumn(columns);

        columns.add(new PropertyColumn<EventBook>(new FIDLabelModel("label.title"), "name", "name")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<EventBook>> item, final String componentId, final IModel<EventBook> rowModel)
            {
//                item.add(new Label(componentId, createLabelModel(rowModel)));
                item.add(new EventBooksEditCell(componentId, rowModel));
            }
        });

        columns.add(new PropertyColumn<EventBook>(new FIDLabelModel("label.jobsite"),"owner.name", "owner.name"));

        columns.add(new PropertyColumn<EventBook>(new FIDLabelModel("label.created"), "created", "created"));

//        columns.add(new PropertyColumn<EventBook>(new FIDLabelModel("label.status"), "open", "open"));
        columns.add(new PropertyColumn<EventBook>(new FIDLabelModel("label.status"), "open", "open")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<EventBook>> item, final String componentId, final IModel<EventBook> rowModel) {
                item.add(new Label(componentId, (rowModel.getObject().isOpen() ? "Open" : "Closed")));
            }
        });

        addActionColumn(columns);

        return columns;
    }

    protected void addActionColumn(List<IColumn<EventBook>> columns) {
        //This does nothing.  You need to override it where you use this panel.
    }

//    private void addEditColumn(List<IColumn<EventBook>> columns) {
//        columns.add(new EventBooksEditColumn());
//    }

    /**
     * This method needs to be overridden by the class that implements this Panel.  This provides an easy way for the
     * internal cells to access the main page.  Why?  Sorcery!!
     *
     * @return A null value, because you need to Override this method, not just use the default.
     */
    protected FIDFeedbackPanel getFeedbackPanel() {
        return null;
    }

    public FieldIDDataProvider<EventBook> getDataProvider() {
        return dataProvider;
    }
}
