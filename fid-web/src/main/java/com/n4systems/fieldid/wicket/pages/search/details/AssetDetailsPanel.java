package com.n4systems.fieldid.wicket.pages.search.details;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.LatentImage;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.event.QuickEventPage;
import com.n4systems.model.Asset;
import com.n4systems.services.search.SearchResult;
import com.n4systems.services.search.field.AssetIndexField;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class AssetDetailsPanel extends Panel {

    private static String CUSTOM_ATTR_FORMAT = "<span class='attr'>%s=</span><span class='value'>%s</span>";

    @SpringBean private PersistenceService persistenceService;
    @SpringBean private S3Service s3Service;

    public AssetDetailsPanel(String id, IModel<SearchResult> resultModel) {
        super(id);
        SearchResult result = resultModel.getObject();

        final boolean hasCreateEvent = FieldIDSession.get().getSessionUser().hasAccess("createevent");
        final boolean hasEditEvent = FieldIDSession.get().getSessionUser().hasAccess("editevent");

        BookmarkablePageLink summaryLink;
        final Long assetId = result.getLong(AssetIndexField.ID.getField());
        add(summaryLink = new BookmarkablePageLink<Void>("summaryLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(assetId)));
        summaryLink.add(new Label("summary", getIdentifier(result)).setEscapeModelStrings(false));

        add(new Label("fixedAttributes", getFixedAttributes(result)).setEscapeModelStrings(false));

        add(new Label("customAttributes", getCustomAttributes(result)).setEscapeModelStrings(false));

        add(new Link("viewLink") {
            @Override
            public void onClick() {
                setResponsePage(new AssetSummaryPage(new PageParameters().add("uniqueID", assetId)));
            }
        });

        add(new BookmarkablePageLink("startEventLink", QuickEventPage.class, PageParametersBuilder.id(assetId)).setVisible(hasCreateEvent));
        add(new NonWicketLink("mergeLink", "assetMergeAdd.action?uniqueID=" + assetId, new AttributeAppender("class", "mattButtonRight")).setVisible(hasEditEvent));

        add(new LatentImage("assetImage") {
            @Override
            protected String updateSrc() {
                Asset asset = persistenceService.find(Asset.class, assetId);
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
        });
    }

    private String getFixedAttributes(SearchResult result) {
        List<String> fields = Lists.newArrayList();
        for (AssetIndexField field : AssetIndexField.getDisplayedFixedAttributes()) {
            if (!field.isInternal()) {
                fields.add(result.get(field.getField()));
            }
        }
        return Joiner.on(" / ").skipNulls().join(Iterables.filter(fields, new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input != null && StringUtils.isNotBlank(input);
            }
        }));
    }

    private String getIdentifier(SearchResult result) {
        String type = result.get(AssetIndexField.TYPE.getField());
        String id = result.get(AssetIndexField.IDENTIFIER.getField());
        String status = result.get(AssetIndexField.STATUS.getField());
        return Joiner.on(" / ").skipNulls().join(type, id, status);
    }

    private String getCustomAttributes(SearchResult result) {
        List<String> fields = Lists.newArrayList();
        for (String field:result.getFields()) {
            AssetIndexField f = AssetIndexField.fromString(field);
            boolean highlighted = result.isHighlighted(field);
            // if no predefined field exists, we know its a custom field .: display it.
            // if it's a predefined field but one that isn't always displayed, display it if it matches the criteria.
            if (f==null || (f.isNonDisplayedFixedAttribute() && highlighted && !f.isInternal())) {
                int index = highlighted ? 0 :fields.size();
                String key = WordUtils.capitalize(field);
                String value = result.get(field);
                fields.add(index, String.format(CUSTOM_ATTR_FORMAT, key, value));
            }
        }
        return Joiner.on("<span class='separator'>|</span>").skipNulls().join(fields.toArray(new String[fields.size()]));
    }

}
