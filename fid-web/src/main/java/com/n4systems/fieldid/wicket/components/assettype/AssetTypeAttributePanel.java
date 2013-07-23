package com.n4systems.fieldid.wicket.components.assettype;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.model.AssetType;
import com.n4systems.model.UnitOfMeasure;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.List;
import java.util.Set;

public class AssetTypeAttributePanel extends Panel {

    @SpringBean
    private AssetTypeService assetTypeService;

    private List<InfoFieldBean> attributes = Lists.newArrayList();
    private WebMarkupContainer existingAttributesContainer;
    private ListView<InfoFieldBean> listView;

    private final static List<String> ATTRIBUTE_TYPES = Lists.newArrayList(InfoFieldBean.TEXTFIELD_FIELD_TYPE, InfoFieldBean.SELECTBOX_FIELD_TYPE, InfoFieldBean.COMBOBOX_FIELD_TYPE, InfoFieldBean.UNIT_OF_MEASURE, InfoFieldBean.DATEFIELD_FIELD_TYPE);
    private final static String DELIMITER = ",";

    public AssetTypeAttributePanel(String id, IModel<AssetType> assetTypeModel) {
        super(id, assetTypeModel);

        final AssetType assetType = assetTypeModel.getObject();
        if(!assetType.isNew()) {
            attributes.addAll(assetType.getInfoFields());
        }

        existingAttributesContainer = new WebMarkupContainer("existingAttributesContainer");
        existingAttributesContainer.setOutputMarkupId(true);

        existingAttributesContainer.add(listView = new ListView<InfoFieldBean>("existingAttributes", attributes) {

            @Override
            protected void populateItem(ListItem<InfoFieldBean> item) {

                final IModel<InfoFieldBean> model = item.getModel();
                final IModel<String> options = Model.of(new String());
                final int index = item.getIndex();

                item.add(new RequiredTextField<String>("attributeName", new PropertyModel<String>(model, "name")));

                FidDropDownChoice typeSelect;
                item.add(typeSelect = new FidDropDownChoice("attributeType", new PropertyModel<String>(model, "fieldType"), ATTRIBUTE_TYPES));
                typeSelect.setRequired(true);
                typeSelect.add(new OnChangeAjaxBehavior() {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        listView.removeAll();
                        target.add(existingAttributesContainer);
                    }
                });

                final RequiredTextField selectOptions;
                CheckBox dateOption;
                FidDropDownChoice<UnitOfMeasure> unitOfMeasureChoice;

                item.add(selectOptions = new RequiredTextField("selectOptions", options));
                selectOptions.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        String[] parsedOptions = options.getObject().split("[" + DELIMITER + "]");

                        Set<InfoOptionBean> optionsList = Sets.newHashSet();
                        Long weight = 0L;
                        for(String opt : parsedOptions) {
                            InfoOptionBean option = new InfoOptionBean();
                            option.setInfoField(model.getObject());
                            option.setName(opt.trim());
                            option.setStaticData(true);
                            option.setWeight(weight++);
                            optionsList.add(option);
                        }
                        attributes.get(index).setUnfilteredInfoOptions(optionsList);
                    }
                });
                item.add(dateOption = new CheckBox("dateOption", new PropertyModel<Boolean>(model, "includeTime")));
                item.add(unitOfMeasureChoice = new FidDropDownChoice<UnitOfMeasure>("unitOfMeasureChoice", new PropertyModel<UnitOfMeasure>(model, "unitOfMeasure"), assetTypeService.getAllUnitOfMeasures(), new ListableChoiceRenderer<UnitOfMeasure>()));
                unitOfMeasureChoice.setRequired(true);

                String type = model.getObject().getFieldType();

                if(type != null && (type.equals(InfoFieldBean.SELECTBOX_FIELD_TYPE) || type.equals(InfoFieldBean.COMBOBOX_FIELD_TYPE))) {
                    selectOptions.setVisible(true);
                    dateOption.setVisible(false);
                    unitOfMeasureChoice.setVisible(false);
                } else if (type != null && (type.equals(InfoFieldBean.DATEFIELD_FIELD_TYPE))) {
                    selectOptions.setVisible(false);
                    dateOption.setVisible(true);
                    unitOfMeasureChoice.setVisible(false);
                } else if (type != null && (type.equals(InfoFieldBean.UNIT_OF_MEASURE))) {
                    selectOptions.setVisible(false);
                    dateOption.setVisible(false);
                    unitOfMeasureChoice.setVisible(true);
                } else {
                    selectOptions.setVisible(false);
                    dateOption.setVisible(false);
                    unitOfMeasureChoice.setVisible(false);
                }

                item.add(new CheckBox("required", new PropertyModel<Boolean>(model, "required")));
            }
        }.setReuseItems(true));

        add(existingAttributesContainer);
        add(new AjaxLink<Void>("add") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                InfoFieldBean infoFieldBean = new InfoFieldBean();
                infoFieldBean.setAssetInfo(assetType);
                attributes.add(infoFieldBean);
                target.add(existingAttributesContainer);
            }
        });
    }

    public List<InfoFieldBean> getAttributes() {
        return attributes;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/newCss/assetType/assetType.css");
    }
}
