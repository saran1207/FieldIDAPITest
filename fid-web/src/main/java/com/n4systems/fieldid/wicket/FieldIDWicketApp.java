package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.wicket.pages.columnlayout.ColumnsLayoutPage;
import com.n4systems.fieldid.wicket.pages.eventform.EventFormEditPage;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;

public class FieldIDWicketApp extends WebApplication {

    @Override
    protected void init() {
        mountBookmarkablePage("eventFormEdit", EventFormEditPage.class);
        mountBookmarkablePage("columnsLayout", ColumnsLayoutPage.class);
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return EventFormEditPage.class;
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new FieldIDSession(request);
    }

    @Override
    public RequestCycle newRequestCycle(Request request, Response response) {
        return new FieldIDRequestCycle(this, (WebRequest) request, response);
    }

}
