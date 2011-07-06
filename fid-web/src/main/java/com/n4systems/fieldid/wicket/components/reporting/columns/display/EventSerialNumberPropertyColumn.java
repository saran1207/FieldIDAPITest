package com.n4systems.fieldid.wicket.components.reporting.columns.display;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.table.UnescapedLabelPropertyColumn;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.security.SecurityLevel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EventSerialNumberPropertyColumn extends UnescapedLabelPropertyColumn<Event> {

    public EventSerialNumberPropertyColumn(IModel<String> displayModel, String sortExpression, String propertyExpression) {
        super(displayModel, sortExpression, propertyExpression);
    }

    @Override
    protected IModel<?> createLabelModel(IModel<Event> rowModel) {
		Asset asset = rowModel.getObject().getAsset();

		SecurityLevel level = asset.getSecurityLevel(FieldIDSession.get().getSessionUser().getSecurityFilter().getOwner());

		// build the asset info link for local assets, just show the serial for network assets
		String serialNumber;
		if (level.isLocal()) {
			serialNumber = String.format("<a href='/fieldid/asset.action?uniqueID=%d'>%s</a>", asset.getId(), asset.getSerialNumber());
		} else {
			serialNumber = asset.getSerialNumber();
		}

		return new Model<String>(serialNumber);
    }

}
