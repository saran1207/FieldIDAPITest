package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.model.procedure.LockoutReason;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class LockoutReasonsListPage extends LockoutReasonsPage {

    public LockoutReasonsListPage() {
        super();
        add(listContainer = new WebMarkupContainer("listContainer"));
        listContainer.setOutputMarkupId(true);

        listContainer.add(new ListView<LockoutReason>("lockoutReason", getLockoutReasonsListModel()) {
            @Override
            protected void populateItem(ListItem<LockoutReason> item) {
                item.add(new Label("name", new PropertyModel<>(item.getModel(), "name")));
                item.add(new Label("createdBy", new PropertyModel<>(item.getModel(), "createdBy.fullName")));
                item.add(new DateTimeLabel("created", new PropertyModel<>(item.getModel(), "created")));
                item.add(new Label("modifiedBy", new PropertyModel<>(item.getModel(), "modifiedBy.fullName")));
                item.add(new DateTimeLabel("lastModified", new PropertyModel<>(item.getModel(), "modified")));

                item.add(new AjaxLink<Void>("edit") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        addOrEditModalWindow.setContent(getAddEditPanel(addOrEditModalWindow.getContentId(), item.getModel()));
                        addOrEditModalWindow.show(target);
                    }
                });

                item.add(new AjaxLink<Void>("archive") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        lockoutReasonsService.archive(item.getModelObject());
                        setResponsePage(LockoutReasonsListPage.class);
                    }
                });
            }
        });
    }

    private LoadableDetachableModel<List<LockoutReason>> getLockoutReasonsListModel() {
        return new LoadableDetachableModel<List<LockoutReason>>() {
            @Override
            protected List<LockoutReason> load() {
                return lockoutReasonsService.getActiveLockoutReasons();
            }
        };
    }
}
