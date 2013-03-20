package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.MultiSelectDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.model.AssetType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.*;

public class DeviceLockPicker extends Panel {

    private boolean useJChosen;
    private IModel<List<InfoOptionBean>> attributeList;
    private Map<InfoFieldBean, List<InfoOptionBean>> selectedAttributes;

    private IModel<AssetType> deviceType;
    private IModel<InfoFieldBean> attribute;
    private IModel<List<InfoOptionBean>> option;

    @SpringBean
    private AssetTypeService assetTypeService;
    private WebMarkupContainer selectedList;

    public DeviceLockPicker(String id, IModel<List<InfoOptionBean>> attributeList) {
        this(id, attributeList, true);
    }

    public DeviceLockPicker(String id, final IModel<List<InfoOptionBean>> attributeList, boolean useJChosen) {
        super(id);
        this.useJChosen = useJChosen;
        this.attributeList = attributeList;
        deviceType = Model.of(new AssetType());
        attribute = Model.of(new InfoFieldBean());
        option = new ListModel<InfoOptionBean>(new ArrayList<InfoOptionBean>());

        selectedAttributes = new HashMap<InfoFieldBean, List<InfoOptionBean>>();

        final DropDownChoice attributes;
        final MultiSelectDropDownChoice options;

        add(options = new MultiSelectDropDownChoice<InfoOptionBean>("options", option, new OptionListModel(), new InfoOptionChoiceRenderer()));
        options.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                selectedAttributes.put(attribute.getObject(), new ArrayList(option.getObject()));

                List<InfoOptionBean> newList = new ArrayList<InfoOptionBean>();

                for (List<InfoOptionBean> list: selectedAttributes.values()) {
                    newList.addAll(list);
                }

                attributeList.setObject(newList);
                target.add(selectedList);
            }
        });

        options.setVisible(!attribute.getObject().isNew());

        add(attributes = new DropDownChoice<InfoFieldBean>("attributes", attribute, new AttributeListModel(), new InfoFieldChoiceRenderer()) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

            @Override
            protected void onSelectionChanged(InfoFieldBean newSelection) {
                if(newSelection != null) {
                    attribute.setObject(newSelection);
                    options.setVisible(!attribute.getObject().isNew());
                }else {
                    options.setVisible(false);
                }

                attributeList.getObject().clear();
                selectedAttributes.clear();
            }
        });
        attributes.setVisible(!deviceType.getObject().isNew());
        attributes.setNullValid(true);

        DropDownChoice assetTypes;
        add(assetTypes = new DropDownChoice<AssetType>("assetTypes", deviceType, new DeviceListModel(), new ListableChoiceRenderer<AssetType>()){
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

            @Override
            protected void onSelectionChanged(AssetType newSelection) {
                if(newSelection != null) {
                    deviceType.setObject(newSelection);
                    attributes.setVisible(!deviceType.getObject().isNew());
                } else {
                    attributes.setVisible(false);
                }
                options.setVisible(false);
                selectedAttributes.clear();
                attributeList.getObject().clear();
            }
        });
        assetTypes.setNullValid(true);

        if (useJChosen) {
            assetTypes.add(new JChosenBehavior());
            attributes.add(new JChosenBehavior());
            options.add(new JChosenBehavior());
        }

        add(selectedList = new WebMarkupContainer("selectedList"));
        selectedList.setOutputMarkupPlaceholderTag(true);

        selectedList.add(new ListView<InfoOptionBean>("list", attributeList) {
            @Override
            protected void populateItem(ListItem<InfoOptionBean> item) {
                item.add(new Label("name", new PropertyModel<InfoOptionBean>(item.getModelObject(), "infoField.name")));
                item.add(new Label("option", new PropertyModel<InfoOptionBean>(item.getModelObject(), "name")));
            }
        });
    }

    private class DeviceListModel extends LoadableDetachableModel<List<AssetType>> {
        @Override
        protected List<AssetType> load() {
            return assetTypeService.getLotoAssetTypes();
        }
    }

    private class AttributeListModel extends LoadableDetachableModel<List<InfoFieldBean>> {

        @Override
        protected List<InfoFieldBean> load() {
            List<InfoFieldBean> selectAndComboFields = new ArrayList<InfoFieldBean>();

            if(deviceType.getObject()!= null && !deviceType.getObject().isNew()) {
                Collection<InfoFieldBean> allInfoFields = deviceType.getObject().getInfoFields();
                for (InfoFieldBean infoFieldBean: allInfoFields) {
                    if (infoFieldBean.getFieldType().equals(InfoFieldBean.SELECTBOX_FIELD_TYPE) ||
                            infoFieldBean.getFieldType().equals(InfoFieldBean.COMBOBOX_FIELD_TYPE))
                        selectAndComboFields.add(infoFieldBean);
                }
            }
            return selectAndComboFields;
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/device_lock_picker.css");
    }

    private class OptionListModel extends LoadableDetachableModel<List<InfoOptionBean>> {

        @Override
        protected List<InfoOptionBean> load() {
            return attribute.getObject().getInfoOptions();
        }
    }

    private class InfoFieldChoiceRenderer implements IChoiceRenderer<InfoFieldBean> {
        @Override
        public Object getDisplayValue(InfoFieldBean object) {
            return object.getName();
        }

        @Override
        public String getIdValue(InfoFieldBean object, int index) {
            return object.getUniqueID() + "";
        }
    }

    private class InfoOptionChoiceRenderer implements IChoiceRenderer<InfoOptionBean> {
        @Override
        public Object getDisplayValue(InfoOptionBean object) {
            return object.getName();
        }

        @Override
        public String getIdValue(InfoOptionBean object, int index) {
            return object.getUniqueID() + "";
        }
    }
}
