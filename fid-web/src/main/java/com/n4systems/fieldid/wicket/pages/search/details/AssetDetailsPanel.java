package com.n4systems.fieldid.wicket.pages.search.details;

import com.google.common.base.Joiner;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.LatentImage;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.event.QuickEventPage;
import com.n4systems.model.Asset;
import com.n4systems.services.search.SearchResult;
import com.n4systems.services.search.field.AssetIndexField;
import com.n4systems.services.search.field.IndexField;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.EnumSet;

public class AssetDetailsPanel extends SearchItemDetailsPanel {

    @SpringBean private S3Service s3Service;
    @SpringBean private PersistenceService persistenceService;
    @SpringBean private AssetService assetService;

    public AssetDetailsPanel(String id, IModel<SearchResult> resultModel) {

        super(id, resultModel);
        final Long assetId = resultModel.getObject().getLong(AssetIndexField.ID.getField());
        final boolean hasCreateEvent = FieldIDSession.get().getSessionUser().hasAccess("createevent");
        final boolean hasEditEvent = FieldIDSession.get().getSessionUser().hasAccess("editevent");
        add(new Link("viewLink") {
            @Override
            public void onClick() {
                setResponsePage(new AssetSummaryPage(new PageParameters().add("uniqueID", assetId)));
            }
        });

        add(new BookmarkablePageLink("startEventLink", QuickEventPage.class, PageParametersBuilder.id(assetId)).setVisible(hasCreateEvent));
        add(new NonWicketLink("mergeLink", "assetMergeAdd.action?uniqueID=" + assetId, new AttributeAppender("class", "mattButtonRight")).setVisible(hasEditEvent));
    }

    protected String getIdentifier(SearchResult result) {
        String type = result.get(AssetIndexField.TYPE.getField());
        String id = result.get(AssetIndexField.IDENTIFIER.getField());
        String status = result.get(AssetIndexField.STATUS.getField());
        return Joiner.on(" / ").skipNulls().join(type, id, status);
    }

    @Override
    protected IndexField getIndexField(String fieldName) {
        return AssetIndexField.fromString(fieldName);
    }

    @Override
    protected EnumSet<? extends IndexField> getDisplayedFixedAttributes() {
        return AssetIndexField.getDisplayedFixedAttributes();
    }

    @Override
    protected Component createImageComponent(String id, final SearchResult searchResult) {
        return new LatentImage("assetImage") {
            @Override
            protected String updateSrc() {
                final Long assetId = searchResult.getLong(AssetIndexField.ID.getField());
                Asset asset = assetService.findById(assetId);
                // CAVEAT : careful about making assumptions regarding asset Ids retrieved from search results (ie. lucene index).
                // it is entirely possible that it might be a stale id and therefore might not exist.
                // e.g. if user deletes an asset and you search for it before the asset index has been updated.
                // .: lesson learned is always guard against nulls!
                if (asset == null || StringUtils.isBlank(asset.getImageName())) {
                    return null;
                } else {
                    return s3Service.getAssetProfileImageThumbnailURL(assetId, asset.getImageName()).toString();
                }
            }
        };
    }

    @Override
    protected WebMarkupContainer createSummaryLink(SearchResult searchResult) {
        final Long assetId = searchResult.getLong(AssetIndexField.ID.getField());
        return new BookmarkablePageLink<Void>("summaryLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(assetId));
    }
}
