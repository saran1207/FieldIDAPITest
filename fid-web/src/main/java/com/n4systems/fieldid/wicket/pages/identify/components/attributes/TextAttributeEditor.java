package com.n4systems.fieldid.wicket.pages.identify.components.attributes;

import com.n4systems.fieldid.wicket.util.ProxyModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import rfid.ejb.entity.InfoOptionBean;

import static ch.lambdaj.Lambda.on;

public class TextAttributeEditor extends Panel {

    public TextAttributeEditor(String id, IModel<InfoOptionBean> optionBean) {
        super(id);

        TextField<String> textField = new TextField<String>("text", ProxyModel.of(optionBean, on(InfoOptionBean.class).getName()));

        textField.setRequired(optionBean.getObject().getInfoField().isRequired());

        textField.add(new AjaxFormComponentUpdatingBehavior("onblur") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            }
        });

        add(textField);
    }

}
