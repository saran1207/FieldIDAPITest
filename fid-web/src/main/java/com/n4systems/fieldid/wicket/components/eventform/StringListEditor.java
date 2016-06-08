package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.wicket.behavior.ClickOnComponentWhenEnterKeyPressedBehavior;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
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
                        target.add(StringListEditor.this);
                    }

                    @Override
                    public String getReorderImage() {
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
        private TextField<String> addItemTextField;
        private FIDFeedbackPanel feedbackPanel;

        public AddStringForm(String id) {
            super(id);
            add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
            add(addItemTextField = new RequiredTextField<String>("string", new PropertyModel<String>(this, "string")));
            addItemTextField.setOutputMarkupId(true);
            AjaxButton addButton;
            add(addButton = new AjaxButton("addButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    getStringList().add(string);
                    string = null;
                    target.add(StringListEditor.this);
                    focusOnAddItemTextField(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
            addItemTextField.add(new ClickOnComponentWhenEnterKeyPressedBehavior(addButton));

            withValidation(addItemTextField);
        }

        private void focusOnAddItemTextField(AjaxRequestTarget target) {
            target.focusComponent(addItemTextField);
        }

    }

    protected void withValidation(TextField<String> addItemTextField) {}

}
