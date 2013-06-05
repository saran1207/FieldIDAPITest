package com.n4systems.fieldid.wicket.pages.identify.components.attributes;

import com.n4systems.fieldid.wicket.components.measure.UnitOfMeasureEditor;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import rfid.ejb.entity.InfoOptionBean;

public class UnitMeasureAttributeEditor extends Panel {

    public UnitMeasureAttributeEditor(String id, IModel<InfoOptionBean> optionBean) {
        super(id);

        add(new UnitOfMeasureEditor("editor", optionBean));
    }

}
