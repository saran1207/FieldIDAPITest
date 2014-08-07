package com.n4systems.fieldid.wicket.pages.setup.assettypegroup;

import com.n4systems.fieldid.service.asset.AssetTypeGroupService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.AssetTypeGroup;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by tracyshi on 2014-08-07.
 */
public class ReorderAssetTypeGroupPage extends FieldIDTemplatePage {

    @SpringBean
    private AssetTypeGroupService assetTypeGroupService;

    private Button doneReorderButton;
    private ListView groupListView;

    public ReorderAssetTypeGroupPage() {

        Form doneReorderForm = new Form("doneReorderForm") {
            @Override
            public void onSubmit() {
                setResponsePage(AssetTypeGroupListPage.class);
            }
        };

        doneReorderForm.add(new Button("doneReorderButton"));

        groupListView = new ListView<AssetTypeGroup>("reorderGroupList", getAssetTypeGroups()) {
            @Override
            protected void populateItem(ListItem<AssetTypeGroup> listItem) {
                AssetTypeGroup assetTypeGroup = listItem.getModelObject();
                ContextImage reorderImage = new ContextImage("reorderImage", "images/reorder.png");
                listItem.add(reorderImage);
                listItem.add(new Label("assetTypeGroupName", new PropertyModel<String>(assetTypeGroup, "displayName")));
            }
        };

        add(doneReorderForm);
        add(groupListView);

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

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.reorderassettypegroups"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/assetTypeGroupReorder.js");
        response.renderCSSReference("style/legacy/newCss/assetTypeGroup/assetTypeGroupReorderPage.css");
    }

}
