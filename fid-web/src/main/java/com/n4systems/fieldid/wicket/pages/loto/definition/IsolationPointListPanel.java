package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.image.EditableImageList;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.procedure.IsolationPoint;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class IsolationPointListPanel extends Panel {

    private @SpringBean PersistenceService persistenceService;

    public IsolationPointListPanel(String id, IModel<List<IsolationPoint>> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);

        add(new AttributeAppender("class", "isolation-point-list"));

        add(new EditableImageList("images", new ImageModel() ));

        add(new ListView<IsolationPoint>("list") {
            @Override protected void populateItem(ListItem<IsolationPoint> item) {
                addIsolationPoint(item);
            }
        });

        add(new AjaxLink("addButton") {
            @Override public void onClick(AjaxRequestTarget target) {
                doAdd(target);
            }
        });

        add(new AjaxLink("cancel") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doCancel(target);
            }
        });
        add(new AjaxLink("continue") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doContinue(target);
            }

        });

    }

    private void doContinue(AjaxRequestTarget target) { }

    private void doCancel(AjaxRequestTarget target) { }

    protected void doAdd(AjaxRequestTarget target) { }

    protected void addIsolationPoint(ListItem<IsolationPoint> item) {
        IsolationPoint isolationPoint = item.getModelObject();
        item.add(new Label("id", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getIdentifier())));
        item.add(new Label("source", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getSource())));
        item.add(new Label("device", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getDeviceDefinition().getAssetType().getName())));
        item.add(new Label("location", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getLocation())));
        item.add(new Label("method", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getMethod())));
        item.add(new Label("check", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getCheck())));
        // UI : suggestion, don't have edit next to delete.
        item.add(new AjaxLink("edit") {
            @Override public void onClick(AjaxRequestTarget target) {
                // TODO : edit isolation point.
            }
        });
        item.add(new AjaxLink("delete") {
            @Override public void onClick(AjaxRequestTarget target) {
                // TODO : delete isolation point.
            }
        });
    }

    class ImageModel extends LoadableDetachableModel<List<EditableImage>> {
        @Override
        protected List<EditableImage> load() {
//            return Lists.newArrayList();
            List<EditableImage> all = persistenceService.findAll(EditableImage.class);
            return all;
        }
    }




}
