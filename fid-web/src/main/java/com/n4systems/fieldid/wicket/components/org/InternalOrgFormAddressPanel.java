package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.model.AddressInfo;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class InternalOrgFormAddressPanel extends Panel {

    public InternalOrgFormAddressPanel(String id, IModel<AddressInfo> addressInfo) {
        super(id, addressInfo);

        add(new TextField<String>("streetAddress", new PropertyModel<String>(addressInfo, "streetAddress")));
        add(new TextField<String>("city", new PropertyModel<String>(addressInfo, "city")));
        add(new TextField<String>("stateProvince", new PropertyModel<String>(addressInfo, "state")));
        add(new TextField<String>("country", new PropertyModel<String>(addressInfo, "country")));
        add(new TextField<String>("phone", new PropertyModel<String>(addressInfo, "phone1")));
        add(new TextField<String>("fax", new PropertyModel<String>(addressInfo, "fax1")));
    }

}
