package com.n4systems.fieldid.wicket.components.reporting.columns.display;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.table.UnescapedLabelPropertyColumn;
import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityLevel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EventRfidNumberPropertyColumn<T> extends UnescapedLabelPropertyColumn<T> {

    public EventRfidNumberPropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression) {
        super(displayModel, sortProperty, propertyExpression);
    }

    @Override
    protected IModel<?> createLabelModel(IModel<T> rowModel) {
        IModel<?> labelModel = super.createLabelModel(rowModel);
        Asset asset = (Asset) labelModel.getObject();

		if (asset.getRfidNumber() == null) {
			return new Model<String>("");
		}

		SecurityLevel level = asset.getSecurityLevel(FieldIDSession.get().getSessionUser().getSecurityFilter().getOwner());

		// build the asset info link for local assets, just show the serial for network assets
		String rfidNumber;
		if (level.isLocal()) {
			rfidNumber = String.format("<a href='asset.action?uniqueID=%d'>%s</a>", asset.getId(), asset.getRfidNumber());
		} else {
			rfidNumber = asset.getRfidNumber();
		}

		return new Model<String>(rfidNumber);
    }

}
