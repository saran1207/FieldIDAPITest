package com.n4systems.fieldid.wicket.components.reporting.columns.display;

import com.n4systems.fieldid.wicket.components.table.UnescapedLabelPropertyColumn;
import com.n4systems.model.Asset;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class AssetLinkPropertyColumn extends UnescapedLabelPropertyColumn<Asset> {

    public AssetLinkPropertyColumn(IModel<String> displayModel, String propertyExpression) {
        super(displayModel, propertyExpression);
    }

    @Override
    protected IModel<?> createLabelModel(IModel<Asset> rowModel) {
        Asset asset = rowModel.getObject();
        return new Model<String>("<a href=\"/fieldid/asset.action?uniqueID=" +
                asset.getId() + "\" >" +
                asset.getSerialNumber() + "</a>");
    }
}
