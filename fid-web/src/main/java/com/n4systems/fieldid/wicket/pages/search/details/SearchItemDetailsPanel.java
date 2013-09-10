package com.n4systems.fieldid.wicket.pages.search.details;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.services.search.SearchResult;
import com.n4systems.services.search.field.IndexField;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.EnumSet;
import java.util.List;

public abstract class SearchItemDetailsPanel extends Panel {

    private static String CUSTOM_ATTR_FORMAT = "<span class='attr'>%s=</span><span class='value'>%s</span>";

    @SpringBean private PersistenceService persistenceService;

    public SearchItemDetailsPanel(String id, IModel<SearchResult> resultModel) {
        super(id);
        SearchResult result = resultModel.getObject();

        WebMarkupContainer summaryLink;
        add(summaryLink = createSummaryLink(result));
        summaryLink.add(new Label("summary", getIdentifier(result)).setEscapeModelStrings(false));

        add(new Label("fixedAttributes", getFixedAttributes(result)).setEscapeModelStrings(false));

        add(new Label("customAttributes", getCustomAttributes(result)).setEscapeModelStrings(false));

        add(createImageComponent("assetImage", result));
    }

    private String getCustomAttributes(SearchResult result) {
        List<String> fields = Lists.newArrayList();
        for (String field:result.getFields()) {
            IndexField f = getIndexField(field);
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

    private String getFixedAttributes(SearchResult result) {
        List<String> fields = Lists.newArrayList();
        for (IndexField field : getDisplayedFixedAttributes()) {
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

    protected abstract IndexField getIndexField(String fieldName);
    protected abstract String getIdentifier(SearchResult searchResult);
    protected abstract EnumSet<? extends IndexField> getDisplayedFixedAttributes();
    protected abstract Component createImageComponent(String id, SearchResult searchResult);
    protected abstract WebMarkupContainer createSummaryLink(SearchResult searchResult);

}
