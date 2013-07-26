package com.n4systems.fieldid.wicket.components.assettype;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.model.AssetType;
import com.n4systems.model.UnitOfMeasure;
import com.n4systems.util.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AssetTypeAttributePanel extends Panel {

    @SpringBean
    private AssetTypeService assetTypeService;

    private List<InfoFieldInput> infoFields;
    private List<InfoOptionInput> editInfoOptions;
    private WebMarkupContainer existingAttributesContainer;
    private ListView<InfoFieldInput> listView;

    private SortableAjaxBehavior sortableAjaxBehavior;

    private final List<String> attributeTypes = getAttributeTypes();
    private final static String DELIMITER = ",";

    public AssetTypeAttributePanel(String id, IModel<AssetType> assetTypeModel) {
        super(id, assetTypeModel);
        setOutputMarkupId(true);

        final AssetType assetType = assetTypeModel.getObject();
        if(assetType.isNew()) {
            assetType.setInfoFields(Lists.<InfoFieldBean>newArrayList());
        }
        getInfoFields(assetType);
        getEditInfoOptions(assetType);

        existingAttributesContainer = new WebMarkupContainer("existingAttributesContainer");
        existingAttributesContainer.setOutputMarkupId(true);

        sortableAjaxBehavior = new SimpleSortableAjaxBehavior() {
            @Override
            public void onUpdate(Component sortedComponent, int index, AjaxRequestTarget target) {
                InfoFieldInput movedItem = (InfoFieldInput) sortedComponent.getDefaultModelObject();
                infoFields.remove(movedItem);
                infoFields.add(index-1, movedItem);
                //reorder options
                for(int i = index-1; i < infoFields.size(); i++) {
                    int oldIndex = infoFields.get(i).getWeight().intValue();
                    for (int j = 0; j < editInfoOptions.size(); j++) {
                        if(editInfoOptions.get(j).getInfoFieldIndex() == oldIndex)
                            editInfoOptions.get(j).setInfoFieldIndex(i);
                    }
                    infoFields.get(i).setWeight(Long.valueOf(i));
                }
                listView.removeAll();
                target.add(existingAttributesContainer);
            }
        };
        existingAttributesContainer.add(sortableAjaxBehavior);
        existingAttributesContainer.add(listView = new ListView<InfoFieldInput>("existingAttributes", infoFields) {

            @Override
            protected void populateItem(final ListItem<InfoFieldInput> item) {

                final Long index = new Integer(item.getIndex()).longValue();

                final IModel<InfoFieldInput> infoField = item.getModel();
                final IModel<String> options = Model.of(getOptionsAsString(index));
                final IModel<UnitOfMeasure> unitOfMeasure = Model.of(new UnitOfMeasure());
                boolean isRetired = infoField.getObject().isRetired();

                RequiredTextField name;
                FidDropDownChoice typeSelect;
                final RequiredTextField selectOptions;
                CheckBox dateOption;
                FidDropDownChoice<UnitOfMeasure> unitOfMeasureChoice;

                item.add(name = new RequiredTextField<String>("attributeName", new PropertyModel<String>(infoField, "name")));
                name.add(new UpdateComponentOnChange());
                name.setEnabled(!isRetired);


                item.add(typeSelect = new FidDropDownChoice<String>("attributeType", new PropertyModel<String>(infoField, "fieldType"), attributeTypes, new IChoiceRenderer<String>() {
                    @Override
                    public Object getDisplayValue(String object) {
                        return InfoFieldBean.InfoFieldType.valueOf(object).getLabel();
                    }

                    @Override
                    public String getIdValue(String object, int index) {
                        return object;
                    }
                }));
                typeSelect.setRequired(true);
                typeSelect.add(new OnChangeAjaxBehavior() {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        listView.removeAll();
                        target.add(existingAttributesContainer);
                    }
                });
                typeSelect.setEnabled(!isRetired);


                item.add(selectOptions = new RequiredTextField<String>("selectOptions", options));
                selectOptions.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        List<String> originalOptions = Lists.newArrayList(getOptionsAsString(index).split("[" + DELIMITER + "]"));
                        List<String> parsedOptions = Lists.newArrayList(options.getObject().split("[" + DELIMITER + "]"));

                        //Remove deleted options
                        for (String opt : originalOptions) {
                            if (!parsedOptions.contains(opt)) {
                                InfoOptionInput input = getOption(opt, index);
                                if (input.getUniqueID() != null)
                                    input.setDeleted(true);
                                else
                                    editInfoOptions.remove(input);
                            }
                        }

                        //Reorder existing options and add new options
                        for (int i = 0; i < parsedOptions.size(); i++) {
                            InfoOptionInput input = getOption(parsedOptions.get(i), index);
                            if (input != null) {
                                input.setWeight(Long.valueOf(i));
                                input.setDeleted(false);
                            } else {
                                InfoOptionInput option = new InfoOptionInput();
                                option.setInfoFieldId(infoField.getObject().getUniqueID());
                                option.setName(parsedOptions.get(i).trim());
                                option.setWeight(Long.valueOf(i));
                                option.setInfoFieldIndex(index);
                                editInfoOptions.add(option);
                            }
                        }
                    }
                });

                selectOptions.setEnabled(!isRetired);

                item.add(dateOption = new CheckBox("dateOption", new PropertyModel<Boolean>(infoField, "includeTime")));
                dateOption.setEnabled(!isRetired);
                item.add(unitOfMeasureChoice = new FidDropDownChoice<UnitOfMeasure>("unitOfMeasureChoice", unitOfMeasure, assetTypeService.getAllUnitOfMeasures(), new ListableChoiceRenderer<UnitOfMeasure>()));
                unitOfMeasureChoice.setRequired(true);
                unitOfMeasureChoice.setEnabled(!isRetired);
                unitOfMeasureChoice.add(new OnChangeAjaxBehavior() {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        item.getModelObject().setDefaultUnitOfMeasure(unitOfMeasure.getObject().getId());
                    }
                });

                String type = infoField.getObject().getFieldType();

                if (type != null && (type.equals(InfoFieldBean.InfoFieldType.SelectBox.getName()) || type.equals(InfoFieldBean.InfoFieldType.ComboBox.getName()))) {
                    selectOptions.setVisible(true);
                    dateOption.setVisible(false);
                    unitOfMeasureChoice.setVisible(false);
                } else if (type != null && (type.equals(InfoFieldBean.InfoFieldType.DateField.getName()))) {
                    selectOptions.setVisible(false);
                    dateOption.setVisible(true);
                    unitOfMeasureChoice.setVisible(false);
                } else if (type != null && (type.equals(InfoFieldBean.InfoFieldType.UnitOfMeasure.getName()))) {
                    selectOptions.setVisible(false);
                    dateOption.setVisible(false);
                    unitOfMeasureChoice.setVisible(true);
                } else {
                    selectOptions.setVisible(false);
                    dateOption.setVisible(false);
                    unitOfMeasureChoice.setVisible(false);
                }

                item.add(new CheckBox("required", new PropertyModel<Boolean>(infoField, "required")).setEnabled(!isRetired));

                item.add(new AjaxLink<Void>("delete") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        infoFields.remove(index.intValue());
                        listView.removeAll();
                        target.add(existingAttributesContainer);
                    }
                }.setVisible(infoField.getObject().getUniqueID() == null));

                item.add(new AjaxLink<Void>("retire") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        infoFields.get(index.intValue()).setRetired(true);
                        listView.removeAll();
                        target.add(existingAttributesContainer);
                    }
                }.setVisible(infoField.getObject().getUniqueID() != null && !isRetired));

                item.add(new AjaxLink<Void>("unretire") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        infoFields.get(index.intValue()).setRetired(false);
                        listView.removeAll();
                        target.add(existingAttributesContainer);
                    }
                }.setVisible(infoField.getObject().getUniqueID() != null && isRetired));
                item.setOutputMarkupId(true);
            }

            private String getOptionsAsString(Long infoFieldIndex) {
                String options;
                if (!assetType.isNew()) {
                    List<String> optList = Lists.newArrayList();
                    for (InfoOptionInput opt : editInfoOptions) {
                        if (opt.getInfoFieldIndex().equals(infoFieldIndex) && !opt.isDeleted())
                            optList.add(opt.getName());
                    }
                    options = StringUtils.concat(optList, DELIMITER + " ");
                } else {
                    options = "";
                }
                return options;
            }
        }.setReuseItems(true));

        add(existingAttributesContainer);
        add(new AjaxLink<Void>("add") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                InfoFieldInput infoFieldBean = new InfoFieldInput();
                infoFields.add(infoFieldBean);
                target.add(existingAttributesContainer);
            }
        });

    }

    public List<InfoFieldInput> getInfoFields(AssetType assetType) {
        if (infoFields == null) {
            infoFields = new ArrayList<InfoFieldInput>();
            Iterator<InfoFieldBean> iter = assetType.getInfoFields().iterator();
            while (iter.hasNext()) {
                InfoFieldBean infoField = iter.next();
                infoFields.add(new InfoFieldInput(infoField));

            }
        }
        return infoFields;
    }

    public List<InfoOptionInput> getEditInfoOptions(AssetType assetType) {
        if (editInfoOptions == null) {
            editInfoOptions = new ArrayList<InfoOptionInput>();

            List<InfoFieldBean> infoFieldList = new ArrayList<InfoFieldBean>(assetType.getInfoFields());
            for (int index = 0; index < infoFieldList.size(); index++) {
                for (InfoOptionBean infoOption : infoFieldList.get(index).getInfoOptions()) {
                    InfoOptionInput infoOptionInput = new InfoOptionInput(infoOption);
                    infoOptionInput.setInfoFieldIndex((long) index);
                    editInfoOptions.add(infoOptionInput);
                }
            }

        }
        return editInfoOptions;
    }

    private InfoOptionInput getOption(String opt, Long index) {
        for (InfoOptionInput option: editInfoOptions) {
            if (option.getName().equals(opt.trim()) && option.getInfoFieldIndex().equals(index)) {
                return option;
            }
        }
        return null;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/newCss/assetType/assetType.css");
    }

    public List<String> getAttributeTypes() {
        List<String> attributeTypes = Lists.newArrayList();
        for (InfoFieldBean.InfoFieldType type: InfoFieldBean.InfoFieldType.values()) {
            attributeTypes.add(type.getName());
        }
        return attributeTypes;
    }
}
