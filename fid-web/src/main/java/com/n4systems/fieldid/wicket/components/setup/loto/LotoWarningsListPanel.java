package com.n4systems.fieldid.wicket.components.setup.loto;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.warningtemplate.WarningTemplate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.List;

/**
 * This is the List Panel for Warning Templates in the LOTO Details Setup page.
 *
 * Created by Jordan Heath on 14-11-20.
 */
public class LotoWarningsListPanel extends Panel {

    private static final int TEMPLATES_PER_PAGE = 20;

    public LotoWarningsListPanel(String id,
                                 FieldIDDataProvider<WarningTemplate> dataProvider) {
        super(id);
        add(new SimpleDefaultDataTable<WarningTemplate>("warningTemplateTable",
                                                        getWarningTemplateColumns(),
                                                        dataProvider,
                                                        TEMPLATES_PER_PAGE));

        add(new AjaxLink<Void>("addLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                //Override this method where the panel is instantiated.  We need access to some additional functions
                //down there.
                doAddAction(target);
            }
        });
    }

    /**
     * This method takes care of creating all necessary columns for the table.
     *
     * @return A <b>List</b> of the columns for the DataTable.
     */
    private List<IColumn<WarningTemplate>> getWarningTemplateColumns() {
        List<IColumn<WarningTemplate>> columnList = Lists.newArrayList();

        columnList.add(new PropertyColumn<>(new FIDLabelModel("label.name"),
                                            "name"));

        columnList.add(new PropertyColumn<>(new FIDLabelModel("label.warning_value"),
                                            "warning"));

        addActionColumn(columnList);

        return columnList;
    }

    /**
     * This method needs to be overridden so that Action Columns can be associated with the List Panel.
     *
     * @param columnList - A List of columns to be appended to the DataTable.
     */
    protected void addActionColumn(List<IColumn<WarningTemplate>> columnList) {}

    /**
     * This method needs to be overridden by the class that implements this Panel.  This provides an easy way for
     * internal cells to access the main page.
     *
     * @return A null value, because you need to override this method.  If you use the default, zombies will rise!!
     */
    protected FIDFeedbackPanel getFeedbackPanel() {
        return null;
    }

    protected void doAddAction(AjaxRequestTarget target) {
        target.add(getFeedbackPanel());
        warn(new FIDLabelModel("label.success"));
    }
}
