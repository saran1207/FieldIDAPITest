package com.n4systems.fieldid.wicket.components.actions;

import com.n4systems.fieldid.service.event.PriorityCodeService;
import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.PriorityCode;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class PriorityCodeArchivedListPanel extends Panel {

    @SpringBean
    private PriorityCodeService priorityCodeService;

    public PriorityCodeArchivedListPanel(String id) {
        super(id);


        ListView<PriorityCode> priorityCodes = new ListView<PriorityCode>("priorityCode", createActionTypesModel()) {
            @Override
            protected void populateItem(ListItem<PriorityCode> item) {

                final PriorityCode priorityCode = item.getModelObject();

                item.add(new Label("name", new PropertyModel<PriorityCode>(priorityCode, "name")));

                Label createdBy;
                Label modifiedBy;

                item.add(createdBy = new Label("createdBy", new PropertyModel<Object>(priorityCode, "createdBy.userID")));
                createdBy.setVisible(priorityCode.getCreatedBy() != null);
                item.add(new DateTimeLabel("created", new PropertyModel<Date>(priorityCode, "created")));

                item.add(modifiedBy = new Label("modifiedBy", new PropertyModel<Object>(priorityCode, "modifiedBy.userID")));
                modifiedBy.setVisible(priorityCode.getModifiedBy() != null);
                item.add(new DateTimeLabel("lastModified", new PropertyModel<Date>(priorityCode, "modified")));

                AjaxLink unarchive;
                item.add(unarchive = new AjaxLink("unarchive") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        priorityCodeService.unarchive(priorityCode);
                        target.add(PriorityCodeArchivedListPanel.this);
                    }
                });
                unarchive.add(new AttributeAppender("title", new FIDLabelModel("label.unarchive")));
            }
        };
        add(priorityCodes);

    }

   private LoadableDetachableModel<List<PriorityCode>> createActionTypesModel() {
       return new LoadableDetachableModel<List<PriorityCode>>() {
           @Override
           protected List<PriorityCode> load() {
               return priorityCodeService.getArchivedPriorityCodes();
           }
       };
   }
}
