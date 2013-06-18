package com.n4systems.fieldid.wicket.pages.identify.components;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.asset.AutoAttributeService;
import com.n4systems.fieldid.wicket.components.measure.UnitOfMeasureEditor;
import com.n4systems.fieldid.wicket.pages.identify.components.attributes.ComboBoxAttributeEditor;
import com.n4systems.fieldid.wicket.pages.identify.components.attributes.DateAttributeEditor;
import com.n4systems.fieldid.wicket.pages.identify.components.attributes.SelectAttributeEditor;
import com.n4systems.fieldid.wicket.pages.identify.components.attributes.TextAttributeEditor;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeDefinition;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.AddAssetHistory;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class AttributesEditPanel extends Panel {

    @SpringBean private AssetService assetService;
    @SpringBean private AutoAttributeService autoAttributeService;

    private IModel<AssetType> assetTypeModel;
    List<InfoOptionBean> infoOptions;

    public AttributesEditPanel(String id, IModel<AssetType> assetTypeModel) {
        super(id);
        this.assetTypeModel = assetTypeModel;
        setOutputMarkupPlaceholderTag(true);

        refreshInfoOptions();

        add(new ListView<InfoOptionBean>("attributes", new PropertyModel<List<InfoOptionBean>>(this, "infoOptions")) {
            @Override
            protected void populateItem(final ListItem<InfoOptionBean> item) {
                InfoOptionBean infoOption = item.getModelObject();
                PropertyModel<InfoFieldBean> infoFieldModel = ProxyModel.of(item.getModel(), on(InfoOptionBean.class).getInfoField());
                item.add(new Label("attributeName", ProxyModel.of(infoFieldModel, on(InfoFieldBean.class).getName())));
                item.add(new WebMarkupContainer("requiredIndicator").setVisible(infoOption.getInfoField().isRequired()));
                InfoFieldBean.InfoFieldType fieldType = infoOption.getInfoField().getType();
                switch (fieldType) {
                    case ComboBox:
                        item.add(new ComboBoxAttributeEditor("attributeEditor", item.getModel()) {
                            @Override
                            protected void onChange(AjaxRequestTarget target) {
                                performAutoAttributeAdjustments();
                                target.add(AttributesEditPanel.this);
                            }
                        });
                        break;
                    case DateField:
                        item.add(new DateAttributeEditor("attributeEditor", item.getModel()));
                        break;
                    case SelectBox:
                        item.add(new SelectAttributeEditor("attributeEditor", item.getModel()) {
                            @Override
                            protected void onChange(AjaxRequestTarget target) {
                                performAutoAttributeAdjustments();
                                target.add(AttributesEditPanel.this);
                            }
                        });
                        break;
                    case TextField:
                        if (!infoOption.getInfoField().isUsingUnitOfMeasure()) {
                            item.add(new TextAttributeEditor("attributeEditor", item.getModel()));
                        } else {
                            item.add(new UnitOfMeasureEditor("attributeEditor", item.getModel()));
                        }
                        break;
                }
            }
        });
    }

    public void refreshInfoOptions() {
        List<InfoFieldBean> infoFields = new ArrayList<InfoFieldBean>(assetTypeModel.getObject().getAvailableInfoFields());
        Collections.sort(infoFields, new Comparator<InfoFieldBean>(){
            @Override
            public int compare(InfoFieldBean infoFieldBean, InfoFieldBean infoFieldBean2) {
                return infoFieldBean.getWeight().compareTo(infoFieldBean2.getWeight());
            }
        });

        AddAssetHistory addAssetHistory = assetService.getAddAssetHistory();

        infoOptions = new ArrayList<InfoOptionBean>(infoFields.size());
        for (InfoFieldBean infoField : infoFields) {

            InfoOptionBean infoOption = null;

            if (addAssetHistory != null && addAssetHistory.getAssetType().equals(assetTypeModel.getObject())) {
                infoOption = populateAttributeValueFromHistoryIfPresent(infoField, addAssetHistory);
            }

            if (infoOption == null) {
                infoOption = new InfoOptionBean();
                infoOption.setInfoField(infoField);
            }

            infoOptions.add(infoOption);
        }
    }

    private InfoOptionBean populateAttributeValueFromHistoryIfPresent(InfoFieldBean infoField, AddAssetHistory addAssetHistory) {
        List<InfoOptionBean> historyOptions = addAssetHistory.getInfoOptions();
        for (InfoOptionBean historyOption : historyOptions) {
            if (historyOption.getInfoField().equals(infoField)) {
                if (historyOption.isStaticData()) {
                    return historyOption;
                }
                InfoOptionBean optionBean = new InfoOptionBean();
                optionBean.setInfoField(infoField);
                optionBean.setName(historyOption.getName());
                return optionBean;
            }
        }
        return null;
    }

    public List<InfoOptionBean> getEnteredInfoOptions() {
        List<InfoOptionBean> optionsWithBlanksRemoved = new ArrayList<InfoOptionBean>();
        for (InfoOptionBean infoOption : infoOptions) {
            if (infoOption != null && !StringUtils.isBlank(infoOption.getName())) {
                optionsWithBlanksRemoved.add(infoOption);
            }
        }
        return optionsWithBlanksRemoved;
    }

    private void performAutoAttributeAdjustments() {
        AutoAttributeDefinition template = autoAttributeService.findTemplateToApply(assetTypeModel.getObject(), infoOptions);

        if (template == null) {
            // No auto attribute template here!!
            return;
        }

        List<InfoOptionBean> outputs = template.getOutputs();
        List<InfoOptionBean> newAttributesList = new ArrayList<InfoOptionBean>(infoOptions.size());

        for (InfoOptionBean infoOption : infoOptions) {
            boolean foundInOutput = false;
            for (InfoOptionBean output : outputs) {
                if (output.getInfoField().getUniqueID().equals(infoOption.getInfoField().getUniqueID())) {
                    foundInOutput = true;
                    if (output.isStaticData()) {
                        newAttributesList.add(output);
                    } else {
                        infoOption.setName(output.getName());
                        newAttributesList.add(infoOption);
                    }
                    break;
                }
            }
            if (!foundInOutput) {
                // If there was no output value for this info field, we just restore whatever value we were currently editing.
                // UNLESS the Criteria definition had this field as a POTENTIAL output, in which case we want to clear whatever value was entered.
                if (template.getCriteria().getOutputs().contains(infoOption.getInfoField())) {
                    InfoOptionBean newDummyOption = new InfoOptionBean();
                    newDummyOption.setInfoField(infoOption.getInfoField());
                    newAttributesList.add(newDummyOption);
                } else {
                    newAttributesList.add(infoOption);
                }
            }
        }

        infoOptions = newAttributesList;
    }

}
