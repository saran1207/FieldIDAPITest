package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.components.image.ArrowStyleAnnotatedSvg;
import com.n4systems.fieldid.wicket.components.image.CallOutStyleAnnotatedSvg;
import com.n4systems.fieldid.wicket.components.image.ImageList;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.procedure.AnnotationType;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
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
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class IsolationPointListPanel extends Panel {

    //May no longer be needed here...
    private @SpringBean PersistenceService persistenceService;
    private @SpringBean S3Service s3Service;

    private final IModel<ProcedureDefinition> model;
    private final Component blankSlate;
    private ImageList images;

    private ListView<IsolationPoint> listView;
    private boolean isLockDirection;


    public IsolationPointListPanel(String id, final IModel<ProcedureDefinition> model, boolean isLockDirection) {
        super(id, model);
        this.model = model;
        this.isLockDirection = isLockDirection;
        setOutputMarkupPlaceholderTag(true);

        add(new AttributeAppender("class", "isolation-point-list"));

        add(images = getImageList(model));

        add(new AjaxLink<Void>("showLockOrder") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                toggleList(target, false);
                target.appendJavaScript("$('.show-unlock-order').removeClass('mattButtonPressed');$('.show-lock-order').addClass('mattButtonPressed');");
            }
        });

        add(new AjaxLink<Void>("showUnlockOrder") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                toggleList(target, true);
                target.appendJavaScript("$('.show-lock-order').removeClass('mattButtonPressed');$('.show-unlock-order').addClass('mattButtonPressed');");
            }
        });

        listView = new ListView<IsolationPoint>("list", getIsolationPointList()) {
            @Override protected void populateItem(ListItem<IsolationPoint> item) {
                populateIsolationPoint(item);
                item.setOutputMarkupId(true);
            }
        };

        final WebMarkupContainer sortableAjaxWicket = new WebMarkupContainer("sortableList");
        SortableAjaxBehavior sortableAjaxBehavior = new SimpleSortableAjaxBehavior() {

            @Override public void onReceive(Component sortedComponent, int index, Component parentSortedComponent, AjaxRequestTarget ajaxRequestTarget) {}

            @Override public void onUpdate(Component sortedComponent, int index, AjaxRequestTarget target) {
                reorderIsolationPoint(target, (IsolationPoint)sortedComponent.getDefaultModelObject(), index);
                listView.detach();
                target.add(sortableAjaxWicket);
            }

            @Override public void onRemove(Component sortedComponent, AjaxRequestTarget ajaxRequestTarget) { }
        };

        sortableAjaxBehavior.setOpacity(0.5F);
        sortableAjaxBehavior.setPlaceholder("sorting");

        sortableAjaxWicket.add(sortableAjaxBehavior);
        sortableAjaxWicket.setOutputMarkupId(true);
        sortableAjaxWicket.add(listView);
        add(sortableAjaxWicket);
        add(blankSlate = new WebMarkupContainer("blankSlate").setVisible(false));
    }

    /**
     * This method populates rows in the Isolation Point List on the LOTO Procedure Editor.
     *
     * @param item - A ListItem object containing an IsolationPoint model.
     */
    protected void populateIsolationPoint(ListItem<IsolationPoint> item) {
        final IsolationPoint isolationPoint = item.getModelObject();

        item.add(new AjaxLink("delete") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doDelete(target, isolationPoint);
            }
        });

        item.add(new Label("id", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getIdentifier())));

        //Depending on the "Source" for the Isolation Point, we're either going to display a NoteListItem
        //or an IsolationPointListItem so that the proper values are displayed.
        if(IsolationPointSourceType.N.equals(isolationPoint.getSourceType())) {
            item.add(new NoteListItem("listItem", item.getModel()));
        } else {
            item.add(new IsolationPointListItem("listItem", item.getModel()));
        }

        item.add(new AjaxLink("edit") {
            @Override public void onClick(AjaxRequestTarget target) {
                doEdit(target, isolationPoint);
            }
        });
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        boolean showBlankSlate = model.getObject().getLockIsolationPoints().size()==0;
        blankSlate.setVisible(showBlankSlate);
    }

    private ImageList getImageList(IModel<ProcedureDefinition> model) {
        if (model.getObject().getAnnotationType().equals(AnnotationType.CALL_OUT_STYLE)) {
            return new ImageList<ProcedureDefinitionImage>("images", ProxyModel.of(model, on(ProcedureDefinition.class).getImages())) {
                @Override
                protected void createImage(ListItem<ProcedureDefinitionImage> item) {
                    if (item.getModelObject().getAnnotations().size() > 0) {
                        item.add(new CallOutStyleAnnotatedSvg("image", item.getModel()).withScale(2.0));
                    }
                }
            };
        } else {
            return new ImageList<IsolationPoint>("images", ProxyModel.of(model, on(ProcedureDefinition.class).getLockIsolationPoints())) {
                @Override
                protected void createImage(ListItem<IsolationPoint> item) {
                    item.add(new ArrowStyleAnnotatedSvg("image", item.getModelObject().getAnnotation()));
                }
            };
        }
    }

    public void reloadImageList(AjaxRequestTarget target, IModel<ProcedureDefinition> model) {
        images.replaceWith(getImageList(model));
        target.add(images);
    }

    protected void doEdit(AjaxRequestTarget target, IsolationPoint isolationPoint) { }

    protected void doDelete(AjaxRequestTarget target, IsolationPoint isolationPoint) { }

    protected void reorderIsolationPoint(AjaxRequestTarget target, IsolationPoint defaultModelObject, int index) { }

    protected void toggleList(AjaxRequestTarget target, boolean isLockDirection) {}

    public LoadableDetachableModel<List<IsolationPoint>> getIsolationPointList() {
        return new LoadableDetachableModel<List<IsolationPoint>>() {
            @Override
            protected List<IsolationPoint> load() {
                if(isLockDirection)
                    return model.getObject().getLockIsolationPoints();
                else
                    return model.getObject().getUnlockIsolationPoints();
            }
        };
    }
}
