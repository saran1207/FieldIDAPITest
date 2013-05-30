package com.n4systems.fieldid.wicket.pages.identify.components;

import com.n4systems.fieldid.wicket.pages.identify.components.attributes.ComboBoxAttributeEditor;
import com.n4systems.fieldid.wicket.pages.identify.components.attributes.DateAttributeEditor;
import com.n4systems.fieldid.wicket.pages.identify.components.attributes.SelectAttributeEditor;
import com.n4systems.fieldid.wicket.pages.identify.components.attributes.TextAttributeEditor;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.AssetType;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class AttributesEditPanel extends Panel {
    IModel<AssetType> assetTypeModel;
    List<InfoOptionBean> infoOptions;

    public AttributesEditPanel(String id, IModel<AssetType> assetTypeModel) {
        super(id);
        this.assetTypeModel = assetTypeModel;

        refreshInfoOptions();

        add(new ListView<InfoOptionBean>("attributes", new PropertyModel<List<InfoOptionBean>>(this, "infoOptions")) {
            @Override
            protected void populateItem(ListItem<InfoOptionBean> item) {
                InfoOptionBean infoOption = item.getModelObject();
                PropertyModel<InfoFieldBean> infoFieldModel = ProxyModel.of(item.getModel(), on(InfoOptionBean.class).getInfoField());
                item.add(new Label("attributeName", ProxyModel.of(infoFieldModel, on(InfoFieldBean.class).getName())));
                item.add(new WebMarkupContainer("requiredIndicator").setVisible(infoOption.getInfoField().isRequired()));
                InfoFieldBean.InfoFieldType fieldType = infoOption.getInfoField().getType();
                switch(fieldType) {
                    case ComboBox:
                        item.add(new ComboBoxAttributeEditor("attributeEditor", item.getModel()));
                        break;
                    case DateField:
                        item.add(new DateAttributeEditor("attributeEditor", item.getModel()));
                        break;
                    case SelectBox:
                        item.add(new SelectAttributeEditor("attributeEditor", item.getModel()));
                        break;
                    case TextField:
                        item.add(new TextAttributeEditor("attributeEditor", item.getModel()));
                        break;
                    case UnitOfMeasure:
                        item.add(new WebMarkupContainer("attributeEditor"));
                        break;
                }
            }
        });
    }

    public void refreshInfoOptions() {
        List<InfoFieldBean> infoFields = new ArrayList<InfoFieldBean>(assetTypeModel.getObject().getInfoFields());
        Collections.sort(infoFields, new Comparator<InfoFieldBean>(){
            @Override
            public int compare(InfoFieldBean infoFieldBean, InfoFieldBean infoFieldBean2) {
                return infoFieldBean.getWeight().compareTo(infoFieldBean2.getWeight());
            }
        });
        infoOptions = new ArrayList<InfoOptionBean>(infoFields.size());
        for (InfoFieldBean infoField : infoFields) {
            InfoOptionBean infoOption = new InfoOptionBean();
            infoOption.setInfoField(infoField);
            infoOptions.add(infoOption);
        }
    }
}
