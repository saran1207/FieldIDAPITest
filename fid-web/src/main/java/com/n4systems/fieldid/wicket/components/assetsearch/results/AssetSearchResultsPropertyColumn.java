package com.n4systems.fieldid.wicket.components.assetsearch.results;

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
public class AssetSearchResultsPropertyColumn extends FieldIdPropertyColumn {

    private boolean googleTranslateEnabled;

    public AssetSearchResultsPropertyColumn(boolean googleTranslateEnabled, IModel<String> displayModel, ColumnMappingView column, int index, boolean sortable) {
        super(displayModel, column, index, sortable);
        this.googleTranslateEnabled =  googleTranslateEnabled;
    }

    public AssetSearchResultsPropertyColumn(boolean googleTranslateEnabled, IModel<String> displayModel, ColumnMappingView column, int index) {
        super(displayModel, column, index);
        this.googleTranslateEnabled = googleTranslateEnabled;
    }

    @Override
    public void populateItem(Item<ICellPopulator<RowView>> item, String componentId, IModel<RowView> rowModel) {
        if (googleTranslateEnabled) {
            ICellPopulator<RowView> modelObject = item.getModelObject();
            if (modelObject instanceof AssetSearchResultsPropertyColumn) {
                String propertyExpression = ((AssetSearchResultsPropertyColumn) modelObject).getPropertyExpression();
                if ("identifiedBy.displayName".equals(propertyExpression) ||
                        "modifiedBy.displayName".equals(propertyExpression) ||
                        ("assignedUser.displayName".equals(propertyExpression) &&
                                !rowModel.getObject().getValues().get(item.getIndex() - 1).toString().isEmpty())) {
                    item.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
                }
            }
        }
        Label label = new Label(componentId, createLabelModel(rowModel));
        label.setEscapeModelStrings(false);
        item.add(label);
    }
}
