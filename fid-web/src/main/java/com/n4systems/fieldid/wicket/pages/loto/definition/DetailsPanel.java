package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.components.user.AutoCompleteUser;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class DetailsPanel extends Panel {

    // TODO : replace with real model when that's done.
    private String procedureCode;
    private String identifier;
    private String revision;
    private String warnings;
    private User user;
    private String equipmentNumber;
    private String equipmentLocation;
    private String equipmentDescription;


    public DetailsPanel(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        add(new AttributeAppender("class", Model.of("details")));

        add(new Label("procedureCodeLabel", new FIDLabelModel("label.procedure_code")));
        add(new TextField("procedureCode", new PropertyModel(this, "procedureCode")));

        add(new Label("identifierLabel", new FIDLabelModel("label.electronic_identifier")));
        add(new TextField("identifier", new PropertyModel(this, "identifier")));

        add(new Label("revisionLabel", new FIDLabelModel("label.revision_number")));
        add(new TextField("revision", new PropertyModel(this, "revision")));

        add(new Label("warningsLabel",new FIDLabelModel("label.warnings")));
        add(new TextArea("warnings",new PropertyModel(this,"warnings")));

        add(new Label("userLabel",new FIDLabelModel("label.developed_by")));
        add(new AutoCompleteUser("user",new PropertyModel(this,"user")));

        add(new Label("equipmentNumberLabel",new FIDLabelModel("label.equipment_number")));
        add(new TextField("equipmentNumber",new PropertyModel(this,"equipmentNumber")));

        add(new Label("equipmentLocationLabel",new FIDLabelModel("label.equipment_number")));
        add(new TextField("equipmentLocation",new PropertyModel(this,"equipmentLocation")));

        add(new Label("equipmentDescriptionLabel",new FIDLabelModel("label.equipment_description")));
        add(new TextField("equipmentDescription",new PropertyModel(this,"equipmentDescription")));

        add(new AjaxLink("cancel") {
            @Override public void onClick(AjaxRequestTarget target) {
                doCancel(target);
            }
        });
        add(new AjaxLink("continue") {
            @Override public void onClick(AjaxRequestTarget target) {
                doContinue(target);
            }
        });
    }

    protected void doCancel(AjaxRequestTarget target) { }

    protected void doContinue(AjaxRequestTarget target) { }

    public DetailsPanel(String id, IModel<?> model) {
        super(id, model);
    }
}
