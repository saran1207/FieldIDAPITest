package com.n4systems.fieldid.wicket.pages.setup.eventbook;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.event.EventBookService;
import com.n4systems.fieldid.service.eventbook.EventBookListFilterCriteria;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.components.setup.eventbook.EventBooksActionColumn;
import com.n4systems.fieldid.wicket.components.setup.eventbook.EventBooksEditCell;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.EventBookDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.EventBook;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * This is the shared back-end for <b>EventBooksListAllPage</b> and <b>EventBooksListArchivedPage</b>.
 *
 * This handles building all shared page components and sets up any shared logic.
 *
 * Created by Jordan Heath on 06/08/14.
 */
public abstract class EventBooksListPage extends FieldIDTemplatePage {


    public static final int BOOKS_PER_PAGE = 20;

    protected FIDFeedbackPanel feedbackPanel;
    private WebMarkupContainer listContainer;
    private OrgLocationPicker picker;

    private IModel<EventBookListFilterCriteria> filterCriteriaModel;
    private Form filterForm;

    protected IModel<Archivable.EntityState> archivableState;

    @SpringBean
    protected EventBookService eventBookService;

    public EventBooksListPage(IModel<Archivable.EntityState> model) {
        archivableState = model;
        filterCriteriaModel = Model.of(new EventBookListFilterCriteria());
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        add(filterForm = new Form<EventBookListFilterCriteria>("filterForm", filterCriteriaModel));
        filterForm.add(new TextField<String>("titleFilter", new PropertyModel<String>(filterCriteriaModel, "titleFilter")));

        picker = new OrgLocationPicker("orgPickerField", new PropertyModel<BaseOrg>(filterCriteriaModel, "owner"));
        filterForm.add(picker);

        filterForm.add(new AjaxButton("filter") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(listContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {}
        });

        filterForm.add(new AjaxSubmitLink("clear") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                filterCriteriaModel.getObject().reset();
                picker.resetInput();
                target.add(filterForm, listContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {}
        });


        add(listContainer = new WebMarkupContainer("eventBooksListPanel"));

        listContainer.setOutputMarkupId(true);

        listContainer.add(new SimpleDefaultDataTable<AssetStatus>("eventBooksList",
                                                                  getEventBookTableColumns(),
                                                                  getDataProvider(),
                                                                  BOOKS_PER_PAGE));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_event_books"));
    }


    @Override
    protected Component createBackToLink(String linkId,
                                         String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId,
                                                                             AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId,
                                   new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }


    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                        aNavItem().label(new FIDLabelModel("nav.view_all.count",
                                         eventBookService.getActiveEventBookCount()))
                                  .page(EventBooksListAllPage.class)
                                  .build(),

                        aNavItem().label(new FIDLabelModel("nav.view_all_archived.count",
                                         eventBookService.getArchivedEventBookCount()))
                                  .page(EventBooksListArchivedPage.class)
                                  .build(),

                        aNavItem().label(new FIDLabelModel("nav.add"))
                                  .page(AddEventBookPage.class)
                                  .onRight()
                                  .build()
                )
        );
    }

    /**
     * This method sets the Data Provider with the required Archivable.EntityState.
     *
     * @return An <b>EventBookDataProvider</b> tied to the appropriate state..
     */
    protected EventBookDataProvider getDataProvider() {
        return new EventBookDataProvider("name",
                                         SortOrder.ASCENDING,
                                         archivableState.getObject(), filterCriteriaModel.getObject());
    }

    private List<IColumn<EventBook>> getEventBookTableColumns() {
        List<IColumn<EventBook>> columns = Lists.newArrayList();

        columns.add(new PropertyColumn<EventBook>(new FIDLabelModel("label.title"),
                                                  "name",
                                                  "name")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<EventBook>> item,
                                     final String componentId,
                                     final IModel<EventBook> rowModel)
            {
                item.add(new EventBooksEditCell(componentId,
                                                rowModel));
            }
        });

        columns.add(new PropertyColumn<EventBook>(new FIDLabelModel("label.jobsite"),
                                                  "owner.name",
                                                  "owner.name"));

        columns.add(new PropertyColumn<EventBook>(new FIDLabelModel("label.createdby"),
                "createdBy.firstName, createdBy.lastName",
                "createdBy.fullName"));

        columns.add(new PropertyColumn<EventBook>(new FIDLabelModel("label.created_on"),
                                                  "created",
                                                  "created"));

        columns.add(new PropertyColumn<EventBook>(new FIDLabelModel("label.modifiedby"),
                "modifiedBy.firstName, modifiedBy.lastName",
                "modifiedBy.fullName"));

        columns.add(new PropertyColumn<EventBook>(new FIDLabelModel("label.modified_on"),
                "modified",
                "modified"));

        columns.add(new PropertyColumn<EventBook>(new FIDLabelModel("label.status"),
                                                  "open",
                                                  "open")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<EventBook>> item,
                                     final String componentId,
                                     final IModel<EventBook> rowModel) {
                item.add(new Label(componentId,
                                   (rowModel.getObject().isOpen() ? "Open" : "Closed")));
            }
        });

        addActionColumn(columns);

        return columns;
    }

    protected void addActionColumn(List<IColumn<EventBook>> columns) {
        columns.add(new EventBooksActionColumn() {
            @Override
            protected void onAction(AjaxRequestTarget target) {
                target.add(listContainer,
                           getTopFeedbackPanel());
            }
        });
    }
}
