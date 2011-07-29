package com.n4systems.fieldid.wicket.components.addressinfo;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.model.AddressInfo;

public class AddressInfoInputPanel extends Panel {

    public AddressInfoInputPanel(String id, final IModel<AddressInfo> addressInfoModel) {
        super(id);
        setRenderBodyOnly(true);
        
        add(new TextField<String>("streetAddress", new PropertyModel(addressInfoModel, "streetAddress")));
        add(new TextField<String>("city", new PropertyModel(addressInfoModel, "city")));
        add(new TextField<String>("state", new PropertyModel(addressInfoModel, "state")));
        add(new TextField<String>("country", new PropertyModel(addressInfoModel, "country")));
        add(new TextField<String>("zip", new PropertyModel(addressInfoModel, "zip")));
        add(new TextField<String>("phone", new PropertyModel(addressInfoModel, "phone1")));
        add(new TextField<String>("fax", new PropertyModel(addressInfoModel, "fax1")));
    }
    
}
