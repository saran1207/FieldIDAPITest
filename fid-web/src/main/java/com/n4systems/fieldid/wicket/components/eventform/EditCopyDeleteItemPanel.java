package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.TrimmedStringModel;
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
import org.apache.wicket.validation.validator.StringValidator;


public class EditCopyDeleteItemPanel extends Panel {

    private static final String DELETE_IMAGE = "images/small-x.png";
    private static final String REORDER_IMAGE = "images/reorder.png";

    private WebMarkupContainer editFormContainer;
    private WebMarkupContainer viewContainer;
    private FlatLabel storeLabel;
    private TextField<String> newText;

    public EditCopyDeleteItemPanel(String id, IModel<String> stringModel) {
        this(id, stringModel, true);
    }

    public EditCopyDeleteItemPanel(String id, IModel<String> stringModel, boolean displayCopyLink) {
        this(id, stringModel, null, displayCopyLink);
    }

    public EditCopyDeleteItemPanel(String id, IModel<String> titleModel, IModel<String> subTitleModel) {
        this(id, titleModel, subTitleModel, true);
    }

    public EditCopyDeleteItemPanel(String id, IModel<String> titleModel, IModel<String> subTitleModel, final boolean displayCopyLink) {
        super(id, titleModel);        
        setOutputMarkupPlaceholderTag(true);

        ContextImage deleteImage = new ContextImage("deleteImage", new PropertyModel<String>(this, "deleteImage")) {
            @Override
            public boolean isVisible() {
                return !isReorderState();
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

        ContextImage reorderImage = new ContextImage("reorderImage", new PropertyModel<String>(this, "reorderImage")) {
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
        viewLink.add(new Label("linkLabel", new TrimmedStringModel(titleModel, 23)));
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

        add(viewContainer);

        editFormContainer = new WebMarkupContainer("editFormContainer");
        editFormContainer.add(new EditForm("editForm", titleModel));
        add(editFormContainer.setVisible(false));
    }

    protected String getTrimmedString(String str, int limit) {
		return str != null && str.length() > limit ? str.substring(0,limit)+"..." : str;
	}	
    
	class EditForm extends Form {

        private IModel<String> stringModel;

        public EditForm(String id, IModel<String> model) {
            super(id);
            this.stringModel = model;
            setOutputMarkupPlaceholderTag(true);
            add(newText = new RequiredTextField<String>("newText", stringModel));

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

}
