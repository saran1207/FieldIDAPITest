package com.n4systems.fieldid.wicket.components.setup.buttongroups;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.StatusChoiceRenderer;
import com.n4systems.model.Button;
import com.n4systems.model.EventResult;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public abstract class ButtonPanel extends Panel {

    private IModel<List<Button>> buttonListModel;
    private WebMarkupContainer container;
    private Form<Button> form;
    private AjaxLink addLink;
    private FIDFeedbackPanel feedbackPanel;

    public ButtonPanel(String id, IModel<List<Button>> buttonListModel) {
        super(id, buttonListModel);
        this.buttonListModel = buttonListModel;

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);


        add(container = new WebMarkupContainer("container"));
        container.setOutputMarkupId(true);

        container.add(new ListView<Button>("button", getButtons(buttonListModel)) {
            @Override
            protected void populateItem(ListItem<Button> item) {
                item.add(new ContextImage("buttonImage", "images/eventButtons/" + item.getModelObject().getButtonName() + ".png"));
                item.add(new FlatLabel("displayText", new PropertyModel<String>(item.getModel(), "displayText")));
                item.add(new FlatLabel("eventResult", new PropertyModel<String>(item.getModel(), "eventResult.displayName")));
                item.add(new AjaxLink<Void>("retireLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        item.getModelObject().setRetired(true);
                        didUpdateButtons();
                        target.add(container);
                    }

                    @Override
                    public boolean isVisible() {
                        return !item.getModelObject().isNew();
                    }
                });
                item.add(new AjaxLink<Void>("removeLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        buttonListModel.getObject().remove(item.getIndex());
                        addLink.setVisible(showAddLink());
                        target.add(container, addLink);
                    }

                    @Override
                    public boolean isVisible() {
                        return item.getModelObject().isNew();
                    }
                });
                if (item.getModelObject().isRetired())
                    item.add(new AttributeAppender("class", "hide").setSeparator(" "));
            }
        });

        container.add(addLink = new AjaxLink<Void>("addLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                form.setVisible(true);
                addLink.setVisible(false);
                target.add(form, addLink);
            }

        });
        addLink.setOutputMarkupId(true);
        addLink.setVisible(showAddLink());

        container.add(form = new ButtonForm("buttonForm", Model.of(new Button())));
        form.setVisible(false);

    }

    protected abstract void didUpdateButtons();

    private class ButtonForm extends Form<Button> {

        private final int NUM_BUTTONS = 14;
        private ContextImage buttonImage;
        private int imageIndex = 0;

        public ButtonForm(String id, IModel<Button> buttonModel) {
            super(id, buttonModel);
            setOutputMarkupPlaceholderTag(true);
            buttonModel.getObject().setEventResult(EventResult.PASS);

            add(buttonImage = new ContextImage("buttonImage", new PropertyModel<String>(this, "buttonImageUrl")));
            buttonImage.setOutputMarkupId(true);
            buttonImage.add(new AjaxEventBehavior("onclick") {
                @Override
                protected void onEvent(AjaxRequestTarget target) {
                    imageIndex = (imageIndex + 1) % NUM_BUTTONS;
                    target.add(buttonImage);
                }
            });

            add(new RequiredTextField<>("displayText", new PropertyModel<>(buttonModel, "displayText")));
            add(new FidDropDownChoice<>("eventResult", new PropertyModel<>(buttonModel, "eventResult"), EventResult.getValidEventResults(), new StatusChoiceRenderer()));

            add(new AjaxSubmitLink("saveLink") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Button button = getModelObject();
                    button.setTenant(FieldIDSession.get().getTenant());
                    button.setButtonName("btn" + imageIndex);
                    buttonListModel.getObject().add(button);
                    form.setVisible(false);
                    ButtonForm.this.reset();
                    addLink.setVisible(showAddLink());
                    didUpdateButtons();
                    target.add(container, feedbackPanel, ButtonPanel.this.form, addLink);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });

        }

        public String getButtonImageUrl() {
            return "images/eventButtons/btn"+imageIndex+".png";
        }

        private void reset() {
            imageIndex = 0;
            Button button = new Button();
            button.setEventResult(EventResult.PASS);
            setModelObject(button);
        }


    }

    private boolean showAddLink() {
        return buttonListModel.getObject().stream().filter(b-> !b.isRetired()).count() < 6;
    }

    private LoadableDetachableModel<List<Button>> getButtons(IModel<List<Button>> buttonListModel) {
        return new LoadableDetachableModel<List<Button>>() {
            @Override
            protected List<Button> load() {
                return buttonListModel.getObject();
            }
        };
    }


}
