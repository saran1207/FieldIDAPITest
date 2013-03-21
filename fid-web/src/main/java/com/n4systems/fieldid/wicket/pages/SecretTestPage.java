package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.wicket.components.loto.DeviceLockPicker;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.List;

public class SecretTestPage extends FieldIDAuthenticatedPage {

    private WebMarkupContainer selectedList;

    public SecretTestPage() {

        IModel<List<InfoOptionBean>> optionList = new ListModel(new ArrayList<InfoOptionBean>());
        Form form = new Form("form");
        form.add(new DeviceLockPicker("picker", optionList));
        form.add(new AjaxSubmitLink("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(selectedList);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });
        add(form);

        add(selectedList = new WebMarkupContainer("selectedList"));
        selectedList.setOutputMarkupPlaceholderTag(true);

        selectedList.add(new ListView<InfoOptionBean>("list", optionList) {
            @Override
            protected void populateItem(ListItem<InfoOptionBean> item) {
                item.add(new Label("name", new PropertyModel<InfoOptionBean>(item.getModelObject(), "infoField.name")));
                item.add(new Label("option", new PropertyModel<InfoOptionBean>(item.getModelObject(), "name")));
            }
        });

    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
        response.renderCSSReference("style/reset.css");
        response.renderCSSReference("style/site_wide.css");
        response.renderCSSReference("style/fieldid.css");
    }

}
