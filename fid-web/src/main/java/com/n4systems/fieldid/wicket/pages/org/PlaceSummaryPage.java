package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.addressinfo.AddressPanel;
import com.n4systems.fieldid.wicket.components.form.InlineEditableForm;
import com.n4systems.model.Address;
import com.n4systems.model.GpsLocation;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class PlaceSummaryPage extends PlacePage {

    // BOGUS TEST DATA
    Address address = new Address("111 queen street, east");
    String name="susan richardson",
            email="foo@bar.com",
            phone="123 456 7890",
            fax,
            notes;
    // ---------------


    private final GoogleMap map;

    public PlaceSummaryPage(PageParameters params) {
        super(params);

        //add(new GoogleMap("map",ProxyModel.of(model, on(BaseOrg.class).getGpsLocation())));
        add(map = new GoogleMap("map", Model.of(new GpsLocation(43.70263, -79.46654))));

        add(new AjaxLink("attachmentsLink") {
            @Override public void onClick(AjaxRequestTarget target) {
                //updateContent(Content.ATTACHMENTS, target);  // make this more of settings thang...?
            }
        }.add(new Label("label", "8 attachments")));
        
        add(new ContextImage("img", "images/add-photo-slate.png"));

        add(new InlineEditableForm("contact").withSaveCancelEditLinks()
                .add(new TextField("name", new PropertyModel(PlaceSummaryPage.this, "name")))
                .add(new TextField("email", new PropertyModel(PlaceSummaryPage.this, "email")))
                .add(new AddressPanel("address", new PropertyModel(PlaceSummaryPage.this, "address")).withExternalMap(map.getJsVar()))
                .add(new TextField("phone", new PropertyModel(PlaceSummaryPage.this, "phone")))
                .add(new TextField("fax", new PropertyModel(PlaceSummaryPage.this, "fax"))));

        add(new InlineEditableForm("general").withSaveCancelEditLinks()
                .add(new TextArea<String>("notes", new PropertyModel(PlaceSummaryPage.this, "notes")))
        );
    }
}
