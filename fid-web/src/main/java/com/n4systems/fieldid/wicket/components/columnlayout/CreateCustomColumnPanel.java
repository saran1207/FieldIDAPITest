package com.n4systems.fieldid.wicket.components.columnlayout;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class CreateCustomColumnPanel extends Panel {

    private NewColumnForm newColumnForm;
    private AjaxLink addNewLink;
    private String newColumnName;

    public CreateCustomColumnPanel(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);

        add(newColumnForm = new NewColumnForm("newColumnForm"));
        newColumnForm.setVisible(false);
        add(addNewLink = new AjaxLink("addNewLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                newColumnName = null;
                setAddMode(target, true);
                target.focusComponent(newColumnForm.textField);
            }
        });
    }

    class NewColumnForm extends Form {

        private AjaxSubmitLink submitLink;
        public TextField textField;

        public NewColumnForm(String id) {
            super(id);
            setOutputMarkupPlaceholderTag(true);
            add(textField = new RequiredTextField<String>("newColumnName", new PropertyModel<String>(CreateCustomColumnPanel.this, "newColumnName")) {
                @Override
                protected void onComponentTag(ComponentTag tag) {
                    super.onComponentTag(tag);
//                    tag.put("onkeypress", "if (event.keyCode == 13) { eval(document.getElementById('"+submitLink.getMarkupId()+"').getAttribute('onclick'));return false; }");
                    tag.put("onkeypress", "if (event.keyCode == 13) { fireEvent('"+submitLink.getMarkupId()+"', 'click');return false; }");
                    tag.put("autocomplete", "off");
                }
            });
            textField.setOutputMarkupPlaceholderTag(true);
            add(submitLink = new AjaxSubmitLink("saveLink") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    NewColumnForm.this.onSubmit();
                    setAddMode(target, false);
                    onNewCustomColumnAdded(target, newColumnName);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    super.onError(target, form);
                }
            });
            add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    setAddMode(target, false);
                    target.addComponent(CreateCustomColumnPanel.this);
                }
            });
        }
    }

    protected void onNewCustomColumnAdded(AjaxRequestTarget target, String name) {
    }

    private void setAddMode(AjaxRequestTarget target, boolean addMode) {
        newColumnForm.setVisible(addMode);
        addNewLink.setVisible(!addMode);
        target.addComponent(CreateCustomColumnPanel.this);
    }

}
