package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.service.tenant.SystemSettingsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.renderer.DateFormatChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import com.n4systems.model.tenant.SystemSettings;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class SystemSettingsPage extends FieldIDLoggedInPage {

    @SpringBean
    private SystemSettingsService systemSettingsService;

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                NavigationItemBuilder.aNavItem().label("nav.view_all").page(SystemSettingsPage.class).build()));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, SettingsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new FlatLabel(labelId, new FIDLabelModel("label.system_settings"));
    }

    public SystemSettingsPage() {
        SystemSettings systemSettings = systemSettingsService.getSystemSettings();
        add(new SystemSettingsForm("systemSettingsForm", systemSettings));
    }

    class SystemSettingsForm extends Form<SystemSettings> {

        public SystemSettingsForm(String id, SystemSettings data) {
            super(id, new CompoundPropertyModel<SystemSettings>(data));

            add(new FIDFeedbackPanel("feedbackPanel"));

            add(new CheckBox("assignedTo"));
            add(new CheckBox("proofTestIntegration"));
            add(new CheckBox("manufacturerCertificate"));
            add(new CheckBox("gpsCapture"));
            add(new DropDownChoice<String>("dateFormat", getDateFormats(), new DateFormatChoiceRenderer()).setNullValid(false));
            add(new TextField<String>("serialNumberFormat"));

            add(new Button("submitButton"));
            add(new BookmarkablePageLink<Void>("cancelLink", SettingsPage.class));
        }

        @Override
        protected void onSubmit() {
            systemSettingsService.saveSystemSettings(getModelObject());
            FieldIDSession.get().info(new FIDLabelModel("message.system_settings_updated").getObject());
            setResponsePage(SystemSettingsPage.class);
        }
    }

    public List<String> getDateFormats() {
        List<String> dateFormats = new ArrayList<String>();
        dateFormats.add("MM/dd/yy");
        dateFormats.add("dd/MM/yy");
        dateFormats.add("MM/dd/yyyy");
        dateFormats.add("dd/MM/yyyy");
        dateFormats.add("yyyy-MM-dd");
        return dateFormats;
    }

}
