package com.n4systems.fieldid.wicket.pages.search.details;

import com.google.common.base.Joiner;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.services.search.SearchResult;
import com.n4systems.services.search.field.EventIndexField;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class EventDetailsPanel extends Panel {

    public EventDetailsPanel(String id, IModel<SearchResult> resultModel) {
        super(id);

        SearchResult result = resultModel.getObject();

        String assetId = result.get(EventIndexField.ID.getField());

        NonWicketLink summaryLink;
        add(summaryLink = new NonWicketLink("summaryLink", "event.action?uniqueID="+ assetId));
        summaryLink.add(new Label("summary", getIdentifier(result)).setEscapeModelStrings(false));

        add(new NonWicketLink("viewLink", "event.action?uniqueID="+ assetId, new AttributeAppender("class", Model.of("mattButtonLeft"), " ")));
        add(new NonWicketLink("editLink", "selectEventEdit.action?uniqueID="+ assetId, new AttributeAppender("class", Model.of("mattButtonRight"), " ")));
    }

    private String getIdentifier(SearchResult result) {
        String type = result.get(EventIndexField.EVENT_TYPE.getField());
        String id = result.get(EventIndexField.IDENTIFIER.getField());
        String workflowState = result.get(EventIndexField.WORKFLOW_STATE.getField());
        String eventResult = result.get(EventIndexField.RESULT.getField());
        return Joiner.on(" / ").skipNulls().join(type, id, workflowState, eventResult);
    }

}
