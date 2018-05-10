package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.table.StyledAjaxPagingNavigator;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.DivisionOrgPaginatedLoader;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;

/**
 * Created by agrabovskis on 2018-02-09.
 */
abstract public class CustomerDivisionsListPanel extends Panel {

    static final private Logger logger = Logger.getLogger(CustomerDivisionsListPanel.class);

    private IModel<Long> customerSelectedForEditModel;
    private LoaderFactory loaderFactory;

    @SpringBean
    private OrgService orgService;

    @SpringBean
    private ConfigService configService;

    @SpringBean
    private PlaceService placeService;

    private int currentResultCount = 0;
    private int currentArchivedResultCount = 0;
    private boolean expandArchiveResults = false;

    public CustomerDivisionsListPanel(String id, IModel<Long> customerSelectedForEditModel, LoaderFactory loaderFactory) {
        super(id);
        this.customerSelectedForEditModel = customerSelectedForEditModel;
        this.loaderFactory = loaderFactory;
        addComponents();
    }

    private void addComponents() {

        final Integer pageSize = ConfigService.getInstance().getInteger(ConfigEntry.WEB_PAGINATION_PAGE_SIZE);

        /* Create loaders within models to avoid serializing them */
        final IModel<DivisionOrgPaginatedLoader> divisionLoaderModel =
                new LoadableDetachableModel<DivisionOrgPaginatedLoader>() {

            protected DivisionOrgPaginatedLoader load() {
                DivisionOrgPaginatedLoader loader = getLoaderFactory().createDivisionOrgPaginatedLoader();
                loader.setPageSize(pageSize);
                loader.setCustomerFilter((CustomerOrg) orgService.findById(customerSelectedForEditModel.getObject()));
                return loader;
            }
        };
        currentResultCount = (int) divisionLoaderModel.getObject().load().getTotalResults();

        final IModel<DivisionOrgPaginatedLoader> archivedDivisionLoaderModel =
                new LoadableDetachableModel<DivisionOrgPaginatedLoader>() {

                    protected DivisionOrgPaginatedLoader load() {
                        DivisionOrgPaginatedLoader loader = getLoaderFactory().createDivisionOrgPaginatedLoader();
                        loader.setPageSize(pageSize);
                        loader.setCustomerFilter((CustomerOrg) orgService.findById(customerSelectedForEditModel.getObject()));
                        loader.setArchivedOnly(true);
                        return loader;
                    }
                };
        currentArchivedResultCount = (int) archivedDivisionLoaderModel.getObject().load().getTotalResults();

        /* Create the top level containers */

        final WebMarkupContainer resultList = new WebMarkupContainer("divisionList") {
            @Override
            public boolean isVisible() {
                return currentResultCount > 0;
            }
        };
        resultList.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        add(resultList);

        final WebMarkupContainer noResults = new WebMarkupContainer("noResults") {
            @Override
            public boolean isVisible() {
                return currentResultCount == 0;
            }
        };
        noResults.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        add(noResults);

        final WebMarkupContainer archiveDisplayControl = new WebMarkupContainer("archiveDisplayControl") {
            @Override
            public boolean isVisible() {
                return currentArchivedResultCount > 0;
            }
        };
        archiveDisplayControl.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        add(archiveDisplayControl);

        WebMarkupContainer archivedResultsSection = new WebMarkupContainer("archivedList") {
            @Override
            public boolean isVisible() {
                return expandArchiveResults && currentArchivedResultCount > 0;
            }
        };
        archivedResultsSection.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        add(archivedResultsSection);

        /* Create the non archived results section content */

        final IDataProvider divisionDataProvider = new DivisionDataProvider(divisionLoaderModel, currentResultCount, pageSize);

        final DataView<DivisionOrg> lineItemsView =
                new DataView<DivisionOrg>("result.list", divisionDataProvider) {
                    @Override
                    public void populateItem(final Item<DivisionOrg> item) {

                        final DivisionOrg division = item.getModelObject();
                        item.add(new AjaxLink("divisionEditLink") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                invokeEdit(division, target);
                            }
                        }.add(new Label("label", Model.of(division.getName()))));
                        item.add(new Label("divisionId", Model.of(division.getCode())));
                        item.add(new AjaxLink("divisionEditLink2") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                invokeEdit(division, target);
                            }
                        });
                        item.add(new AjaxLink("divisionArchiveLink") {
                            @Override
                            public IAjaxCallDecorator getAjaxCallDecorator() {
                                return new AjaxCallDecorator() {
                                    @Override
                                    public CharSequence decorateScript(Component c, CharSequence script) {
                                        return "if (!confirm('" +
                                                getString("label.areyousurearchivecustomer") + "')) return false;" + script;
                                    }
                                };
                            }
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                if (doArchive(division)) {
                                    divisionLoaderModel.detach();
                                    archivedDivisionLoaderModel.detach();
                                    expandArchiveResults = true;
                                    target.add(resultList);
                                    target.add(noResults);
                                    target.add(archiveDisplayControl);
                                    target.add(archivedResultsSection);
                                }
                                target.addChildren(getPage(), FeedbackPanel.class);
                            }
                        });
                    }
                };

        lineItemsView.setOutputMarkupId(true);
        lineItemsView.setItemsPerPage(pageSize);


        resultList.add(lineItemsView);
        resultList.add(new StyledAjaxPagingNavigator("resultTableNavigatorTop", lineItemsView, resultList) {
            @Override
            protected void onAjaxEvent(AjaxRequestTarget target) {
                target.add(resultList);
            }
        });
        resultList.add(new StyledAjaxPagingNavigator("resultTableNavigatorBottom", lineItemsView, resultList) {
            @Override
            protected void onAjaxEvent(AjaxRequestTarget target) {
                target.add(resultList);
            }
        });

        /* Create the control buttons for the archived results section */

        final AjaxLink expandArchiveList = new AjaxLink("expand_archived_list") {
            @Override
            public boolean isVisible() {
                return !expandArchiveResults;
            }
            @Override
            public void onClick(AjaxRequestTarget target) {
                expandArchiveResults = true;
                target.add(archivedResultsSection);
                target.add(archiveDisplayControl);
            }
        };
        expandArchiveList.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        archiveDisplayControl.add(expandArchiveList);

        final AjaxLink collapseArchiveList = new AjaxLink("collapse_archived_list") {
            @Override
            public boolean isVisible() {
                return expandArchiveResults;
            }
            @Override
            public void onClick(AjaxRequestTarget target) {
                expandArchiveResults = false;
                target.add(archivedResultsSection);
                target.add(archiveDisplayControl);
            }
        };
        collapseArchiveList.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        archiveDisplayControl.add(collapseArchiveList);


        /* Create the archived results section contents */

        final IDataProvider archivedDivisionDataProvider =
                new DivisionDataProvider(archivedDivisionLoaderModel, currentArchivedResultCount, pageSize);

        final DataView<DivisionOrg> archivedLineItemsView =
                new DataView<DivisionOrg>("result.list", archivedDivisionDataProvider) {
                    @Override
                    public void populateItem(final Item<DivisionOrg> item) {

                        final DivisionOrg division = item.getModelObject();
                        item.add(new Label("divisionName", Model.of(division.getName())));
                        item.add(new Label("divisionId", Model.of(division.getCode())));
                        item.add(new AjaxLink("divisionUnarchiveLink") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                if (doUnarchive(division)) {
                                    divisionDataProvider.detach();
                                    archivedDivisionDataProvider.detach();
                                    expandArchiveResults = false;
                                    target.add(resultList);
                                    target.add(noResults);
                                    target.add(archiveDisplayControl);
                                    target.add(archivedResultsSection);
                                }
                                target.addChildren(getPage(), FeedbackPanel.class);
                            }
                        });
                    }
                };
        archivedLineItemsView.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        archivedResultsSection.add(archivedLineItemsView);

        archivedResultsSection.add(
                new StyledAjaxPagingNavigator("resultTableNavigatorTop", archivedLineItemsView, archivedResultsSection) {
            @Override
            protected void onAjaxEvent(AjaxRequestTarget target) {
                target.add(resultList);
            }
        });
        archivedResultsSection.add(
                new StyledAjaxPagingNavigator("resultTableNavigatorBottom", archivedLineItemsView, archivedResultsSection) {
            @Override
            protected void onAjaxEvent(AjaxRequestTarget target) {
                target.add(resultList);
            }
        });
    }

    private boolean doArchive(DivisionOrg division) {
        boolean result = setDivisionActive(division, false);
        if (result)
            info("Archive successful");
        return result;
    }

    private boolean doUnarchive(DivisionOrg division) {
        boolean result = setDivisionActive(division, true);
        if (result)
            info("Unarchive successful");
        return result;
    }

    abstract void invokeEdit(DivisionOrg division, AjaxRequestTarget target);

    private boolean setDivisionActive(DivisionOrg division, boolean active) {

        if (division == null) {
            error("Division not found");
            return false;
        }
        try {
            if (!active) {
                placeService.archive(division);
            } else {
                placeService.unarchive(division);
            }
            return true;

        } catch (Exception e) {
            logger.error("Failed updating division", e);
            error(getString("error.updatingdivision"));
            return false;
       }
    }

    private LoaderFactory getLoaderFactory() {
        return loaderFactory;
    }



    private class DivisionDataProvider implements IDataProvider<DivisionOrg> {

        private IModel<DivisionOrgPaginatedLoader> loader;
        private int currentCount;
        private int pageSize;

        public DivisionDataProvider(IModel<DivisionOrgPaginatedLoader> loader, int currentCount, int pageSize) {
            this.loader = loader;
            this.currentCount = currentCount;
            this.pageSize = pageSize;
        }

        @Override
        public void detach() {
        }

        /* Gets an iterator for the subset of total data */
        @Override
        public Iterator iterator(int first, int count) {
            int currentPage = first / pageSize + 1;
            loader.getObject().setPage(currentPage);
            return loader.getObject().load().getList().iterator();
        }

        /* Gets total number of items in the collection represented by the DataProvider */
        @Override
        public int size() {
            return currentCount;
        }

        @Override
        public IModel model(DivisionOrg object) {
            return new AbstractReadOnlyModel<DivisionOrg>() {
                @Override
                public DivisionOrg getObject() {
                    return object;
                }
            };
        }
    }


}
