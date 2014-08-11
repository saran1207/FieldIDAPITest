package com.n4systems.fieldid.wicket.components.setup.comment;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.commenttemplate.CommentTemplate;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * This is the comment column for the Comment Template List Page.
 *
 * Created by Jordan Heath on 11/08/14.
 */
public class CommentTemplateActionColumn extends AbstractColumn<CommentTemplate> {

    private CommentTemplateListPanel listPanel;

    public CommentTemplateActionColumn(CommentTemplateListPanel listPanel) {
        super(new FIDLabelModel(""));
        this.listPanel = listPanel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<CommentTemplate>> item,
                             String id,
                             IModel<CommentTemplate> model) {

        item.add(new CommentTemplateActionCell(id, model, listPanel));
    }
}
