package com.n4systems.fieldid.wicket.pages.masterevent;

import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.model.SubEvent;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;


public class SubEventListPanel extends Panel {

    private IModel<ThingEvent> masterEventModel;

    public SubEventListPanel(String id, IModel<ThingEvent> masterEventModel) {
        super(id, masterEventModel);

        this.masterEventModel = masterEventModel;

        WebMarkupContainer listContainer = new WebMarkupContainer("listContainer");
        listContainer.setOutputMarkupId(true);
        add(listContainer);

        listContainer.add(new ListView<SubEvent>("subEvent", getSubEventListModel()) {
            @Override
            protected void populateItem(ListItem<SubEvent> item) {
                SubEvent subEvent = item.getModelObject();
                item.add(new FlatLabel("assetType", new PropertyModel<String>(subEvent, "asset.type.displayName")));
                item.add(new FlatLabel("identifier", new PropertyModel<String>(subEvent, "asset.identifier")));
                item.add(new Link<Void>("editSubEventLink") {
                    @Override
                    public void onClick() {
                        onEditSubEvent(masterEventModel);
                        setResponsePage(new EditSubEventPage(masterEventModel, item.getModel()));
                    }
                }.add(new FlatLabel("eventType", new PropertyModel<String>(subEvent, "type.displayName"))));
                item.add(new AjaxLink<Void>("deleteSubEventLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        masterEventModel.getObject().getSubEvents().remove(subEvent);
                        target.add(listContainer);
                        onDeleteSubEvent(target);
                    }
                });
            }
        });
    }

    protected void onEditSubEvent(IModel<ThingEvent> masterEventModel) {}

    protected void onDeleteSubEvent(AjaxRequestTarget target) {}

    public LoadableDetachableModel<List<SubEvent>> getSubEventListModel() {
        return new LoadableDetachableModel<List<SubEvent>>() {
            @Override
            protected List<SubEvent> load() {
                return masterEventModel.getObject().getSubEvents();
            }
        };
    }
}
