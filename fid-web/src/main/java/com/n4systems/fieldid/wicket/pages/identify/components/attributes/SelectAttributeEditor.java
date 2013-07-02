package com.n4systems.fieldid.wicket.pages.identify.components.attributes;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.List;

public class SelectAttributeEditor extends FormComponentPanel<InfoOptionBean> {

    private InfoOptionBean infoOption;

    public SelectAttributeEditor(String id, IModel<InfoOptionBean> infoOptionModel, IModel<InfoFieldBean> infoFieldModel) {
        super(id, infoOptionModel);
        this.infoOption = infoOptionModel.getObject();

        List<InfoOptionBean> unfilteredOptions = new ArrayList<InfoOptionBean>(infoFieldModel.getObject().getUnfilteredInfoOptions());

        DropDownChoice<InfoOptionBean> select = new DropDownChoice<InfoOptionBean>("select", new PropertyModel<InfoOptionBean>(this,"infoOption"), unfilteredOptions, new InfoOptionChoiceRenderer());
        select.setNullValid(true);

        select.add(new ValidateIfRequiredValidator<InfoOptionBean>(infoFieldModel.getObject()));
        add(select);

        select.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                convertInput();
                updateModel();
                onChange(target);
            }
        });

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
        setConvertedInput(infoOption);
    }

    protected void onChange(AjaxRequestTarget target) {
    }

}
