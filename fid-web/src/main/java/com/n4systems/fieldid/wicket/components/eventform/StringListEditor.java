package com.n4systems.fieldid.wicket.components.eventform;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class StringListEditor extends Panel {

    private ListView<String> stringList;

    public StringListEditor(String id, IModel<List<String>> listModel) {
        super(id, listModel);
        setOutputMarkupId(true);

        add(stringList = new ListView<String>("stringList", listModel) {
            @Override
            protected void populateItem(final ListItem<String> item) {
                item.add(new EditCopyDeleteItemPanel("itemEditor", item.getModel(), false) {
                    @Override
                    protected void onDeleteButtonClicked(AjaxRequestTarget target) {
                        getStringList().remove(item.getIndex());
                        target.addComponent(StringListEditor.this);
                    }

                    @Override
                    public String getContextRelativeImage() {
                        return "images/small-x.png";
                    }
                });
            }
        });
        stringList.setOutputMarkupId(true);

        add(new AddStringForm("addStringForm"));
    }

    public List<String> getStringList() {
        return (List<String>)getDefaultModelObject();
    }

    class AddStringForm extends Form {

        private String string;

        public AddStringForm(String id) {
            super(id);
            add(new RequiredTextField<String>("string", new PropertyModel<String>(this, "string")));
            add(new AjaxButton("addButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    getStringList().add(string);
                    string = null;
                    target.addComponent(StringListEditor.this);
                }
            });
        }

    }

}
