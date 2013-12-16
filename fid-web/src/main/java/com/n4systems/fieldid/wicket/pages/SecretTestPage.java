package com.n4systems.fieldid.wicket.pages;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.addressinfo.AddressPanel;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.EventType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.search.AssetFullTextSearchService;
import com.n4systems.services.search.AssetIndexerService;
import com.n4systems.services.search.SearchResult;
import com.n4systems.util.SearchRecord;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.resources.CoreJavaScriptResourceReference;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

public class SecretTestPage extends FieldIDAuthenticatedPage {

    private @SpringBean PersistenceService persistenceService;
    private @SpringBean AssetFullTextSearchService assetFullTextSearchService;
    private @SpringBean AssetIndexerService assetIndexerService;

    private WebMarkupContainer selectedDeviceList;
    private WebMarkupContainer selectedLockList;

    private SearchRecord mySearch;

    private String text = null;
    private String tenant = "n4";
    private String language = "fr";
    private BaseOrg org;

    private List<SearchResult> docs = Lists.newArrayList();
    private List<EventType> testEntities = Lists.newArrayList();
    private AddressInfo address = new AddressInfo();
    private GoogleMap map;

    public SecretTestPage() {
//        address.setGpsLocation(new GpsLocation(178523L, 12728L));
//        address.setInput("111 Queen St East, Toronto");

        Form form = new Form("form");
        form.add(map=new GoogleMap("map"));
        form.add(new AddressPanel("address", new PropertyModel(this,"address")).withExternalMap(map.getJsVar()));
        form.add(new SubmitLink("submit") {
            @Override public void onSubmit() {
                super.onSubmit();
            }

            @Override public void onError() {
                super.onError();
            }
        });
        add(form);
    }

    private Locale getLocaleFromForm() {
        return StringUtils.parseLocaleString(language);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference(CoreJavaScriptResourceReference.get());
        response.renderCSSReference("style/reset.css");
        response.renderCSSReference("style/site_wide.css");
        response.renderCSSReference("style/fieldid.css");
    }

}
