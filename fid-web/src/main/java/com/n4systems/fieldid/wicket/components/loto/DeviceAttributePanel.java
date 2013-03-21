package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.MultiSelectDropDownChoice;
import com.n4systems.model.AssetType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DeviceAttributePanel extends Panel {

    private IModel<AssetType> deviceType;

    private IModel<List<InfoOptionBean>> optionList;
    private List<IModel<InfoFieldBean>> selectedAttributes = new ArrayList<IModel<InfoFieldBean>>();
    private List<IModel<List<InfoOptionBean>>> selectedOptions = new ArrayList<IModel<List<InfoOptionBean>>>();

    private AjaxLink addLink;

    @SpringBean
    private PersistenceService persistenceService;

    public DeviceAttributePanel(String id, final IModel<AssetType> deviceType, IModel<List<InfoOptionBean>> optionList) {
        super(id, optionList);
        this.deviceType = deviceType;
        this.optionList = optionList;
        addNewAttribute();

        final ListView<InfoFieldBean> attributeListView;
        add(attributeListView = new ListView<InfoFieldBean>("attributeList", getListModel()) {

            @Override
            protected void populateItem(final ListItem<InfoFieldBean> item) {

                final FidDropDownChoice attributesChoice;
                final MultiSelectDropDownChoice options;

                final int index = item.getIndex();

                item.add(options = new MultiSelectDropDownChoice<InfoOptionBean>("options", selectedOptions.get(index), getOptionListModel(selectedAttributes.get(index).getObject()), new InfoOptionChoiceRenderer()));
                options.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        onOptionSelected(selectedOptions);
                    }
                });
                if(selectedAttributes.get(index).getObject() != null)
                    options.setVisible(!selectedAttributes.get(index).getObject().isNew());
                else
                    options.setVisible(false);

                item.add(attributesChoice = new FidDropDownChoice<InfoFieldBean>("attributes", selectedAttributes.get(index), new AvailableAttributeListModel(index), new InfoFieldChoiceRenderer()) {
                    @Override
                    protected boolean wantOnSelectionChangedNotifications() {
                        return true;
                    }

                    @Override
                    protected void onSelectionChanged(InfoFieldBean newSelection) {
                        if (newSelection != null) {
                            selectedAttributes.get(index).setObject(newSelection);
                            options.setModel(getOptionListModel(newSelection));
                            options.setVisible(true);
                            onAttributeSelected(index, newSelection);
                        } else {
                            selectedOptions.get(index).setObject(new ArrayList<InfoOptionBean>());
                            options.setVisible(false);
                            onOptionSelected(selectedOptions);
                        }
                    }
                });
                attributesChoice.setNullValid(true);

                item.add(new AjaxLink<Void>("deleteLink") {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        selectedAttributes.remove(index);
                        selectedOptions.remove(index);
                        onDeleteAttribute(target, index, selectedOptions);
                    }
                });

            }
        });

        add(addLink = new AjaxLink<Void>("addLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                addNewAttribute();
                onAddAttribute(target);
            }

            @Override
            public boolean isVisible() {
                if (deviceType.getObject() != null) {
                    persistenceService.reattach(deviceType.getObject());
                    return selectedAttributes.size() < deviceType.getObject().getInfoFields().size();
                } else
                    return false;
            }
        });

    }

    private void addNewAttribute() {
        selectedAttributes.add(Model.of(new InfoFieldBean()));
        selectedOptions.add(new ListModel<InfoOptionBean>(new ArrayList<InfoOptionBean>()));
    }

    protected void resetAttributeAndOptions() {
        selectedAttributes.clear();
        selectedOptions.clear();
        addNewAttribute();
    }

    public void onAddAttribute(AjaxRequestTarget target) {};

    public void onAttributeSelected(int index, InfoFieldBean newSelection) {};

    public void onOptionSelected(List<IModel<List<InfoOptionBean>>> selectedOptions) {};

    public void onDeleteAttribute(AjaxRequestTarget target, int index, List<IModel<List<InfoOptionBean>>> selectedOptions) {};

    private IModel<List<InfoFieldBean>> getListModel() {
        return (IModel<List<InfoFieldBean>>) getDefaultModel();
    }

    private class AvailableAttributeListModel extends LoadableDetachableModel<List<InfoFieldBean>> {

        private int index;

        public AvailableAttributeListModel(int index) {
            this.index = index;
        }

        @Override
        protected List<InfoFieldBean> load() {
            List<InfoFieldBean> selectAndComboFields = new ArrayList<InfoFieldBean>();

            if(deviceType.getObject()!= null && !deviceType.getObject().isNew()) {
                Collection<InfoFieldBean> allInfoFields = deviceType.getObject().getInfoFields();
                for (InfoFieldBean infoFieldBean: allInfoFields) {
                    if ((infoFieldBean.getFieldType().equals(InfoFieldBean.SELECTBOX_FIELD_TYPE)
                            || infoFieldBean.getFieldType().equals(InfoFieldBean.COMBOBOX_FIELD_TYPE))
                            && !isSelected(infoFieldBean, index)) {
                        selectAndComboFields.add(infoFieldBean);
                    }
                }
            }
            return selectAndComboFields;
        }
    }

    private boolean isSelected(InfoFieldBean infoFieldBean, int index) {
        for(IModel<InfoFieldBean> attributeModel: selectedAttributes) {
            if(infoFieldBean.equals(selectedAttributes.get(index).getObject())) {
                return false;
            }
            if(attributeModel.getObject() != null  && attributeModel.getObject().equals(infoFieldBean)) {
                return true;
            }
        }
        return false;
    }

    private LoadableDetachableModel<List<InfoOptionBean>> getOptionListModel(final InfoFieldBean attribute) {
        return new LoadableDetachableModel<List<InfoOptionBean>>() {

            @Override
            protected List<InfoOptionBean> load() {
                return attribute.getInfoOptions();
            }
        };
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
