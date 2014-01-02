package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.BackToPlaceSubHeader;
import com.n4systems.fieldid.wicket.components.org.CreatePlacePanel;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.PlaceEventType;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class PlaceDescendantsPage extends PlacePage {

    private static final int ROWS_PER_PAGE = 20;

    private @SpringBean PlaceService placeService;
    private @SpringBean PersistenceService persistenceService;

    private List<PlaceEventType> eventTypes;
    private SimpleDefaultDataTable<BaseOrg> table;

    public PlaceDescendantsPage(IModel<BaseOrg> model) {
        super(model);
        init();
    }

    public PlaceDescendantsPage(PageParameters params) {
        super(params);
        init();
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        if (getOrg().isPrimary()) {
            return new Label(labelId, new FIDLabelModel("title.decendants", getOrg().getName()));
        } else if (getOrg().isSecondary()) {
            return new Label(labelId, new FIDLabelModel("title.add_view_customers", getOrg().getName()));
        } else if (getOrg().isCustomer()) {
            return new Label(labelId, new FIDLabelModel("title.add_view_divisions", getOrg().getName()));
        } else {
            return new Label(labelId);
        }
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new WebMarkupContainer(actionGroupId).setVisible(false);
    }

    @Override
    protected List<NavigationItem> createBreadCrumbs(BaseOrg org) {
        List<NavigationItem> navItems = super.createBreadCrumbs(org);
        if (org.isPrimary()) {
            navItems.add(aNavItem().label(new FIDLabelModel("label.decendants")).page(getClass()).params(PageParametersBuilder.id(org.getId())).build());
        } else if(org.isSecondary()) {
            navItems.add(aNavItem().label(new FIDLabelModel("label.add_view_customers")).page(getClass()).params(PageParametersBuilder.id(org.getId())).build());
        } else if(org.isCustomer()) {
            navItems.add(aNavItem().label(new FIDLabelModel("label.add_view_divisions")).page(getClass()).params(PageParametersBuilder.id(org.getId())).build());
        }

        return navItems;
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId).setVisible(false));
    }

    @Override
    protected Component createSubHeader(String subHeaderId) {
        return new BackToPlaceSubHeader(subHeaderId, orgModel);
    }

    @Override
    public String getMainCss() {
        return "place-add-descendant";
    }

    private void init() {

        add(table = new SimpleDefaultDataTable<BaseOrg>("descendants", getDescendantsColumns(), new DescendantsDataProvider() , ROWS_PER_PAGE));
        add(new CreatePlacePanel("createNewPlace") {
            @Override protected void onCreate(BaseOrg org, AjaxRequestTarget target) {
                persistenceService.save(org);
                resetModelObject();
            }
            @Override protected void onCancel(AjaxRequestTarget target) {
                setResponsePage(PlaceSummaryPage.class, PageParametersBuilder.id(getOrg().getId()));
            }
        }.forParentOrg(getOrg()).show());
    }

    private List<IColumn<BaseOrg>> getDescendantsColumns() {
        List<IColumn<BaseOrg>> columns = Lists.newArrayList();
        String key = getOrg().isCustomer() ? "label.divisions" :
                    getOrg().isSecondary() ? "label.customers" :
                            getOrg().isPrimary() ? "label.descendants" : "label.children";
        columns.add(new AbstractColumn<BaseOrg>(new FIDLabelModel(key)) {
            @Override public void populateItem(Item<ICellPopulator<BaseOrg>> cellItem, String componentId, final IModel<BaseOrg> rowModel) {
                cellItem.add(new OrgCell(componentId,rowModel));
            }
        });
        return columns;
    }

    class DescendantsDataProvider extends FieldIDDataProvider<BaseOrg> {

        public DescendantsDataProvider() {
        }

        @Override
        public Iterator<? extends BaseOrg> iterator(int first, int count) {
            List<? extends BaseOrg> descendants = placeService.getDescendants(getOrg(), first, count);
            return descendants.iterator();
        }

        @Override
        public int size() {
            return placeService.countDescendants(orgModel.getObject()).intValue();
        }

        @Override
        public IModel<BaseOrg> model(final BaseOrg object) {
            return new AbstractReadOnlyModel<BaseOrg>() {
                @Override
                public BaseOrg getObject() {
                    return object;
                }
            };
        }

    }

}
