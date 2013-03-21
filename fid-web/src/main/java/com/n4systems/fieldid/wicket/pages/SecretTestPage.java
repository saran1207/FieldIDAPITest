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

    private WebMarkupContainer selectedDeviceList;
    private WebMarkupContainer selectedLockList;

    public SecretTestPage() {

        IModel<List<InfoOptionBean>> deviceOptionList = new ListModel(new ArrayList<InfoOptionBean>());
        Form deviceForm = new Form("deviceForm");
        deviceForm.add(new DeviceLockPicker("picker", deviceOptionList, true));
        deviceForm.add(new AjaxSubmitLink("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(selectedDeviceList);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });
        add(deviceForm);

        add(selectedDeviceList = new WebMarkupContainer("selectedDeviceList"));
        selectedDeviceList.setOutputMarkupPlaceholderTag(true);

        selectedDeviceList.add(new ListView<InfoOptionBean>("list", deviceOptionList) {
            @Override
            protected void populateItem(ListItem<InfoOptionBean> item) {
                item.add(new Label("name", new PropertyModel<InfoOptionBean>(item.getModelObject(), "infoField.name")));
                item.add(new Label("option", new PropertyModel<InfoOptionBean>(item.getModelObject(), "name")));
            }
        });

        IModel<List<InfoOptionBean>> lockOptionList = new ListModel(new ArrayList<InfoOptionBean>());
        Form lockForm = new Form("lockForm");
        lockForm.add(new DeviceLockPicker("picker", deviceOptionList, false));
        lockForm.add(new AjaxSubmitLink("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(selectedLockList);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });
        add(lockForm);

        add(selectedLockList = new WebMarkupContainer("selectedLockList"));
        selectedLockList.setOutputMarkupPlaceholderTag(true);

        selectedLockList.add(new ListView<InfoOptionBean>("list", deviceOptionList) {
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
