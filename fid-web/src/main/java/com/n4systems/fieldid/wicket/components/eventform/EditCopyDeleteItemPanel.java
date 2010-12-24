package com.n4systems.fieldid.wicket.components.eventform;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class EditCopyDeleteItemPanel extends Panel {

    private static final String DELETE_IMAGE = "images/small-x.png";
    private static final String REORDER_IMAGE = "images/reorder.png";

    private EditForm editForm;
    private WebMarkupContainer viewContainer;

    public EditCopyDeleteItemPanel(String id, IModel<String> stringModel) {
        this(id, stringModel, true);
    }

    public EditCopyDeleteItemPanel(String id, IModel<String> stringModel, boolean displayCopyLink) {
        this(id, stringModel, null, displayCopyLink);
    }

    public EditCopyDeleteItemPanel(String id, IModel<String> titleModel, IModel<String> subTitleModel) {
        this(id, titleModel, subTitleModel, true);
    }

    public EditCopyDeleteItemPanel(String id, IModel<String> titleModel, IModel<String> subTitleModel, boolean displayCopyLink) {
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
        viewLink.add(new Label("linkLabel", titleModel));
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
                return !isReorderState();
            }

        }.setVisible(displayCopyLink));

        add(viewContainer);

        editForm = new EditForm("editForm", titleModel);
        add(editForm.setVisible(false));
    }

    class EditForm extends Form {

        private IModel<String> stringModel;

        public EditForm(String id, IModel<String> model) {
            super(id);
            this.stringModel = model;
            add(new TextField<String>("newText", stringModel));
            add(new AjaxSubmitLink("storeLink") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    setViewState(target);
                }
            });
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

    private void setViewState(AjaxRequestTarget target) {
        editForm.setVisible(false);
        viewContainer.setVisible(true);
        target.addComponent(this);
    }

    private void setEditState(AjaxRequestTarget target) {
        editForm.setVisible(true);
        viewContainer.setVisible(false);
        target.addComponent(this);
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

}
