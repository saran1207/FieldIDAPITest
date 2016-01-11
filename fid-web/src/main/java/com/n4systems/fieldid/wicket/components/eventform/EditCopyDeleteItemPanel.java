package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.wicket.behavior.ClickOnComponentWhenEnterKeyPressedBehavior;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.TrimmedStringModel;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.validator.StringValidator;


public class EditCopyDeleteItemPanel extends Panel {
    private static final Logger logger = Logger.getLogger(EditCopyDeleteItemPanel.class);

    private static final String DELETE_IMAGE = "images/small-x.png";
    private static final String REORDER_IMAGE = "images/reorder.png";
    private static final Integer DEFAULT_TEXT_DISPLAY_LIMIT = 23;

    private WebMarkupContainer editFormContainer;
    private WebMarkupContainer viewContainer;
    private FlatLabel storeLabel;
    private TextField<String> newText;

    public EditCopyDeleteItemPanel(String id, IModel<String> stringModel) {
        this(id, stringModel, true);
    }

    public EditCopyDeleteItemPanel(String id, IModel<String> stringModel, boolean displayCopyLink) {
        this(id, stringModel, null, displayCopyLink, false);
    }

    public EditCopyDeleteItemPanel(String id, IModel<String> titleModel, IModel<String> subTitleModel) {
        this(id, titleModel, subTitleModel, true, false);
    }

    public EditCopyDeleteItemPanel(String id, IModel<String> titleModel, IModel<String> subTitleModel, final boolean displayCopyLink) {
        this(id, titleModel, subTitleModel, displayCopyLink, false);
    }

    public EditCopyDeleteItemPanel(String id, IModel<String> titleModel, IModel<String> subTitleModel, final boolean displayCopyLink, final boolean displayAddLogicLink) {
        super(id, titleModel);        
        setOutputMarkupPlaceholderTag(true);

        ContextImage deleteImage = new ContextImage("deleteImage", new PropertyModel<>(this, "deleteImage")) {
            @Override
            public boolean isVisible() {
                return !isReorderState() && isDeletable();
            }
        };
        deleteImage.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                if (!isReorderState())
                    onDeleteButtonClicked(target);
            }
        });
        add(deleteImage);

        ContextImage reorderImage = new ContextImage("reorderImage", new PropertyModel<>(this, "reorderImage")) {
            @Override
            public boolean isVisible() {
                return isReorderState();
            }
        };
        add(reorderImage);

        AjaxLink viewLink;
        viewContainer = new WebMarkupContainer("viewContainer");
        viewContainer.add(viewLink = new AjaxLink("viewLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                if (!isReorderState())
                    onViewLinkClicked(target);
            }
        });
        viewLink.add(new Label("linkLabel", new TrimmedStringModel(titleModel, getTextDisplayLimit())));
        if (subTitleModel != null) {
            viewLink.add(new Label("subTitle", subTitleModel));
        } else {
            viewLink.add(new WebMarkupContainer("subTitle").setVisible(false));
        }

        viewContainer.add(new AjaxLink("editLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setEditState(target);
            }

            @Override
            public boolean isVisible() {
                return !isReorderState();
            }
        });

        viewContainer.add(new AjaxLink("copyLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onCopyLinkClicked(target);
            }
            @Override
            public boolean isVisible() {
                return displayCopyLink && !isReorderState();
            }

        });

        viewContainer.add(new AjaxLink<Void>("addLogicLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onAddLogicClicked(target, titleModel.getObject());
            }
        }.setBody(isRuleExists(titleModel.getObject()) ? new StringResourceModel("label.edit_logic", this, null):new StringResourceModel("label.add_logic", this, null))
                .setVisible(displayAddLogicLink));

        add(viewContainer);

        editFormContainer = new WebMarkupContainer("editFormContainer");
        editFormContainer.add(new EditForm("editForm", titleModel));
        add(editFormContainer.setVisible(false));

        add(createOptionalPanel("optionalPanel"));
    }

    protected String getTrimmedString(String str, int limit) {
		return str != null && str.length() > limit ? str.substring(0,limit)+"..." : str;
	}

    public int getTextDisplayLimit() {
        return DEFAULT_TEXT_DISPLAY_LIMIT;
    }

    class EditForm extends Form {

        private IModel<String> stringModel;

        public EditForm(String id, IModel<String> model) {
            super(id);
            this.stringModel = model;
            setOutputMarkupPlaceholderTag(true);
            add(newText = new RequiredTextField<>("newText", stringModel));

            AjaxSubmitLink storeLink;
            add(storeLink = new AjaxSubmitLink("storeLink") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    setViewState(target);
                    onStoreLinkClicked(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    onFormValidationError(target);
                }
            });
            storeLink.add(storeLabel = new FlatLabel("storeLabel", new FIDLabelModel("label.store")));

            newText.add(new ClickOnComponentWhenEnterKeyPressedBehavior(storeLink));
            add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    setViewState(target);
                }
            });
        }

    }

    protected IModel<String> getStringModel() {
        return (IModel<String>) getDefaultModelObject();
    }

    protected void onViewLinkClicked(AjaxRequestTarget target) { }

    protected void onDeleteButtonClicked(AjaxRequestTarget target) { }

    protected void onCopyLinkClicked(AjaxRequestTarget target) { }

    protected void onFormValidationError(AjaxRequestTarget target) { }

    protected void onStoreLinkClicked(AjaxRequestTarget target) { }

    protected Component createOptionalPanel(String id) {
        return new WebMarkupContainer(id).setVisible(false);
    }

    private void setViewState(AjaxRequestTarget target) {
        editFormContainer.setVisible(false);
        viewContainer.setVisible(true);
        target.add(this);
    }

    private void setEditState(AjaxRequestTarget target) {
        editFormContainer.setVisible(true);
        viewContainer.setVisible(false);
        target.add(this);
    }

    protected boolean isReorderState() {
        return false;
    }

    public boolean isDeletable() {
        return true;
    }

    public String getDeleteImage() {
        return DELETE_IMAGE;
    }

    public String getReorderImage() {
        return REORDER_IMAGE;
    }

    public void setStoreLabel(IModel<String> storeLabelModel) {
        storeLabel.setDefaultModel(storeLabelModel);
    }

    public void setEditMaximumLength(int maximumLength) {
        newText.add(new StringValidator.MaximumLengthValidator(maximumLength));
    }

    public TextField<String> getTextField() {
        return newText;
    }

    protected void onAddLogicClicked(AjaxRequestTarget target, String selectValue) {}

    protected boolean isRuleExists(String selectValue) {
        return false;
    }
}
