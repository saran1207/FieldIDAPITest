package com.n4systems.fieldid.wicket.pages.masterevent;

import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

public class MasterEventSubHeader extends Panel {

    private DialogModalWindow subEventListModal;

    private Label subEventCount;

    public MasterEventSubHeader(String id, IModel<ThingEvent> model) {
        super(id, model);

        add(subEventListModal = new DialogModalWindow("subEventListModal"));

        add(new FlatLabel("assetType", new PropertyModel<String>(model, "asset.type.displayName")));

        add(new AjaxLink<Void>("linkedEventsLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                subEventListModal.setContent(new SubEventListPanel(subEventListModal.getContentId(), model) {
                    @Override
                    protected void onEditSubEvent(IModel<ThingEvent> masterEventModel) {
                        MasterEventSubHeader.this.onEditSubEvent(masterEventModel);
                    }

                    @Override
                    protected void onDeleteSubEvent(AjaxRequestTarget target) {
                        target.add(subEventCount);
                    }
                });
                subEventListModal.show(target);
            }
        }.add(subEventCount = new Label("linkLabel", getLinkEventsCount(model))));
        subEventCount.setOutputMarkupId(true);
    }

    private LoadableDetachableModel<String> getLinkEventsCount(IModel<ThingEvent> model) {
        return new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return new FIDLabelModel("label.linked_events_#", model.getObject().getSubEvents().size()).getObject();
            }
        };
    }



    protected void onEditSubEvent(IModel<ThingEvent> masterEventModel) {}
}
