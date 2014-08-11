package com.n4systems.fieldid.wicket.components.setup.comment;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.commenttemplate.CommentTemplate;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.List;

/**
 * This is a list panel to display Comment Templates to the user.  It contains only two columns (Comment Template name
 * and an "action" column) and - since Comment Templates have no state like other entities - there is no "Archived"
 * list.
 *
 *
 *
 * Created by Jordan Heath on 08/08/14.
 */
public class CommentTemplateListPanel extends Panel {

    public static final int TEMPLATES_PER_PAGE = 20;

    private FieldIDDataProvider<CommentTemplate> dataProvider;

    public CommentTemplateListPanel(String id,
                                    FieldIDDataProvider<CommentTemplate> dataProvider) {
        super(id);

        //Drop the data provider into the table and keep a reference to it locally.
        this.dataProvider = dataProvider;
        add(new SimpleDefaultDataTable<CommentTemplate>("commentTemplateTable",
                                                        getCommentTemplateColumns(),
                                                        dataProvider,
                                                        TEMPLATES_PER_PAGE));
    }

    private List<IColumn<CommentTemplate>> getCommentTemplateColumns() {
        List<IColumn<CommentTemplate>> columnList = Lists.newArrayList();

        columnList.add(new PropertyColumn<CommentTemplate>(new FIDLabelModel("label.title"),
                                                           "name",
                                                           "name"));

        addActionColumn(columnList);

        return columnList;
    }

    /**
     * This method needs to be overridden so that Action Columns can be associated with the List Panel.
     *
     * @param columnList - A List of columns to be appended to the DataTable.
     */
    protected void addActionColumn(List<IColumn<CommentTemplate>> columnList) {}

    /**
     * This method needs to be overridden by the class that implements this Panel.  This provides an easy way for the
     * internal cells to access the main page.  Why?  Sorcery!!
     *
     * @return A null value, because you need to Override this method, not just use the default.
     */
    protected FIDFeedbackPanel getFeedbackPanel() {
        return null;
    }
}
