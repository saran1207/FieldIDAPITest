package com.n4systems.fieldid.wicket.pages.admin.printouts;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.model.LotoPrintout;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * This is the LOTO Printout Template listing page.
 *
 * Created by Jordan Heath on 2014-10-28.
 */
public class LotoPrintoutListPage extends FieldIDAdminPage {

    private static final int PRINTOUTS_PER_PAGE = 200;

    private FIDFeedbackPanel feedbackPanel;
    private WebMarkupContainer listContainer;

    private ModalWindow addTemplateWindow;

    public LotoPrintoutListPage() {
        //Add all of the page components here...


    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        add(listContainer = new WebMarkupContainer("lotoPrintoutListPanel"));

        listContainer.setOutputMarkupId(true);

        listContainer.add(new SimpleDefaultDataTable<LotoPrintout>("lotoPrintoutList",
                                                                    getLotoPrintoutColumns(),
                                                                    getDataProvider(),
                                                                    PRINTOUTS_PER_PAGE));


        add(addTemplateWindow = new DialogModalWindow("addTemplateModalWindow").setInitialHeight(400).setInitialWidth(400));

        addTemplateWindow.setContent(new AddLotoTemplatePanel(addTemplateWindow.getContentId()));

//        add(addTemplateWindow = createModalWindow(Model.of(new LotoPrintout()), feedbackPanel));


        add(new AjaxLink<Void>("addTemplateLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                addTemplateWindow.show(target);
            }
        });
    }

    private List<IColumn<LotoPrintout>> getLotoPrintoutColumns() {
        List<IColumn<LotoPrintout>> columns = Lists.newArrayList();

        columns.add(new PropertyColumn<>(new FIDLabelModel("label.name"), "printoutName", "printoutName"));

        columns.add(new PropertyColumn<>(new FIDLabelModel("label.type"), "printoutType", "printoutType"));

        columns.add(new PropertyColumn<>(new FIDLabelModel("label.directory"), "s3Path", "s3Path"));

        columns.add(new PropertyColumn<LotoPrintout>(new FIDLabelModel("label.dateUploaded"), "created", "created") {
            @Override
            public void populateItem(Item<ICellPopulator<LotoPrintout>> item, String componentId, IModel<LotoPrintout> rowModel) {
                Date created = rowModel.getObject().getCreated();
                item.add(new Label(componentId, new DayDisplayModel(Model.of(created)).includeTime().withTimeZone(FieldIDSession.get().getSessionUser().getTimeZone())));
            }
        });

        columns.add(new PropertyColumn<LotoPrintout>(new FIDLabelModel("label.download"), "s3Path") {
            @Override
            public void populateItem(Item<ICellPopulator<LotoPrintout>> item, String componentId, IModel<LotoPrintout> rowModel) {
                item.add(new Link(componentId, rowModel) {
                    @Override
                    public void onClick() {
                        //Do your clickiness to download the thing here...
                    }
                });
            }
        });

        columns.add(new PropertyColumn<LotoPrintout>(new FIDLabelModel(""), "") {
            @Override
            public void populateItem(Item<ICellPopulator<LotoPrintout>> item, String componentId, IModel<LotoPrintout> rowModel) {
                item.add(new Link(componentId, rowModel) {
                    @Override
                    public void onClick() {
                        //get 'er done for deleting here...
                    }
                });
            }
        });

        return columns;
    }

    private FieldIDDataProvider<LotoPrintout> getDataProvider() {
        return new FieldIDDataProvider<LotoPrintout>() {
            @Override
            public Iterator<? extends LotoPrintout> iterator(int first, int count) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public IModel<LotoPrintout> model(LotoPrintout object) {
                return null;
            }
        };
    }

//    private FIDModalWindow createModalWindow(final IModel<LotoPrintout> printoutModel, final Panel callbackPanel) {
//        FIDModalWindow modalWindow = new FIDModalWindow("modalWindow");
//        modalWindow.setPageCreator(new ModalWindow.PageCreator() {
//            @Override
//            public Page createPage() {
//                //So here, we want to return a page that we've built to show all of this data.
//                //The "add template" page, so to speak.  It should extent FieldIDAuthenticatedPage,
//                //which has no extra crap bolted to it... I think that's right, anyways.
//                return new AddLotoTemplatePanel(addTemplateWindow.getContentId());
//            }
//        });
//
//        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
//            @Override
//            public void onClose(AjaxRequestTarget target) {
//                target.add(callbackPanel);
//            }
//        });
//
//        return modalWindow;
//    }
}
