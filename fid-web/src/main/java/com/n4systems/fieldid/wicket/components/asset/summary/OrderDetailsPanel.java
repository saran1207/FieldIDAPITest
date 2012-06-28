package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketIframeLink;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.LineItem;
import org.apache.wicket.markup.html.IHeaderResponse;
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
        } else if(shopOrder != null) {
            add(orderNumber = new Label("orderNumber",  shopOrder.getOrder().getOrderNumber()));
        } else {
            add(orderNumber = new Label("orderNumber"));
            orderNumber.setVisible(false);
        }

        Label assetCode;
        add(assetCode = new Label("assetCode", shopOrder != null ? shopOrder.getAssetCode() : ""));
        add(new Label("quantity", shopOrder != null ? String.valueOf(shopOrder.getQuantity()) : ""));
        add(new Label("orderDate", shopOrder != null ? (shopOrder.getOrder().getOrderDate() != null? df.format(shopOrder.getOrder().getOrderDate()) : "") : ""));
        add(new Label("orderDescription", shopOrder != null ? shopOrder.getDescription() : ""));
        assetCode.setVisible(shopOrder != null);

        Label poNumber;
        add(poNumber = new Label("purchaseOrder", asset.getPurchaseOrder()));
        poNumber.setVisible(orderDetailsEnabled || integrationEnabled);

        NonWicketIframeLink connectToOrderLink;
        add(connectToOrderLink = new NonWicketIframeLink("connectToOrder", "aHtml/orders.action?asset=" + asset.getId(), false, null, null, false, "onComplete: setupMarryOrder"));
        connectToOrderLink.setVisible(shopOrder == null && integrationEnabled);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/common-jquery.js");
        response.renderJavaScriptReference("javascript/marryOrder-jquery.js");
        response.renderOnLoadJavaScript("ordersUrl='" + ContextAbsolutizer.toContextAbsoluteUrl("/aHtml/orders.action") + "'");
        response.renderOnLoadJavaScript("marryOrderUrl='"+ ContextAbsolutizer.toContextAbsoluteUrl("/ajax/marryOrder.action"+"'"));
    }
}
