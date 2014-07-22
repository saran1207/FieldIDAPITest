package com.n4systems.fieldid.wicket.pages.saveditems.send;

import com.n4systems.model.SendSavedItemSchedule;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AddSendSavedItemPage extends SendSavedItemPage {

    public AddSendSavedItemPage(IModel<? extends SearchCriteria> criteria, Page returnToPage) {
        super(criteria, returnToPage);
    }

    public AddSendSavedItemPage(PageParameters params) {
        super(params);

        savedReportId = params.get("id").toLong();
        initializeSavedItem(savedReportId);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        SendSavedItemSchedule sendSavedItemSchedule = new SendSavedItemSchedule();
        prepopulateSubject(sendSavedItemSchedule);
        add(new SendSavedItemForm("form", new Model<SendSavedItemSchedule>(sendSavedItemSchedule)));
    }

    protected void initializeSavedItem(long savedItemId) {
        savedItem = persistenceService.find(SavedItem.class, savedItemId);
        criteria = new Model<SearchCriteria>(savedItem.getSearchCriteria());
        currentState = SCHEDULE_STATE;
        scheduleStateAvailable = true;
    }
}
