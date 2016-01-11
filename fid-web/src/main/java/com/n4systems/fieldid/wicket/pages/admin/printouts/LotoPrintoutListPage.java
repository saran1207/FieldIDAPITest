package com.n4systems.fieldid.wicket.pages.admin.printouts;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.model.LotoPrintout;
import com.n4systems.reporting.PathHandler;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

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

    private @SpringBean
    LotoReportService lotoReportService;

    private @SpringBean
    UserService userService;

    private Long tenantId;
    private FIDFeedbackPanel feedbackPanel;
    private WebMarkupContainer listContainer;

    private ModalWindow addTemplateWindow;

    public LotoPrintoutListPage(PageParameters pageParameters) {
        super();
        tenantId = pageParameters.get("id").toLong();
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


        add(addTemplateWindow = new DialogModalWindow("addTemplateModalWindow").setInitialHeight(400).setInitialWidth(600));

        addTemplateWindow.setContent(new AddLotoTemplatePanel(addTemplateWindow.getContentId(), tenantId));

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

        columns.add(new PropertyColumn<LotoPrintout>(new FIDLabelModel("label.directory"), "s3Path", "s3Path") {
            @Override
            public void populateItem(Item<ICellPopulator<LotoPrintout>> item, String componentId, IModel<LotoPrintout> rowModel) {
                //String path = PathHandler.getLotoPath(rowModel.getObject());
                item.add(new Label(componentId, PathHandler.getLotoDisplayString(rowModel.getObject())));
            }
        });

        columns.add(new PropertyColumn<LotoPrintout>(new FIDLabelModel("label.dateUploaded"), "created", "created") {
            @Override
            public void populateItem(Item<ICellPopulator<LotoPrintout>> item, String componentId, IModel<LotoPrintout> rowModel) {
                Date created = rowModel.getObject().getCreated();
                item.add(new Label(componentId, created.toString()));
            }
        });

        columns.add(new LotoDownloadColumn(new FIDLabelModel("label.download"),"download"));

        columns.add(new LotoDeleteColumn(new FIDLabelModel("label.delete"),"delete", listContainer));

        return columns;
    }

    private FieldIDDataProvider<LotoPrintout> getDataProvider() {
        return new FieldIDDataProvider<LotoPrintout>() {
            @Override
            public Iterator<? extends LotoPrintout> iterator(int first, int count) {
                //return null;
                return lotoReportService.getLotoReportsByTenantId(tenantId).iterator();
            }

            @Override
            public int size() {
                return lotoReportService.getLotoReportsByTenantId(tenantId).size();
                //return 0;
            }

            @Override
            public IModel<LotoPrintout> model(LotoPrintout object) {
                return new AbstractReadOnlyModel<LotoPrintout>() {
                    @Override
                    public LotoPrintout getObject() {
                        return object;
                    }
                };
            }
        };
    }

}
