package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.InfoOptionBeanPropertyModel;
import com.n4systems.fieldid.wicket.model.LocalizeAround;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.services.date.DateService;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;


public class AssetAttributeDetailsPanel extends Panel {

    private @SpringBean DateService dateService;

    public AssetAttributeDetailsPanel(String id, IModel<Asset> model) {
        super(id, model);

        Asset asset = model.getObject();

        final Set<InfoOptionBean> infoOptions = asset.getInfoOptions();

        boolean assignToEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.AssignedTo);
        Label assignedTo;
        if(asset.getAssignedUser() != null) {
            add(assignedTo = new Label("assignedTo", new PropertyModel(asset, "assignedUser.fullName")));
        } else {
            add(assignedTo = new Label("assignedTo", new FIDLabelModel("label.unassigned")));
        }
        assignedTo.setVisible(assignToEnabled);

        boolean manufacturerCertificateEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.ManufacturerCertificate);

        NonWicketLink manufactureCertificateLink;
        add(manufactureCertificateLink = new NonWicketLink("manufactureCertificateLink", "/file/downloadManufacturerCert.action?uniqueID=" + asset.getId(), new AttributeModifier("target", "_blank")));
        manufactureCertificateLink.setVisible(manufacturerCertificateEnabled && asset.getType().isHasManufactureCertificate());

        List<InfoOptionBean> infoOptionList = new LocalizeAround<List<InfoOptionBean>>(new Callable<List<InfoOptionBean>>() {
            @Override
            public List<InfoOptionBean> call() throws Exception {
                return new ArrayList<InfoOptionBean>(infoOptions);
            }
        }).call();
        Collections.sort(infoOptionList);

        add(new ListView<InfoOptionBean>("attributeListView", infoOptionList) {

            @Override
            protected void populateItem(ListItem<InfoOptionBean> item) {
                item.add(new Label("attributelabel", new PropertyModel<Object>(item.getModelObject(), "infoField.name")));
                item.add(new Label("attribute", new InfoOptionBeanPropertyModel(item.getModelObject(), "name", dateService.getDateTimeDefinition())));
            }
        });
    }
}
