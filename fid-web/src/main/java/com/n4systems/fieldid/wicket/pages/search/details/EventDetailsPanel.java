package com.n4systems.fieldid.wicket.pages.search.details;

import com.google.common.base.Joiner;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.event.ThingEventSummaryPage;
import com.n4systems.services.search.SearchResult;
import com.n4systems.services.search.field.EventIndexField;
import com.n4systems.services.search.field.IndexField;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.EnumSet;

public class EventDetailsPanel extends SearchItemDetailsPanel {

    public EventDetailsPanel(String id, IModel<SearchResult> resultModel) {
        super(id, resultModel);
        String eventIdStr = resultModel.getObject().get(EventIndexField.ID.getField());
        Long eventId = resultModel.getObject().getLong(EventIndexField.ID.getField());

        add(new BookmarkablePageLink<ThingEventSummaryPage>("viewLink", ThingEventSummaryPage.class, PageParametersBuilder.id(eventId)));
        add(new NonWicketLink("editLink", "selectEventEdit.action?uniqueID="+ eventIdStr, new AttributeAppender("class", Model.of("mattButtonRight"), " ")));
    }

    protected String getIdentifier(SearchResult result) {
        String type = result.get(EventIndexField.EVENT_TYPE.getField());
        String id = result.get(EventIndexField.IDENTIFIER.getField());
        String eventResult = result.get(EventIndexField.RESULT.getField());
        return Joiner.on(" / ").skipNulls().join(type, id, eventResult);
    }

    @Override
    protected IndexField getIndexField(String fieldName) {
        return EventIndexField.fromString(fieldName);
    }

    @Override
    protected EnumSet<? extends IndexField> getDisplayedFixedAttributes() {
        return EventIndexField.getDisplayedFixedAttributes();
    }

    @Override
    protected Component createImageComponent(String id, SearchResult searchResult) {
        return new WebMarkupContainer(id).setVisible(false);
    }

    @Override
    protected WebMarkupContainer createSummaryLink(SearchResult searchResult) {
        Long eventId = searchResult.getLong(EventIndexField.ID.getField());
        return new BookmarkablePageLink<ThingEventSummaryPage>("summaryLink", ThingEventSummaryPage.class, PageParametersBuilder.id(eventId));
    }
}
