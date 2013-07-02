package com.n4systems.fieldid.wicket.pages.identify.components.attributes;

import com.n4systems.fieldid.wicket.components.measure.UnitOfMeasureEditor;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import rfid.ejb.entity.InfoOptionBean;

public class UnitMeasureAttributeEditor extends Panel {

    public UnitMeasureAttributeEditor(String id, IModel<InfoOptionBean> optionBean) {
        super(id);

        UnitOfMeasureEditor uomEditor = new UnitOfMeasureEditor("editor", optionBean);
        uomEditor.add(new ValidateIfRequiredValidator<InfoOptionBean>(optionBean.getObject().getInfoField()));
        add(uomEditor);
    }

}
