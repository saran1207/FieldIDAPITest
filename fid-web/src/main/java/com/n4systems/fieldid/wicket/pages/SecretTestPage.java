package com.n4systems.fieldid.wicket.pages;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.services.search.AssetIndexerService;
import com.n4systems.services.search.FullTextSearchService;
import com.n4systems.services.search.SearchResult;
import com.n4systems.util.SearchRecord;
import org.apache.wicket.ajax.AjaxRequestTarget;
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

import java.util.List;

public class SecretTestPage extends FieldIDAuthenticatedPage {

	private @SpringBean PersistenceService persistenceService;
	private @SpringBean FullTextSearchService fullTextSearchService;
	private @SpringBean AssetIndexerService assetIndexerService;

	private WebMarkupContainer selectedDeviceList;
	private WebMarkupContainer selectedLockList;

	private SearchRecord mySearch;

	private String text = "08-000028";
	private String tenant = "n4";

    private List<SearchResult> docs = Lists.newArrayList();
    private final ListView<SearchResult> list;
    private final WebMarkupContainer container;

    public SecretTestPage() {
		Form form = new Form("form");
		form.add(new TextField("text", new PropertyModel<String>(this, "text")));
		form.add(new AjaxSubmitLink("submit") {
            @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                docs = fullTextSearchService.search(text).getResults();
                target.add(container);
            }

            @Override protected void onError(AjaxRequestTarget target, Form<?> form) { }
        });
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
					fullTextSearchService.findAll(tenant);
				}
			}
			.add(new TextField("tenant", new PropertyModel<String>(this, "tenant")))
			.add(new SubmitLink("submitShowAllDocs"))
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
