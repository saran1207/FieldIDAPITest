package com.n4systems.fieldid.wicket.components.actions;

import com.n4systems.fieldid.service.event.PriorityCodeService;
import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.setup.prioritycode.PriorityCodeUniqueNameValidator;
import com.n4systems.model.PriorityCode;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class PriorityCodeListPanel extends Panel {

    @SpringBean
    private PriorityCodeService priorityCodeService;

    public PriorityCodeListPanel(String id) {
        super(id);


        ListView<PriorityCode> priorityCodes = new ListView<PriorityCode>("priorityCode", createActionTypesModel()) {
            @Override
            protected void populateItem(ListItem<PriorityCode> item) {

                final PriorityCode priorityCode = item.getModelObject();
                final Label name;
                item.add(name = new Label("name", new PropertyModel<PriorityCode>(priorityCode, "name")));
                name.setOutputMarkupPlaceholderTag(true);

                final FIDFeedbackPanel feedbackPanel;

                Label createdBy;
                Label modifiedBy;

                item.add(createdBy = new Label("createdBy", new PropertyModel<Object>(priorityCode, "createdBy.userID")));
                createdBy.setVisible(priorityCode.getCreatedBy() != null);
                item.add(new DateTimeLabel("created", new PropertyModel<Date>(priorityCode, "created")));

                item.add(modifiedBy = new Label("modifiedBy", new PropertyModel<Object>(priorityCode, "modifiedBy.userID")));
                modifiedBy.setVisible(priorityCode.getModifiedBy() != null);
                item.add(new DateTimeLabel("lastModified", new PropertyModel<Date>(priorityCode, "modified")));

                final Form<Void> editForm = new Form("editForm");
                final AjaxLink edit;
                final AjaxLink archive;

                item.add(archive = new AjaxLink("archive") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        priorityCodeService.archive(priorityCode);
                        target.add(PriorityCodeListPanel.this);
                    }
                });
                archive.add(new AttributeAppender("title", new FIDLabelModel("label.archive")));

                item.add(edit = new AjaxLink("edit") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        editForm.setVisible(true);
                        name.setVisible(false);
                        setVisible(false);
                        archive.setVisible(false);
                        target.add(name);
                        target.add(editForm);
                        target.add(this);
                        target.add(archive);
                        target.appendJavaScript("$('.tipsy').remove();");
                    }
                });
                edit.add(new AttributeAppender("title", new FIDLabelModel("label.edit")));

                editForm.add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

                RequiredTextField nameField;
                editForm.add(nameField = new RequiredTextField("nameField", new PropertyModel(priorityCode, "name")));
                nameField.add(new PriorityCodeUniqueNameValidator(priorityCode.getId()));

                editForm.add(new AjaxSubmitLink("saveButton") {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        priorityCodeService.update(priorityCode);
                        editForm.setVisible(false);
                        name.setVisible(true);
                        edit.setVisible(true);
                        archive.setVisible(true);
                        feedbackPanel.setVisible(false);
                        target.add(PriorityCodeListPanel.this);
                        target.appendJavaScript("$('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");
                    }

                    @Override
                    protected void onError(AjaxRequestTarget target, Form<?> form) {
                        feedbackPanel.setVisible(true);
                        target.add(feedbackPanel);
                    }
                });

                editForm.setVisible(false);
                editForm.setOutputMarkupPlaceholderTag(true);
                item.add(editForm);

            }

        };
        add(priorityCodes);

    }

   private LoadableDetachableModel<List<PriorityCode>> createActionTypesModel() {
       return new LoadableDetachableModel<List<PriorityCode>>() {
           @Override
           protected List<PriorityCode> load() {
               return priorityCodeService.getActivePriorityCodes();
           }
       };
   }
}
