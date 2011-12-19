package com.n4systems.fieldid.wicket.components.reporting.columns;

import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class SelectDisplayColumnsPanel extends Panel {

    private WebMarkupContainer dynamicColumnsContainer;

    public SelectDisplayColumnsPanel(String id, IModel<List<ColumnMappingGroupView>> columnsModel, IModel<List<ColumnMappingGroupView>> dynamicColumnsModel) {
        super(id);
        setOutputMarkupId(true);

        Form columnsForm = new Form("columnsForm");
        add(columnsForm);

        columnsForm.add(new ListView<ColumnMappingGroupView>("columnGroups", columnsModel) {
            @Override
            protected void populateItem(ListItem<ColumnMappingGroupView> item) {
                item.add(new FlatLabel("groupLabel", new FIDLabelModel(new PropertyModel<String>(item.getModel(), "label"))));
                addColumns(item);
            }
        });

        columnsForm.add(dynamicColumnsContainer = new WebMarkupContainer("dynamicColumnsContainer"));
        dynamicColumnsContainer.setOutputMarkupId(true);

        dynamicColumnsContainer.add(new ListView<ColumnMappingGroupView>("dynamicColumnGroups", dynamicColumnsModel) {
            @Override
            protected void populateItem(final ListItem<ColumnMappingGroupView> item) {
                item.add(new FlatLabel("groupLabel", new FIDLabelModel(new PropertyModel<String>(item.getModel(), "label"))));
                item.add(new WebMarkupContainer("noCommonAttributesMessage").setVisible(item.getModelObject().getMappings().isEmpty()));
                addColumns(item);
            }
        });
    }

    private void addColumns(ListItem<ColumnMappingGroupView> item) {
        item.add(new ListView<ColumnMappingView>("columns", new PropertyModel<List<ColumnMappingView>>(item.getModelObject(), "mappings")) {
            @Override
            protected void populateItem(ListItem<ColumnMappingView> columnItem) {
                columnItem.add(new Label("checkboxLabel", new FIDLabelModel(new PropertyModel<String>(columnItem.getModel(), "label"))));
                columnItem.add(new CheckBox("checkbox", new PropertyModel<Boolean>(columnItem.getModel(), "enabled")));
            }
        });
    }

}
