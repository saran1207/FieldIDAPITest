package com.n4systems.services.search.writer;

import com.n4systems.model.Event;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.search.field.EventIndexField;
import com.n4systems.services.search.field.IndexField;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

import javax.persistence.EntityManager;
import java.io.IOException;

public class EventIndexWriter extends LuceneIndexWriter<Event> {

    public EventIndexWriter() {
        super(Event.class);
    }

    @Override
    public String getIndexPath(Tenant tenant) {
        return String.format("/var/fieldid/private/indexes/%s/events", tenant.getName());
    }

    @Override
    protected IndexField[] getFields() {
        return EventIndexField.values();
    }

    @Override
    protected IndexField getField(String fieldName) {
        return EventIndexField.fromString(fieldName);
    }

    @Override
    protected void unindex(org.apache.lucene.index.IndexWriter writer, Event event) throws IOException {
        unindex(writer, event.getId(), EventIndexField.ID.getField());
    }

    @Override
    protected Document createDocument(EntityManager em, Event event) {
        Document doc = new Document();

        BaseOrg owner = event.getOwner();

        addIdField(doc, EventIndexField.ID.getField(), event);

        addIdField(doc, EventIndexField.SECONDARY_ID.getField(), owner.getSecondaryOrg());
        addIdField(doc, EventIndexField.CUSTOMER_ID.getField(), owner.getCustomerOrg());
        addIdField(doc, EventIndexField.DIVISION_ID.getField(), owner.getDivisionOrg());

        addField(doc, EventIndexField.CREATED, event.getCreated());
        addField(doc, EventIndexField.MODIFIED, event.getModified());
        addField(doc, EventIndexField.COMMENTS, event.getComments());
        addField(doc, EventIndexField.NOTES, event.getNotes());
        addField(doc, EventIndexField.LOCATION, event.getAdvancedLocation().getFullName());
        addField(doc, EventIndexField.IDENTIFIER, event.getAsset().getIdentifier());
        addField(doc, EventIndexField.RFID, event.getAsset().getRfidNumber());
        addField(doc, EventIndexField.REFERENCE_NUMBER, event.getAsset().getCustomerRefNumber());

        addUserField(doc, EventIndexField.CREATED_BY.getField(), event.getCreatedBy());
        addUserField(doc, EventIndexField.MODIFIED_BY.getField(), event.getModifiedBy());
        addUserField(doc, EventIndexField.PERFORMED_BY.getField(), event.getPerformedBy());
        addUserField(doc, EventIndexField.ASSIGNEE.getField(), event.getAssignee());

        addNamedEntity(doc, EventIndexField.OWNER.getField(), owner);
        addNamedEntity(doc, EventIndexField.INTERNAL_ORG.getField(), owner.getInternalOrg());
        addNamedEntity(doc, EventIndexField.CUSTOMER.getField(), owner.getCustomerOrg());
        addNamedEntity(doc, EventIndexField.DIVISION.getField(), owner.getDivisionOrg());
        addNamedEntity(doc, EventIndexField.ASSET_TYPE.getField(), event.getAsset().getType());
        addNamedEntity(doc, EventIndexField.ASSET_TYPE_GROUP.getField(), event.getAsset().getType().getGroup());
        addNamedEntity(doc, EventIndexField.ASSET_STATUS.getField(), event.getAssetStatus());

        addField(doc, EventIndexField.RESULT.getField(), event.getEventResult().getDisplayName());

        addNamedEntity(doc, EventIndexField.EVENT_TYPE.getField(), event.getType());
        addNamedEntity(doc, EventIndexField.EVENT_TYPE_GROUP.getField(), event.getType().getGroup());

        addField(doc, EventIndexField.WORKFLOW_STATE.getField(), event.getWorkflowState().getLabel());
        addField(doc, EventIndexField.DUE_DATE.getField(), event.getDueDate());
        addField(doc, EventIndexField.COMPLETED_DATE.getField(), event.getCompletedDate());
        addField(doc, EventIndexField.SCORE.getField(), event.getScore());

        for (String attributeName : event.getInfoOptionMap().keySet()) {
            addCustomField(doc, attributeName, event.getInfoOptionMap().get(attributeName));
        }

        // CAVEAT : always do this LAST!! after all other fields have been added 'cause it depends on existence of doc's fields.
        addAllField(EventIndexField.ALL, doc);

        return doc;
    }



}
