package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.components.image.EditableImageList;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import static ch.lambdaj.Lambda.on;

public class IsolationPointListPanel extends Panel {

    private @SpringBean PersistenceService persistenceService;
    private final IModel<ProcedureDefinition> model;

    public IsolationPointListPanel(String id, IModel<ProcedureDefinition> model) {
        super(id, model);
        this.model = model;
        setOutputMarkupPlaceholderTag(true);

        add(new AttributeAppender("class", "isolation-point-list"));

        add(new EditableImageList("images", new PropertyModel(model,"images")));

        final ListView<IsolationPoint> listView = new ListView<IsolationPoint>("list",new PropertyModel(model,"isolationPoints")) {
            @Override protected void populateItem(ListItem<IsolationPoint> item) {
                populateIsolationPoint(item);
                item.setOutputMarkupId(true);
            }
        };

        WebMarkupContainer sortableAjaxWicket = new WebMarkupContainer("sortableList");
        SortableAjaxBehavior sortableAjaxBehavior = new SimpleSortableAjaxBehavior() {

            @Override public void onReceive(Component sortedComponent, int index, Component parentSortedComponent, AjaxRequestTarget ajaxRequestTarget) {}

            @Override public void onUpdate(Component sortedComponent, int index, AjaxRequestTarget target) {
                reorderIsolationPoint(target, (IsolationPoint)sortedComponent.getDefaultModelObject(), index);
                target.add(listView);
            }

            @Override public void onRemove(Component sortedComponent, AjaxRequestTarget ajaxRequestTarget) { }
        };
        sortableAjaxBehavior.getSortableBehavior().setConnectWith(".connectedSortable");
        sortableAjaxWicket.add(sortableAjaxBehavior);
        sortableAjaxWicket.add(listView);
        add(sortableAjaxWicket);
    }

    protected void populateIsolationPoint(ListItem<IsolationPoint> item) {
        final IsolationPoint isolationPoint = item.getModelObject();

        item.add(new AjaxLink("delete") {
            @Override public void onClick(AjaxRequestTarget target) {
                doDelete(target,isolationPoint);
            }
        });
        item.add(new Label("id", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getIdentifier())));
        item.add(new Label("source", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getSourceType())));
        item.add(new Label("device", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getDeviceDefinition().getAssetType().getName())));
        item.add(new Label("location", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getLocation())));
        item.add(new Label("method", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getMethod())));
        item.add(new Label("check", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getCheck())));
        // TODO DD : add images to row.
        item.add(new WebMarkupContainer("image"));

        item.add(new AjaxLink("edit") {
            @Override public void onClick(AjaxRequestTarget target) {
                doEdit(target, isolationPoint);
            }
        });
    }

    protected void doEdit(AjaxRequestTarget target, IsolationPoint isolationPoint) { }

    protected void doDelete(AjaxRequestTarget target, IsolationPoint isolationPoint) { }

    protected void reorderIsolationPoint(AjaxRequestTarget target, IsolationPoint defaultModelObject, int index) { }

}
