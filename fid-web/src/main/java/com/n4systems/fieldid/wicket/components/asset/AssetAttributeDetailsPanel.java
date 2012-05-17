package com.n4systems.fieldid.wicket.components.asset;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.Set;

public class AssetAttributeDetailsPanel extends Panel {

    public AssetAttributeDetailsPanel(String id, IModel<Asset> model) {
        super(id, model);
        
        Asset asset = model.getObject();

        Set<InfoOptionBean> infoOptions = asset.getInfoOptions();
        
        if(asset.getAssignedUser() != null) {
            add(new Label("assignedTo", new PropertyModel(asset, "assignedUser.fullName")));
        } else {
            add(new Label("assignedTo", new FIDLabelModel("label.unassigned")));
        }
        
        add(new ListView<InfoOptionBean>("attributeListView", new ArrayList<InfoOptionBean>(infoOptions)) {
            @Override
            protected void populateItem(ListItem<InfoOptionBean> item) {
                item.add(new Label("attributelabel", new PropertyModel<Object>(item.getModelObject(), "infoField.name")));
                item.add(new Label("attribute", new PropertyModel<Object>(item.getModelObject(), "name")));
            }
        });
    }
}
