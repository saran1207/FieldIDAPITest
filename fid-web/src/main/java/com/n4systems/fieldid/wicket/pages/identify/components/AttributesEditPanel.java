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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class AttributesEditPanel extends Panel {

    @SpringBean private AssetService assetService;
    @SpringBean private AutoAttributeService autoAttributeService;

    private IModel<AssetType> assetTypeModel;
    List<AttributeNameValuePair> infoOptions;

    public AttributesEditPanel(String id, IModel<AssetType> assetTypeModel) {
        super(id);
        this.assetTypeModel = assetTypeModel;
        setOutputMarkupPlaceholderTag(true);

        refreshInfoOptions();

        add(new ListView<AttributeNameValuePair>("attributes", new PropertyModel<List<AttributeNameValuePair>>(this, "infoOptions")) {
            @Override
            protected void populateItem(final ListItem<AttributeNameValuePair> item) {
                AttributeNameValuePair pair = item.getModelObject();
                final PropertyModel<InfoFieldBean> infoFieldModel = ProxyModel.of(item.getModel(), on(AttributeNameValuePair.class).getInfoField());
                PropertyModel<InfoOptionBean> infoOptionModel  = ProxyModel.of(item.getModel(), on(AttributeNameValuePair.class).getInfoOption());
                item.add(new Label("attributeName", ProxyModel.of(infoFieldModel, on(InfoFieldBean.class).getName())));
                item.add(new WebMarkupContainer("requiredIndicator").setVisible(pair.infoField.isRequired()));
                InfoFieldBean.InfoFieldType fieldType = pair.infoField.getType();

                switch (fieldType) {
                    case ComboBox:
                        item.add(new ComboBoxAttributeEditor("attributeEditor", infoOptionModel, infoFieldModel) {
                            @Override
                            protected void onChange(AjaxRequestTarget target) {
                                performAutoAttributeAdjustments(infoFieldModel);
                                target.add(AttributesEditPanel.this);
                            }
                        });
                        break;
                    case DateField:
                        item.add(new DateAttributeEditor("attributeEditor", infoOptionModel));
                        break;
                    case SelectBox:
                        item.add(new SelectAttributeEditor("attributeEditor", infoOptionModel, infoFieldModel) {
                            @Override
                            protected void onChange(AjaxRequestTarget target) {
                                performAutoAttributeAdjustments(infoFieldModel);
                                target.add(AttributesEditPanel.this);
                            }
                        });
                        break;
                    case TextField:
                        if (!infoFieldModel.getObject().isUsingUnitOfMeasure()) {
                            item.add(new TextAttributeEditor("attributeEditor", infoOptionModel));
                        } else {
                            item.add(new UnitOfMeasureEditor("attributeEditor", infoOptionModel));
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

        infoOptions = new ArrayList<AttributeNameValuePair>(infoFields.size());
        for (InfoFieldBean infoField : infoFields) {

            AttributeNameValuePair pair = new AttributeNameValuePair();
            pair.infoField = infoField;

            InfoOptionBean infoOption = null;

            if (addAssetHistory != null && addAssetHistory.getAssetType().equals(assetTypeModel.getObject())) {
                infoOption = populateAttributeValueFromHistoryIfPresent(infoField, addAssetHistory);
            }

            if (infoOption == null) {
                infoOption = new InfoOptionBean();
                infoOption.setInfoField(infoField);
            }

            pair.infoOption = infoOption;
            infoOptions.add(pair);
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
        for (AttributeNameValuePair pair : infoOptions) {
            InfoOptionBean infoOption = pair.infoOption;
            if (infoOption != null && !StringUtils.isBlank(infoOption.getName())) {
                optionsWithBlanksRemoved.add(infoOption);
            }
        }
        return optionsWithBlanksRemoved;
    }

    protected List<InfoOptionBean> getAllInfoOptions() {
        List<InfoOptionBean> allInfoOptions = new ArrayList<InfoOptionBean>();
        for (AttributeNameValuePair pair : infoOptions) {
            allInfoOptions.add(pair.getInfoOption());
        }
        return allInfoOptions;
    }

    private void performAutoAttributeAdjustments(IModel<InfoFieldBean> infoField) {
        List<InfoOptionBean> allInfoOptions = getAllInfoOptions();
        AutoAttributeDefinition template = autoAttributeService.findTemplateToApply(assetTypeModel.getObject(), allInfoOptions);

        if (template == null) {
            // No auto attribute template here!!
            return;
        }

        List<InfoOptionBean> inputs = template.getInputs();

        boolean foundInfoField = false;
        for (InfoOptionBean input : inputs) {
            if (input.getInfoField().equals(infoField.getObject())) {
                foundInfoField = true;
            }
        }

        if (!foundInfoField) {
            // We changed an attribute that's not an input in our template. We don't adjust auto attributes in this case.
            return;
        }

        List<InfoOptionBean> outputs = template.getOutputs();
        List<AttributeNameValuePair> newAttributesList = new ArrayList<AttributeNameValuePair>(infoOptions.size());

        for (InfoOptionBean infoOption : allInfoOptions) {
            boolean foundInOutput = false;
            for (InfoOptionBean output : outputs) {
                if (output.getInfoField().getUniqueID().equals(infoOption.getInfoField().getUniqueID())) {
                    foundInOutput = true;
                    if (output.isStaticData()) {
                        newAttributesList.add(createNVP(output));
                    } else {
                        infoOption.setName(output.getName());
                        newAttributesList.add(createNVP(infoOption));
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
                    newAttributesList.add(createNVP(newDummyOption));
                } else {
                    newAttributesList.add(createNVP(infoOption));
                }
            }
        }

        infoOptions = newAttributesList;
    }

    private AttributeNameValuePair createNVP(InfoOptionBean bean) {
        AttributeNameValuePair pair = new AttributeNameValuePair();
        pair.setInfoField(bean.getInfoField());
        pair.setInfoOption(bean);
        return pair;
    }

    public static class AttributeNameValuePair implements Serializable {

        public InfoFieldBean infoField;
        public InfoOptionBean infoOption;

        public InfoFieldBean getInfoField() {
            return infoField;
        }

        public void setInfoField(InfoFieldBean infoField) {
            this.infoField = infoField;
        }

        public InfoOptionBean getInfoOption() {
            return infoOption;
        }

        public void setInfoOption(InfoOptionBean infoOption) {
            this.infoOption = infoOption;
        }
    }

}
