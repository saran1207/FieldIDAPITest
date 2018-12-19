package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.OrgListFilterCriteria;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.columns.OrgListColumn;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.SecondaryOrgDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class OrgsListPage extends FieldIDTemplatePage {

    private static int PAGE_SIZE = 20;

    @SpringBean
    private OrgService orgService;

    private SecondaryOrgDataProvider dataProvider;
    private WebMarkupContainer orgsListContainer;
    private WebMarkupContainer primaryOrgContainer;
    private IModel<OrgListFilterCriteria> filterCriteriaModel;
    private Form filterForm;

    protected FIDFeedbackPanel feedbackPanel;

    public OrgsListPage() {
        filterCriteriaModel = getOrgListFilterCriteria();
        dataProvider = new SecondaryOrgDataProvider(filterCriteriaModel.getObject());
    }

    public OrgsListPage(PageParameters params) {
        filterCriteriaModel = getOrgListFilterCriteria();

        Long secondaryOrgId = params.get("uniqueID").toLong();
        SecondaryOrg secondaryOrg = orgService.getSecondaryOrg(secondaryOrgId);
        dataProvider = new SecondaryOrgDataProvider(filterCriteriaModel.getObject().withSecondaryOrg(secondaryOrg));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupId(true);

        add(primaryOrgContainer = new WebMarkupContainer("primaryOrgContainer"));
        primaryOrgContainer.setOutputMarkupPlaceholderTag(true);
        primaryOrgContainer.add(new Label("primaryOrgName", new PropertyModel(getPrimaryOrg(), "name")));
        primaryOrgContainer.add(new Label("primaryOrgCertificateName", new PropertyModel(getPrimaryOrg(), "certificateName")));

        add(orgsListContainer = new WebMarkupContainer("orgsListContainer"));
        orgsListContainer.setOutputMarkupId(true);
        orgsListContainer.add(new SimpleDefaultDataTable<SecondaryOrg>("orgs", getOrgTableColumns(), dataProvider, PAGE_SIZE)).setVisible(true);

        orgsListContainer.add(new Label("total", getTotalCountLabel()));
    }

    protected Model<OrgListFilterCriteria> getOrgListFilterCriteria() {
        return Model.of(new OrgListFilterCriteria(false));
    }

    protected List<IColumn<SecondaryOrg>> getOrgTableColumns() {
        List<IColumn<SecondaryOrg>> columns = Lists.newArrayList();

        columns.add(new PropertyColumn<SecondaryOrg>(new FIDLabelModel("label.organizationalunits"), "name", "name") {
            @Override
            public void populateItem(Item<ICellPopulator<SecondaryOrg>> cellItem, String componentId, IModel<SecondaryOrg> rowModel) {
                super.populateItem(cellItem, componentId, rowModel);
                cellItem.add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
            }
        });
        columns.add(new OrgListColumn());
        return columns;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_organizational_units"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, SettingsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected void addNavBar(String navBarId) {
        OrgListFilterCriteria criteria = new OrgListFilterCriteria(filterCriteriaModel.getObject());
        Long activeOrgCount = orgService.countSecondaryOrgs(criteria.withArchivedOnly(false));
        Long archivedOrgCount = orgService.countSecondaryOrgs(criteria.withArchivedOnly());

        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", activeOrgCount)).page(OrgsListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", archivedOrgCount)).page(ArchivedOrgsListPage.class).build(),
                aNavItem().label("nav.add").page(AddSecondaryOrgPage.class).onRight().build()
        ));
    }

    private LoadableDetachableModel<String> getTotalCountLabel() {
        return new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                OrgListFilterCriteria criteria = new OrgListFilterCriteria(filterCriteriaModel.getObject());
                Long totalSecondaryOrgs = criteria.isArchivedOnly()?orgService.countSecondaryOrgs(criteria.withArchivedOnly()):orgService.countSecondaryOrgs(criteria.withArchivedOnly(false));//return new FIDLabelModel("label.total_x", orgService.getSearchSecondaryOrgCount(null,null)).getObject();
                return new FIDLabelModel("label.total_x", totalSecondaryOrgs).getObject();
            }
        };
    }

    public PrimaryOrg getPrimaryOrg() {
        return getSecurityGuard().getPrimaryOrg();
    }

}
