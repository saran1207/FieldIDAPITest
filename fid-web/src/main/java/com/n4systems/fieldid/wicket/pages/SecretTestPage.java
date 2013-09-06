package com.n4systems.fieldid.wicket.pages;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.localization.LocalizationPanel;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.model.EventType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.persistence.localization.LocalizedText;
import com.n4systems.services.search.AssetFullTextSearchService;
import com.n4systems.services.search.AssetIndexerService;
import com.n4systems.services.search.SearchResult;
import com.n4systems.util.SearchRecord;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
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
    private final ListView<SearchResult> list;
    private final WebMarkupContainer container;
    private final ListView<EventType> localizedList;
    private final WebMarkupContainer testEntitiesContainer;
    private final LocalizedText test = new LocalizedText("blah blah balh");
    private final LocalizationPanel localizationPanel;

    public SecretTestPage() {
        Form form = new Form("form");
        form.add(new TextField("text", new PropertyModel<String>(this, "text")));
        form.add(new AjaxSubmitLink("submit") {
            @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                docs = assetFullTextSearchService.search(text).getResults();
                target.add(container);
            }
            @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                System.out.println("huh?");
            }
        });
        form.add(new OrgLocationPicker("tree", new PropertyModel(this,"org")));
        add(form);

        add(new Form("indexForm") {
            @Override
            protected void onSubmit() {
                assetIndexerService.indexTenant(tenant);
            }
        }
                .add(new TextField("tenant", new PropertyModel<String>(this, "tenant")))
                .add(new SubmitLink("submitIndex"))

        );

        add(new Form("showAllDocs") {
            @Override
            protected void onSubmit() {
                assetFullTextSearchService.findAll(tenant);
            }
        }
                .add(new TextField("tenant", new PropertyModel<String>(this, "tenant")))
                .add(new SubmitLink("submitShowAllDocs"))
        );

        add(new Form("localize")
                .add(new TextField("language", new PropertyModel<String>(this, "language")))
                .add(new TextField("testText", new PropertyModel<LocalizedText>(this,"test")))
                .add(new AjaxSubmitLink("submit") {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        testEntities = persistenceService.findAll(EventType.class);
                        target.add(testEntitiesContainer);
                    }

                    @Override
                    protected void onError(AjaxRequestTarget target, Form<?> form) {
                    }
                })
        );


        add(container=new WebMarkupContainer("container"));
        list = new ListView<SearchResult>("list", new PropertyModel(this,"docs")) {
            @Override
            protected void populateItem(ListItem<SearchResult> item) {
                SearchResult result = item.getModelObject();
                item.add(new Label("id", Model.of(result.get("id"))));
                item.add(new Label("owner", Model.of(result.get("owner"))));
                item.add(new Label("status", Model.of(result.get("assetstatus")==null?"<no status>" : result.get("assetstatus"))));
                item.add(new Label("comments", Model.of(result.get("comments"))));
                item.add(new Label("location", Model.of(result.get("location")==null?"<no location>" : result.get("location"))));
            }
        };
        container.add(list).setOutputMarkupId(true);

        add(testEntitiesContainer =new WebMarkupContainer("testEntities"));
        localizedList = new ListView<EventType>("list", new PropertyModel(this,"testEntities")) {
            @Override
            protected void populateItem(final ListItem<EventType > item) {
                final EventType result = item.getModelObject();
                item.add(new Label("text", new PropertyModel(result,"name")));
                item.add(new Label("translation", new PropertyModel(result, "name")));
                item.add(new AjaxLink("edit") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        localizationPanel.setDefaultModel(new EntityModel(EventType.class, result));
                        target.add(localizationPanel);
                    }
                });
            }
        };
        testEntitiesContainer.add(localizedList).setOutputMarkupId(true);

        add(localizationPanel = new LocalizationPanel("translation", null));
        localizationPanel.setOutputMarkupId(true);

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
