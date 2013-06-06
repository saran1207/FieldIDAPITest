package com.n4systems.fieldid.wicket.pages.identify.components.attributes;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.List;

public class SelectAttributeEditor extends FormComponentPanel<InfoOptionBean> {

    private InfoOptionBean infoOption;


    public SelectAttributeEditor(String id, IModel<InfoOptionBean> infoOptionModel) {
        super(id);
        this.infoOption = infoOptionModel.getObject();

        List<InfoOptionBean> unfilteredOptions = new ArrayList<InfoOptionBean>(infoOption.getInfoField().getUnfilteredInfoOptions());

        DropDownChoice<InfoOptionBean> select = new DropDownChoice<InfoOptionBean>("select", new PropertyModel<InfoOptionBean>(this,"infoOption"), unfilteredOptions, new InfoOptionChoiceRenderer());
        select.setNullValid(true);

        select.setRequired(infoOption.getInfoField().isRequired());
        add(select);

        if (infoOption.getName() == null) {
            infoOption = null;
        }
    }

    static class InfoOptionChoiceRenderer implements IChoiceRenderer<InfoOptionBean> {
        @Override
        public Object getDisplayValue(InfoOptionBean object) {
            return object.getName();
        }

        @Override
        public String getIdValue(InfoOptionBean object, int index) {
            return object.getUniqueID() == null ? "null" : object.getUniqueID().toString();
        }
    }

    @Override
    protected void convertInput() {
        setModelObject(infoOption);
    }
}
