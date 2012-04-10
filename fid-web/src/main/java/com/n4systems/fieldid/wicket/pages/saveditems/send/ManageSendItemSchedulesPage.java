package com.n4systems.fieldid.wicket.pages.saveditems.send;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.YesOrNoModel;
import com.n4systems.fieldid.wicket.model.time.HourToStringDisplayModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.SendSavedItemSchedule;
import com.n4systems.model.saveditem.SavedItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ManageSendItemSchedulesPage extends FieldIDFrontEndPage {

    @SpringBean
    private PersistenceService persistenceService;

    public ManageSendItemSchedulesPage(PageParameters params) {
        super(params);
        
        final WebMarkupContainer schedulesTable = new WebMarkupContainer("schedulesTable");
        schedulesTable.setOutputMarkupId(true);

        final IModel<SavedItem> savedItemModel = new EntityModel<SavedItem>(SavedItem.class, params.get("id").toLong());

        schedulesTable.add(new ListView<SendSavedItemSchedule>("schedules", new PropertyModel<List<SendSavedItemSchedule>>(savedItemModel, "sendSchedules")) {
            @Override
            protected void populateItem(final ListItem<SendSavedItemSchedule> item) {
                item.add(new Label("frequency", new FIDLabelModel(new PropertyModel<String>(item.getModel(), "frequency.label"))));
                item.add(new Label("sendTo", new FIDLabelModel(new PropertyModel<String>(item.getModel(), "emailAddressesJoined"))));
                item.add(new Label("sendToMe", new YesOrNoModel(new PropertyModel<Boolean>(item.getModel(), "sendToOwner"))));
                item.add(new Label("time", new HourToStringDisplayModel(new PropertyModel<Integer>(item.getModel(), "hourToSend"))));
                item.add(new Link("deleteLink") {
                    @Override
                    public void onClick() {
                        SavedItem savedItem = savedItemModel.getObject();
                        persistenceService.delete(item.getModelObject());
                        savedItem.getSendSchedules().remove(item.getIndex());
                        info("Successfully deleted scheduled email");
                    }
                });
            }
        });
        
        add(schedulesTable);
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.manage_send_schedules"));
    }
}
