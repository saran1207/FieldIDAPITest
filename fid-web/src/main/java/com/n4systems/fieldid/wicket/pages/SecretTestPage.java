package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.wicket.behavior.Watermark;
import com.n4systems.fieldid.wicket.components.AutoCompleteOrgPicker;
import com.n4systems.fieldid.wicket.components.AutoCompleteSearch;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.model.Asset;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.List;

public class SecretTestPage extends FieldIDAuthenticatedPage {

    private BaseOrg org;
    private Asset asset;
    private String text;
    private String text2;
    private String password;

    private int id=0;
    
    public SecretTestPage() {

        List<Component> components = new ArrayList<Component>();
        Form form = new Form("form", new CompoundPropertyModel(this));
        
        form.add(new AutoCompleteOrgPicker("autocompleteorg", new PropertyModel<BaseOrg>(this, "org")));
        form.add(new AutoCompleteSearch("autocompletesearch", new PropertyModel<Asset>(this, "asset")));
        form.add(new TextField("watermark", new PropertyModel<String>(this, "text")).add(new Watermark("enter a value")));
        form.add(new TextField("watermark2", new PropertyModel<String>(this, "text2")).add(new Watermark("enter another value")));
        form.add(new PasswordTextField("password", new PropertyModel<String>(this, "password")).add(new Watermark("enter password")));
        form.add(new GoogleMap("map").addLocation(43.65, -79.34).addLocation(42.00, -80.00).addLocation(44.0,-79.7));

        add(form);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/reset.css");
        response.renderCSSReference("style/component/tabbedPanel.css");
    }

}
