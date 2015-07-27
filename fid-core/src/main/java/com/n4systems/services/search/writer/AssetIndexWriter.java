package com.n4systems.services.search.writer;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.Asset;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.search.field.AssetIndexField;
import com.n4systems.services.search.field.IndexField;
import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import rfid.ejb.entity.InfoOptionBean;

import javax.persistence.EntityManager;
import java.io.IOException;

public class AssetIndexWriter extends LuceneIndexWriter<Asset> {

    private @Autowired EventService eventService;

    public AssetIndexWriter() {
        super(Asset.class);
    }

    @Override
    public String getIndexPath(Tenant tenant) {
        Preconditions.checkNotNull(tenant,"must specify a tenant when indexing");
        return String.format("/var/fieldid/private/indexes/%s/assets", tenant.getName());
    }

    @Override
    protected IndexField[] getFields() {
        return AssetIndexField.values();
    }

    @Override
    protected IndexField getField(String fieldName) {
        return AssetIndexField.fromString(fieldName);
    }

    @Override
    protected void unindex(org.apache.lucene.index.IndexWriter writer, Asset asset) throws IOException {
        unindex(writer, asset.getId(), AssetIndexField.ID.getField());
    }

    public Document createDocument(EntityManager em, Asset asset) {
        Document doc = createNewDocument();

        BaseOrg owner = asset.getOwner();

        addIdField(doc, AssetIndexField.ID.getField(), asset);

        addIdField(doc, AssetIndexField.SECONDARY_ID.getField(), owner.getSecondaryOrg());
        addIdField(doc, AssetIndexField.CUSTOMER_ID.getField(), owner.getCustomerOrg());
        addIdField(doc, AssetIndexField.DIVISION_ID.getField(), owner.getDivisionOrg());

        addField(doc, AssetIndexField.CREATED, asset.getCreated());
        addField(doc, AssetIndexField.MODIFIED, asset.getModified());
        addField(doc, AssetIndexField.IDENTIFIER, asset.getIdentifier());
        addField(doc, AssetIndexField.RFID, asset.getRfidNumber());
        addField(doc, AssetIndexField.REFERENCE_NUMBER, asset.getCustomerRefNumber());
        addField(doc, AssetIndexField.PURCHASE_ORDER, asset.getPurchaseOrder());
        addField(doc, AssetIndexField.COMMENTS, asset.getComments());
        addField(doc, AssetIndexField.IDENTIFIED, asset.getIdentified());
        addField(doc, AssetIndexField.ORDER, asset.getOrderNumber());
        addField(doc, AssetIndexField.LAST_EVENT_DATE, asset.getLastEventDate());
        addField(doc, AssetIndexField.LOCATION, asset.getAdvancedLocation().getFullName());

        addUserField(doc, AssetIndexField.CREATED_BY.getField(), asset.getCreatedBy());
        addUserField(doc, AssetIndexField.MODIFIED_BY.getField(), asset.getModifiedBy());
        addUserField(doc, AssetIndexField.IDENTIFIED_BY.getField(), asset.getIdentifiedBy());
        addUserField(doc, AssetIndexField.ASSIGNED.getField(), asset.getAssignedUser());

        addNamedEntity(doc, AssetIndexField.OWNER.getField(), owner);
        addNamedEntity(doc, AssetIndexField.INTERNAL_ORG.getField(), owner.getInternalOrg());
        addNamedEntity(doc, AssetIndexField.CUSTOMER.getField(), owner.getCustomerOrg());
        addNamedEntity(doc, AssetIndexField.DIVISION.getField(), owner.getDivisionOrg());
        addNamedEntity(doc, AssetIndexField.TYPE.getField(), asset.getType());
        addNamedEntity(doc, AssetIndexField.TYPE_GROUP.getField(), asset.getType().getGroup());
        addNamedEntity(doc, AssetIndexField.STATUS.getField(), asset.getAssetStatus());

        for (InfoOptionBean infoOption : asset.getInfoOptions()) {
            if (!infoOption.getInfoField().isRetired()) {
                addCustomField(doc, infoOption.getInfoField().getName(), parseInfoOptionValue(infoOption));
            }
        }

        // CAVEAT : always do this LAST!! after all other fields have been added 'cause it depends on existence of doc's fields.
        addAllField(AssetIndexField.ALL, doc);

        return doc;
    }

}
