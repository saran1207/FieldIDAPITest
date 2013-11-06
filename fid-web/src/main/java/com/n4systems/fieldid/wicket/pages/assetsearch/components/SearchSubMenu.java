package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.TwoStateAjaxLink;
import com.n4systems.fieldid.wicket.components.assetsearch.AssetSearchMassActionLink;
import com.n4systems.fieldid.wicket.components.search.results.MassActionLink;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.massupdate.MassUpdateAssetsPage;
import com.n4systems.fieldid.wicket.pages.print.ExportSearchToExcelPage;
import com.n4systems.fieldid.wicket.pages.print.PrintAllCertificatesPage;
import com.n4systems.fieldid.wicket.pages.reporting.MassSchedulePage;
import com.n4systems.fieldid.wicket.pages.saveditems.send.SendSavedItemPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.services.search.AssetIndexerService;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

import static ch.lambdaj.Lambda.on;


public abstract class SearchSubMenu extends SubMenu<AssetSearchCriteria> {

    @SpringBean AssetIndexerService assetIndexerService;

    private WebMarkupContainer actions;
    private Form queryForm;

    private Link printLink;
    private Link exportLink;
    private Link massEventLink;
    private Link massUpdateLink;
    private Link massScheduleLink;

    private WebMarkupContainer remainingAssetsWarning;

    public SearchSubMenu(String id, final Model<AssetSearchCriteria> searchCriteria) {
        super(id,searchCriteria);

        // Ideally this should be done more generically one level up but text search needs to be supported on all the search screens first..
        filtersDisabled =  searchCriteria.getObject().getQuery() != null;
        addMattBar();

        queryForm = new Form("queryForm") {
            @Override
            protected void onSubmit() {
                searchCriteria.getObject().setReportAlreadyRun(true);
                searchCriteria.getObject().getSelection().clear();
                onSearchSubmit();
            }
        };
        queryForm.setOutputMarkupPlaceholderTag(true);
        queryForm.setVisible(filtersDisabled);
        queryForm.add(new TextField<String>("query", ProxyModel.of(searchCriteria, on(AssetSearchCriteria.class).getQuery())));
        queryForm.add(new Button("submitQueryButton"));
        final LoadableDetachableModel<Long> remainingAssetsIndexModel = remainingAssetsIndexModel();
        queryForm.add(remainingAssetsWarning = new WebMarkupContainer("remainingAssetsWarning") {
            @Override
            public boolean isVisible() {
                // TODO: Change after styling.
                return true;
//                return remainingAssetsIndexModel.getObject() > 0L;
            }
        });
        remainingAssetsWarning.add(new TipsyBehavior(new StringResourceModel("assets_remaining_to_be_index", this, Model.of("ignored-awful-api"), remainingAssetsIndexModel), TipsyBehavior.Gravity.W));
        remainingAssetsWarning.setOutputMarkupPlaceholderTag(true);
        add(createToggleSearchLink(filtersDisabled, searchCriteria));
        add(queryForm);

        actions = new WebMarkupContainer("actions");

        actions.add(printLink = makeLinkLightBoxed(new MassActionLink<PrintAllCertificatesPage>("printAllCertsLink", PrintAllCertificatesPage.class, searchCriteria)));
        actions.add(exportLink = makeLinkLightBoxed(new MassActionLink<ExportSearchToExcelPage>("exportToExcelLink", ExportSearchToExcelPage.class, searchCriteria)));

        actions.add(massEventLink = new AssetSearchMassActionLink("massEventLink", "/multiEvent/selectEventType.action?searchContainerKey="+ WebSessionMap.SEARCH_CRITERIA+"&searchId=%s", searchCriteria));
        actions.add(massUpdateLink = new Link("massUpdateLink") {
            @Override public void onClick() {
                setResponsePage(new MassUpdateAssetsPage(searchCriteria));
            }
        });
        actions.add(massScheduleLink = new Link("massScheduleLink") {
            @Override public void onClick() {
                setResponsePage(new MassSchedulePage(searchCriteria));
            }
        });

        add(actions);

        add(new Link("emailLink") {
            @Override
            public void onClick() {
                setResponsePage(new SendSavedItemPage(searchCriteria, getPage()));
            }
        });
        add(new SaveMenu("saveMenu") {
            @Override protected Link createSaveLink(String id) {
                return SearchSubMenu.this.createSaveLink(id);
            }
            @Override protected Link createSaveAsLink(String id) {
                return SearchSubMenu.this.createSaveAsLink(id);
            }
        });

        initializeLimits();
    }

    private TwoStateAjaxLink createToggleSearchLink(final boolean startingInQueryMode, final IModel<AssetSearchCriteria> criteria) {
        return new TwoStateAjaxLink("toggleSearchTypeLink", new FIDLabelModel("label.advanced_search"), new FIDLabelModel("label.simple_search")) {
            {
                setInitialState(!startingInQueryMode);
                linkContainer.add(new AttributeAppender("class", Model.of("simple-search"), " ") {
                    @Override
                    public boolean isEnabled(Component component) {
                        return queryForm.isVisible();
                    }
                });
            }
            @Override
            protected void onEnterInitialState(AjaxRequestTarget target) {
                // Entering simple search
                criteria.getObject().setQuery(null);
                target.add(queryForm.setVisible(false));
                setFiltersDisabled(target, false);
            }

            @Override
            protected void onEnterSecondaryState(AjaxRequestTarget target) {
                // Entering advanced search
                assetIndexerService.reindexTenantIfNotStartedAlready();
                target.add(queryForm.setVisible(true));
                setFiltersDisabled(target, true);
                target.appendJavaScript("fieldIdWidePage.updateConfig(false);");
                // The following works when typed into the console and doesn't work when used as an appendJavaScript from wicket. wtf...
//                target.appendJavaScript("jQuery('#"+remainingAssetsWarning.getMarkupId()+"').tipsy('show');");
            }
        };
    }

    @Override
    protected String getNoneSelectedMsgKey() {
        return "label.select_assets";
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        if (filtersDisabled) {
            response.renderOnDomReadyJavaScript("fieldIdWidePage.updateConfig(false);");
        }
    }

    protected void updateMenuBeforeRender(AssetSearchCriteria criteria) {
        super.updateMenuBeforeRender(criteria);

        int selected = criteria.getSelection().getNumSelectedIds();
        boolean rowsSelected = selected > 0;
        exportLink.setVisible(rowsSelected && (selected < maxExport));

        boolean isManufacturerCertificate = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.ManufacturerCertificate);

        printLink.setVisible(rowsSelected && (selected < maxPrint) && isManufacturerCertificate);
        
        SessionUser sessionUser = FieldIDSession.get().getSessionUser();

        massEventLink.setVisible(sessionUser.hasAccess("createevent"));
        massUpdateLink.setVisible(sessionUser.hasAccess("tag"));
        massScheduleLink.setVisible(sessionUser.hasAccess("createevent"));
        
        actions.setVisible(rowsSelected && (selected < maxUpdate) && (massEventLink.isVisible() || massUpdateLink.isVisible() || massScheduleLink.isVisible()));
    }

    protected void onSearchSubmit() { }

    protected LoadableDetachableModel<Long> remainingAssetsIndexModel() {
        return new LoadableDetachableModel<Long>() {
            @Override
            protected Long load() {
                return assetIndexerService.countRemainingIndexItemsForTenant();
            }
        };
    }


}
