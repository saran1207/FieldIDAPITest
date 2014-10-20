package com.n4systems.fieldid.wicket.pages.saveditems.send;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.SendSavedItemSchedule;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.Page;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class EditSendSavedItemPage extends SendSavedItemPage {

    private long scheduleId;
    private SendSavedItemSchedule sendSavedItemSchedule;

    public EditSendSavedItemPage(IModel<? extends SearchCriteria> criteria, Page returnToPage) {
        super(criteria, returnToPage);
    }

    public EditSendSavedItemPage(PageParameters params) {
        super(params);

        savedReportId = params.get("id").toLong();
        scheduleId = params.get("scheduleId").toLong();
        initializeSavedItem(savedReportId);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new SendSavedItemForm("form", new Model<SendSavedItemSchedule>(sendSavedItemSchedule)));
    }

    protected void initializeSavedItem(long savedItemId) {
        savedItem = persistenceService.find(SavedItem.class, savedItemId);
        criteria = new Model<SearchCriteria>(savedItem.getSearchCriteria());
        sendSavedItemSchedule = persistenceService.find(SendSavedItemSchedule.class, scheduleId);
        currentState = SCHEDULE_STATE;
        scheduleStateAvailable = true;
    }

    @Override
    public IModel<String> createCurrentSubmitLabelModel() {
        return new FIDLabelModel("label.update_schedule");
    }
}
