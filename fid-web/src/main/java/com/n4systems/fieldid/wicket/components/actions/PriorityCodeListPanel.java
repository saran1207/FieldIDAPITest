package com.n4systems.fieldid.wicket.components.actions;

import com.n4systems.fieldid.service.event.PriorityCodeService;
import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.PriorityCode;
import com.n4systems.model.PriorityCodeAutoScheduleType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
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

        ListView<PriorityCode> priorityCodes = new ListView<PriorityCode>("priorityCode", createPriorityCodesModel()) {
            @Override
            protected void populateItem(final ListItem<PriorityCode> item) {

                final PriorityCode priorityCode = item.getModelObject();
                final Label name;
                item.add(name = new Label("name", new PropertyModel<PriorityCode>(priorityCode, "name")));
                name.setOutputMarkupPlaceholderTag(true);

                Label createdBy;
                Label modifiedBy;

                item.add(createdBy = new Label("createdBy", new PropertyModel<Object>(priorityCode, "createdBy.userID")));
                createdBy.setVisible(priorityCode.getCreatedBy() != null);
                item.add(new DateTimeLabel("created", new PropertyModel<Date>(priorityCode, "created")));

                item.add(modifiedBy = new Label("modifiedBy", new PropertyModel<Object>(priorityCode, "modifiedBy.userID")));
                modifiedBy.setVisible(priorityCode.getModifiedBy() != null);
                item.add(new DateTimeLabel("lastModified", new PropertyModel<Date>(priorityCode, "modified")));

                item.add(new Label("autoSchedule", createAutoScheduleDescriptionModel(item.getModel())));

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
                        onEditPriorityCodeClicked(target, item.getModelObject());
//                        target.appendJavaScript("$('.tipsy').remove();");
                    }
                });
                edit.add(new AttributeAppender("title", new FIDLabelModel("label.edit")));
            }

        };
        add(priorityCodes);
    }

    private LoadableDetachableModel<List<PriorityCode>> createPriorityCodesModel() {
        return new LoadableDetachableModel<List<PriorityCode>>() {
           @Override
           protected List<PriorityCode> load() {
               return priorityCodeService.getActivePriorityCodes();
           }
       };
    }

    private LoadableDetachableModel<String> createAutoScheduleDescriptionModel(final IModel<PriorityCode> priorityCode) {
        return new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                if (PriorityCodeAutoScheduleType.CUSTOM.equals(priorityCode.getObject().getAutoSchedule())) {
                    return new FIDLabelModel("label.x_days_later", priorityCode.getObject().getAutoScheduleCustomDays()).getObject();
                } else {
                    Object displayValue = new ListableLabelChoiceRenderer<PriorityCodeAutoScheduleType>().getDisplayValue(priorityCode.getObject().getAutoSchedule());
                    return displayValue == null ? null : displayValue.toString();
                }
            }
        };
    }

    protected void onEditPriorityCodeClicked(AjaxRequestTarget target, PriorityCode priorityCode) { }

}
