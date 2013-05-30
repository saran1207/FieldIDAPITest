package com.n4systems.fieldid.wicket.pages.identify.components.attributes;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.List;

public class SelectAttributeEditor extends Panel {
    public SelectAttributeEditor(String id, IModel<InfoOptionBean> infoOption) {
        super(id);

        List<InfoOptionBean> unfilteredOptions = new ArrayList<InfoOptionBean>(infoOption.getObject().getInfoField().getUnfilteredInfoOptions());

        DropDownChoice<InfoOptionBean> select = new DropDownChoice<InfoOptionBean>("select", infoOption, unfilteredOptions, new InfoOptionChoiceRenderer());
        select.setNullValid(true);

        select.setRequired(infoOption.getObject().getInfoField().isRequired());

        add(select);
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
}
