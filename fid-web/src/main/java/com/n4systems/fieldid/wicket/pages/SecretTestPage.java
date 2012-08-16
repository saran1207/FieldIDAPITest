package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.wicket.behavior.Watermark;
import com.n4systems.fieldid.wicket.components.*;
import com.n4systems.model.Asset;
import com.n4systems.model.dashboard.widget.WorkWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.Date;

public class SecretTestPage extends FieldIDAuthenticatedPage {

    private BaseOrg org;
    private Asset asset;
    private String text;
    private String text2;
    private Date date;
    private String password;
    private User user;

    private int id=0;
    
    public SecretTestPage() {
        Form form = new Form("form", new CompoundPropertyModel(this));

        form.add(new AutoCompleteOrgPicker("autocompleteorg", new PropertyModel<BaseOrg>(this, "org")));
        form.add(new AutoCompleteSearch("autocompletesearch", new PropertyModel<Asset>(this, "asset")));
        form.add(new TextField("watermark", new PropertyModel<String>(this,"text")).add(new Watermark("enter a value")));
        form.add(new TextField("watermark2", new PropertyModel<String>(this, "text2")).add(new Watermark("enter another value")));
        form.add(new PasswordTextField("password", new PropertyModel<String>(this, "password")).add(new Watermark("enter password")));
        form.add(new Agenda("agenda", Model.of(new WorkWidgetConfiguration())));
        form.add(new AutoCompleteUser("user", new PropertyModel<User>(this, "user")));
//        form.add(new GoogleMap("map").addLocation(43.65, -79.34).addLocation(42.00, -80.00).addLocation(44.0,-79.7));
        form.add(new GoogleMap("map"));
        form.add(new DateTimePicker("dateTimePicker", new PropertyModel<Date>(this,"date"), true));
        form.add(new DateTimePicker("datePicker", new PropertyModel<Date>(this,"date"), false));

        form.add(new AjaxSubmitLink("submit") {
            @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit();
            }
            @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                // NOTE : password is required field.
                super.onError();
            }
        });

        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/reset.css");
        response.renderCSSReference("style/site_wide.css");
    }

}
