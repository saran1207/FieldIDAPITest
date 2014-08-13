package com.n4systems.fieldid.wicket.pages.setup.assettypegroup;

import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.fieldid.service.asset.AssetTypeGroupService;
import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.components.columnlayout.ReportColumnPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.columns.ColumnMapping;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;
import org.odlabs.wiquery.ui.sortable.SortableRevert;

import java.util.List;

/**
 * Created by tracyshi on 2014-08-07.
 */
public class ReorderAssetTypeGroupPage extends FieldIDTemplatePage {

    @SpringBean
    private AssetTypeGroupService assetTypeGroupService;

    private Button doneReorderButton;
    private ListView groupListView;
    private IModel<List<AssetTypeGroup>> assetTypeGroupsModel;
    private WebMarkupContainer groupListContainer;

    public ReorderAssetTypeGroupPage() {

        assetTypeGroupsModel = getAssetTypeGroups();
        setOutputMarkupId(true);

        // 'Reorder' button
        Form doneReorderForm = new Form("doneReorderForm") {
            @Override
            public void onSubmit() {
                setResponsePage(AssetTypeGroupListPage.class);
            }
        };
        doneReorderForm.add(new Button("doneReorderButton"));
        add(doneReorderForm);

        groupListView = new ListView<AssetTypeGroup>("groupListView",assetTypeGroupsModel ) {
            @Override
            protected void populateItem(ListItem<AssetTypeGroup> listItem) {
                AssetTypeGroup assetTypeGroup = listItem.getModelObject();
                ContextImage reorderImage = new ContextImage("reorderImage", "images/reorder.png");
                listItem.add(reorderImage);
                listItem.add(new Label("groupName", new PropertyModel<String>(assetTypeGroup, "displayName")));
                listItem.setOutputMarkupId(true);
            }
        };
        groupListView.setOutputMarkupId(true);
        groupListContainer = new WebMarkupContainer("groupListContainer");
        groupListContainer.add(groupListView);
        groupListContainer.add(makeSortableBehavior());
        groupListContainer.setOutputMarkupId(true);
        add(groupListContainer);
    }

    private LoadableDetachableModel<List<AssetTypeGroup>> getAssetTypeGroups() {
        return new LoadableDetachableModel<List<AssetTypeGroup>>() {
            @Override
            protected List<AssetTypeGroup> load() {
                return assetTypeGroupService.getAllAssetTypeGroups();
            }
        };
    }

    public boolean isGroupListEmpty() {
        return groupListView.getList().isEmpty();
    }

    protected SortableAjaxBehavior makeSortableBehavior() {
        SortableAjaxBehavior sortable = new SimpleSortableAjaxBehavior() {
            @Override
            public void onUpdate(Component sortedComponent, int index, AjaxRequestTarget target) {

                AssetTypeGroup item = (AssetTypeGroup) sortedComponent.getDefaultModelObject();
                int oldIndex = assetTypeGroupsModel.getObject().indexOf(item);

                assetTypeGroupsModel.getObject().remove(oldIndex);
                assetTypeGroupsModel.getObject().add(index,item);

                assetTypeGroupService.updateAllAssetTypeGroups(assetTypeGroupsModel.getObject());

                target.add(groupListContainer);
            }
        };
        return sortable;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.reorderassettypegroups"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/newCss/assetTypeGroup/assetTypeGroupReorderPage.css");
    }

}
