package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.model.AssetType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.List;

public class DeviceLockPicker extends Panel {

    private IModel<AssetType> deviceType;

    private List<InfoFieldBean> attributeList = new ArrayList<InfoFieldBean>();

    private DeviceAttributePanel deviceAttributePanel;

    private IModel<List<InfoOptionBean>> optionList;

    private AssetType selectedDevice = new AssetType();

    @SpringBean
    private AssetTypeService assetTypeService;

    public DeviceLockPicker(String id, final IModel<List<InfoOptionBean>> optionList) {
        super(id);
        this.optionList = optionList;
        deviceType = Model.of(new AssetType());
        attributeList.add(new InfoFieldBean());

        FidDropDownChoice assetTypes;
        add(assetTypes = new FidDropDownChoice<AssetType>("assetTypes", deviceType, new DeviceListModel(), new ListableChoiceRenderer<AssetType>()));
        assetTypes.setNullValid(true);

        assetTypes.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                deviceAttributePanel.setVisible(deviceType.getObject() != null);
                resetAttributeList();
                deviceAttributePanel.resetAttributeAndOptions();
                target.add(deviceAttributePanel);
            }
        });

        add(deviceAttributePanel = new DeviceAttributePanel("attributeSelector", deviceType, new PropertyModel<List<InfoOptionBean>>(this, "attributeList")){
            @Override
            public void onAddAttribute(AjaxRequestTarget target) {
                attributeList.add(new InfoFieldBean());
                target.add(deviceAttributePanel);
            }

            @Override
            public void onAttributeSelected(int index, InfoFieldBean newSelection) {
                attributeList.remove(index);
                attributeList.add(index, newSelection);
            }

            @Override
            public void onOptionSelected(List<IModel<List<InfoOptionBean>>> selectedOptions) {
                List<InfoOptionBean> newList = new ArrayList<InfoOptionBean>();

                for (IModel<List<InfoOptionBean>> list : selectedOptions) {
                    newList.addAll(list.getObject());
                }

                optionList.setObject(newList);
            }
        });
        deviceAttributePanel.setOutputMarkupPlaceholderTag(true);
        deviceAttributePanel.setVisible(false);

    }

    private void resetAttributeList() {
        attributeList.clear();
        attributeList.add(new InfoFieldBean());
        optionList.setObject(new ArrayList<InfoOptionBean>());
    }

    private class DeviceListModel extends LoadableDetachableModel<List<AssetType>> {
        @Override
        protected List<AssetType> load() {
            return assetTypeService.getLotoAssetTypes();
        }
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/device_lock_picker.css");
    }
}
