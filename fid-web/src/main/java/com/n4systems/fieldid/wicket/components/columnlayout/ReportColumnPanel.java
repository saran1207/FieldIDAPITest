package com.n4systems.fieldid.wicket.components.columnlayout;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.columnlayout.CssForTypeModel;
import com.n4systems.fieldid.wicket.model.columnlayout.PointerOnHoverAppender;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.SystemColumnMapping;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.resource.ContextRelativeResource;

public class ReportColumnPanel extends Panel {

    private WebMarkupContainer areaClickableForAdd;
    private IModel<ColumnMapping> columnModel;
    private boolean addMode;
    private boolean deleteAlwaysVisible;

    public ReportColumnPanel(String id, IModel<ColumnMapping> model, boolean addMode) {
        super(id, model);
        this.columnModel = model;
        this.addMode = addMode;

        areaClickableForAdd = new WebMarkupContainer("areaClickableForAdd");
        add(areaClickableForAdd);

        IModel<String> labelModel;
        // If it's a system mapping, the label is a property key, otherwise it's the actual string
        if (model.getObject() instanceof SystemColumnMapping) {
            labelModel = new FIDLabelModel(model, "label");
        } else {
            labelModel = new PropertyModel<String>(model, "label");
        }
        areaClickableForAdd.add(new Label("columnTitle", labelModel));
        add(new AttributeAppender("class", true, new CssForTypeModel("column_body_", model), " "));

        WebMarkupContainer leftBox = new WebMarkupContainer("leftBox");
        leftBox.add(new AttributeAppender("class", true, new CssForTypeModel("column_box_", model), " "));
        addPlusAndDragImages(leftBox);
        areaClickableForAdd.add(leftBox);

        addRemoveImage();

        areaClickableForAdd.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                onAddLinkClicked(target, columnModel);
            }
        });
        areaClickableForAdd.add(new PointerOnHoverAppender());
    }

    private void addPlusAndDragImages(WebMarkupContainer leftBox) {
        Image dragImage = new Image("dragImage", new ContextRelativeResource("/images/columnlayout/drag.png"));
        dragImage.setVisible(!addMode);
        leftBox.add(dragImage);

        Image plusImage = new Image("plusImage", new ContextRelativeResource("/images/columnlayout/plus.png"));
        plusImage.setVisible(addMode);
        leftBox.add(plusImage);

        plusImage.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                onAddLinkClicked(target, columnModel);
            }
        });
    }

    private void addRemoveImage() {
        Image removeImage;
        add(removeImage = new Image("removeImage", new ContextRelativeResource("/images/columnlayout/remove.png")) {
            @Override
            public boolean isVisible() {
                return deleteAlwaysVisible || !addMode;
            }
        });

        removeImage.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                onRemoveLinkClicked(target);
            }
        });
    }

    protected void onAddLinkClicked(AjaxRequestTarget target, IModel<ColumnMapping> reportColumnModel) {
    }

    protected void onRemoveLinkClicked(AjaxRequestTarget target) {
    }

    public IModel<ColumnMapping> getColumnModel() {
        return columnModel;
    }

    public void setDeleteAlwaysVisible(boolean deleteAlwaysVisible) {
        this.deleteAlwaysVisible = deleteAlwaysVisible;
    }

}
