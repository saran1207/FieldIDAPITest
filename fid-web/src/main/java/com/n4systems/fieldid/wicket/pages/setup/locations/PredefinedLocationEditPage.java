package com.n4systems.fieldid.wicket.pages.setup.locations;

import com.n4systems.fieldid.service.location.LocationService;
import com.n4systems.fieldid.wicket.behavior.ConfirmBehavior;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.location.PredefinedLocation;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class PredefinedLocationEditPage extends FieldIDFrontEndPage {

    private IModel<PredefinedLocation> predefinedLocationModel;

    @SpringBean
    private LocationService locationService;

    public PredefinedLocationEditPage(PageParameters params) {
        Long id = params.get("uniqueID").toLong();
        predefinedLocationModel = new EntityModel<>(PredefinedLocation.class, id);

        Form form;

        add(form = new Form("form"));

        form.add(new RequiredTextField<String>("name", new PropertyModel<>(predefinedLocationModel, "name")));

        form.add(new OrgLocationPicker("owner", new PropertyModel<>(predefinedLocationModel, "owner")));

        form.add(new SubmitLink("saveLink"){
            @Override
            public void onSubmit() {
                locationService.saveOrUpdate(predefinedLocationModel.getObject());
                info(new FIDLabelModel("Location Updated").getObject());
                redirect("/predefinedLocations.action");
            }
        });

        form.add(new SubmitLink("removeLink") {
            @Override
            public void onSubmit() {
                locationService.archive(predefinedLocationModel.getObject());
                info(new FIDLabelModel("message.location_removed").getObject());
                redirect("/predefinedLocations.action");
            }
        }.add(new ConfirmBehavior(new FIDLabelModel("warning.removeselectedlocation"))));

        form.add(new Link<Void>("cancelLink") {
            @Override
            public void onClick() {
                redirect("/predefinedLocations.action");
            }
        });

    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.edit_location"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new BookmarkablePageLink<OwnersUsersLocationsPage>(linkId, OwnersUsersLocationsPage.class)
                .add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/newCss/component/buttons.css");
        response.renderCSSReference("style/legacy/newCss/setup/locationEdit.css");
    }
}
