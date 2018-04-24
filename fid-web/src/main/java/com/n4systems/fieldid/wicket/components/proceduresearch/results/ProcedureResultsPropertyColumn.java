package com.n4systems.fieldid.wicket.components.proceduresearch.results;

import com.n4systems.fieldid.wicket.components.reporting.columns.display.FieldIdPropertyColumn;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.util.views.RowView;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Override the FieldIdPropertyColumn to provide formatting unique to the Asset Search Results page
 */
public class ProcedureResultsPropertyColumn extends FieldIdPropertyColumn {

    private boolean googleTranslateEnabled;

    public ProcedureResultsPropertyColumn(boolean googleTranslateEnabled, IModel<String> displayModel, ColumnMappingView column, int index, boolean sortable) {
        super(displayModel, column, index, sortable);
        this.googleTranslateEnabled =  googleTranslateEnabled;
    }

    public ProcedureResultsPropertyColumn(boolean googleTranslateEnabled, IModel<String> displayModel, ColumnMappingView column, int index) {
        super(displayModel, column, index);
        this.googleTranslateEnabled = googleTranslateEnabled;
    }

    @Override
    public void populateItem(Item<ICellPopulator<RowView>> item, String componentId, IModel<RowView> rowModel) {
        if (googleTranslateEnabled) {
            ICellPopulator<RowView> modelObject = item.getModelObject();
            if (modelObject instanceof ProcedureResultsPropertyColumn) {
                String propertyExpression = ((ProcedureResultsPropertyColumn) modelObject).getPropertyExpression();
                if ("assigneeName".equals(propertyExpression) ||
                        "lockedBy.fullName".equals(propertyExpression) ||
                        "unlockedBy.fullName".equals(propertyExpression) ||
                        "asset.identifiedBy.fullName".equals(propertyExpression)) {
                    item.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
                }
            }
        }
        Label label = new Label(componentId, createLabelModel(rowModel));
        label.setEscapeModelStrings(false);
        item.add(label);
    }
}
