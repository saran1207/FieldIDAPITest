package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.model.AssetType;
import com.n4systems.model.procedure.IsolationDeviceDescription;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.List;

public class DeviceLockPicker extends Panel {

    private IModel<AssetType> selectedDeviceType;

    private List<InfoFieldBean> attributeList = new ArrayList<InfoFieldBean>();
    private IModel<List<InfoOptionBean>> optionList;

    private TextField<String> freeformDescription;
    private DeviceAttributePanel deviceAttributePanel;

    private boolean isDevicePicker;

    private WebMarkupContainer freeformContainer;
    private WebMarkupContainer pickerContainer;

    @SpringBean
    private AssetTypeService assetTypeService;

    public DeviceLockPicker(String id, IModel<IsolationDeviceDescription> deviceDescriptionModel, boolean isDevicePicker) {
        super(id, deviceDescriptionModel);

        this.selectedDeviceType = new PropertyModel<AssetType>(deviceDescriptionModel, "assetType");
        this.optionList = new PropertyModel<List<InfoOptionBean>>(deviceDescriptionModel, "attributeValues");
        this.isDevicePicker = isDevicePicker;
        this.attributeList.add(new InfoFieldBean());

        freeformContainer = new WebMarkupContainer("freeformContainer");
        pickerContainer = new WebMarkupContainer("pickerContainer");

        freeformContainer.add(freeformDescription = new TextField<String>("freeformDescription", new PropertyModel<String>(deviceDescriptionModel, "freeformDescription")));
        freeformContainer.add(new AjaxLink<Void>("specifyLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                freeformContainer.setVisible(false);
                pickerContainer.setVisible(true);
                freeformDescription.setDefaultModelObject(null);
                target.add(DeviceLockPicker.this);
            }
        });
        FidDropDownChoice<AssetType> assetTypes;
        pickerContainer.add(assetTypes = (FidDropDownChoice<AssetType>) new FidDropDownChoice<AssetType>("assetTypes", selectedDeviceType, new DeviceListModel(), new ListableChoiceRenderer<AssetType>()));
        assetTypes.setNullValid(true);
        assetTypes.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if(selectedDeviceType.getObject() == null) {
                    deviceAttributePanel.setVisible(false);
                }
                resetAttributeList();
                deviceAttributePanel.resetAttributeAndOptions();
                target.add(DeviceLockPicker.this);
            }
        });

        pickerContainer.add(new AjaxLink<Void>("refineLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                if (selectedDeviceType.getObject() != null) {
                    deviceAttributePanel.setVisible(true);
                } else {
                    deviceAttributePanel.setVisible(false);
                }
                target.add(deviceAttributePanel);
            }
        });

        pickerContainer.add(new AjaxLink<Void>("cancelLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                resetPicker();
                target.add(DeviceLockPicker.this);
            }
        });

        pickerContainer.add(deviceAttributePanel = new DeviceAttributePanel("attributeSelector", selectedDeviceType, new PropertyModel<List<InfoOptionBean>>(this, "attributeList")) {
            @Override
            public void onAddAttribute(AjaxRequestTarget target) {
                attributeList.add(new InfoFieldBean());
                target.add(deviceAttributePanel);
            }

            @Override
            public void onAttributeSelected(int index, InfoFieldBean newSelection, AjaxRequestTarget target) {
                attributeList.remove(index);
                attributeList.add(index, newSelection);
                target.add(deviceAttributePanel);
            }

            @Override
            public void onOptionSelected(List<IModel<List<InfoOptionBean>>> selectedOptions, AjaxRequestTarget target) {
                updateOptions(selectedOptions);
                target.add(deviceAttributePanel);
                onPickerUpdated();
            }

            @Override
            public void onDeleteAttribute(AjaxRequestTarget target, int index, List<IModel<List<InfoOptionBean>>> selectedOptions) {
                attributeList.remove(index);
                updateOptions(selectedOptions);
                target.add(deviceAttributePanel);
            }
        });
        deviceAttributePanel.setOutputMarkupPlaceholderTag(true);
        deviceAttributePanel.setVisible(false);

        pickerContainer.setVisible(false);

        add(freeformContainer);
        add(pickerContainer);
        setOutputMarkupId(true);
    }

    public void resetPicker() {
        freeformContainer.setVisible(true);
        pickerContainer.setVisible(false);
        deviceAttributePanel.setVisible(false);
        selectedDeviceType.setObject(null);
        resetAttributeList();
    }

    private void updateOptions(List<IModel<List<InfoOptionBean>>> selectedOptions) {
        List<InfoOptionBean> newList = new ArrayList<InfoOptionBean>();

        for (IModel<List<InfoOptionBean>> list : selectedOptions) {
            newList.addAll(list.getObject());
        }
        optionList.setObject(newList);
    }

    private void resetAttributeList() {
        attributeList.clear();
        attributeList.add(new InfoFieldBean());
        optionList.setObject(new ArrayList<InfoOptionBean>());
    }

    private class DeviceListModel extends LoadableDetachableModel<List<AssetType>> {
        @Override
        protected List<AssetType> load() {
            if (isDevicePicker) {
                return assetTypeService.getLotoDevices();
            } else {
                return assetTypeService.getLotoLocks();
            }
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/newCss/component/device_lock_picker.css");
    }

    public void onPickerUpdated() {}
}
