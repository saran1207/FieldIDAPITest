package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
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

public class PlaceDescendantsPage extends PlacePage {

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
    protected Component createActionGroup(String actionGroupId) {
        return new WebMarkupContainer(actionGroupId).setVisible(false);
    }

    @Override
    protected void refreshContent(AjaxRequestTarget target) {

    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId).setVisible(false));
    }

    private void init() {
        add(table = new SimpleDefaultDataTable<BaseOrg>("descendants", getDescendantsColumns(), new DescendantsDataProvider() , 10));
        add(new Label("createNewPlace", "CREATE NEW PLACE"));
    }

    private List<IColumn<BaseOrg>> getDescendantsColumns() {
        List<IColumn<BaseOrg>> columns = Lists.newArrayList();
        columns.add(new AbstractColumn<BaseOrg>(new FIDLabelModel("label.name")) {
            @Override
            public void populateItem(Item<ICellPopulator<BaseOrg>> cellItem, String componentId, final IModel<BaseOrg> rowModel) {
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
