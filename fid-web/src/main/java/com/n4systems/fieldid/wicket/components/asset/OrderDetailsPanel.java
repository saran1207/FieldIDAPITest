package com.n4systems.fieldid.wicket.components.asset;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.LineItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class OrderDetailsPanel extends Panel {

    private DateFormat df = new SimpleDateFormat(FieldIDSession.get().getSessionUser().getDateFormat());

    public OrderDetailsPanel(String id, IModel<Asset> model) {
        super(id, model);

        Asset asset = model.getObject();
        LineItem shopOrder = asset.getShopOrder();

        boolean integrationEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.Integration);
        boolean orderDetailsEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.OrderDetails);
        
        Label orderNumber;
        if(orderDetailsEnabled && !integrationEnabled) {
            add(orderNumber = new Label("orderNumber", asset.getNonIntergrationOrderNumber()));
        } else {
            add(orderNumber = new Label("orderNumber", shopOrder != null ? shopOrder.getOrder().getOrderNumber() : ""));
        }

        Label assetCode;
        add(assetCode = new Label("assetCode", shopOrder != null ? shopOrder.getAssetCode() : ""));
        add(new Label("quantity", shopOrder != null ? String.valueOf(shopOrder.getQuantity()) : ""));
        add(new Label("orderDate", shopOrder != null ? df.format(shopOrder.getOrder().getOrderDate()) : ""));
        add(new Label("orderDescription", shopOrder != null ? shopOrder.getDescription() : ""));
        assetCode.setVisible(shopOrder != null);

        Label poNumber;
        add(poNumber = new Label("purchaseOrder", asset.getPurchaseOrder()));
        poNumber.setVisible(orderDetailsEnabled || integrationEnabled);

    }
}
