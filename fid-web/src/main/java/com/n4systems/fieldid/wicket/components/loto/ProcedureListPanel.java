package com.n4systems.fieldid.wicket.components.loto;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by rrana on 2014-04-10.
 */
public class ProcedureListPanel extends Panel {

    public static final int PROCEDURES_PER_PAGE = 200;
    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    private FieldIDDataProvider<ProcedureDefinition> dataProvider;

    public ProcedureListPanel(String id, FieldIDDataProvider<ProcedureDefinition> dataProvider) {
        super(id);

        this.dataProvider = dataProvider;

        SimpleDefaultDataTable table;
        add(table = new SimpleDefaultDataTable<ProcedureDefinition>("procedureTable", getProceduresTableColumns(), dataProvider, PROCEDURES_PER_PAGE));

        table.add(new AttributeAppender("class", getTableStyle()).setSeparator(" "));
    }

    private IModel<String> getTableStyle() {
        return  new Model<String>() {
            @Override
            public String getObject() {
                String  attribute = "no_paging";
                return attribute;
            }
        };
    }

    private List<IColumn<? extends ProcedureDefinition>> getProceduresTableColumns() {
        List<IColumn<? extends ProcedureDefinition>> columns = Lists.newArrayList();

        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.procedure_code"),"procedureCode", "procedureCode")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<ProcedureDefinition>> item, final String componentId, final IModel<ProcedureDefinition> rowModel)
            {
                item.add(new Label(componentId, createLabelModel(rowModel))).add(new AttributeAppender("class", new Model<String>("procedure-code"), ""));
            }
        });

        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.procedure_type"),"procedureType", "procedureType.label"));

        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.revision_#"),"revisionNumber", "revisionNumber")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<ProcedureDefinition>> item, final String componentId, final IModel<ProcedureDefinition> rowModel)
            {
                item.add(new Label(componentId, createLabelModel(rowModel))).add(new AttributeAppender("class", new Model<String>("revision-number"), ""));
            }
        });

        columns.add(new ProcedureAssetColumn(new FIDLabelModel("label.equipment_#"),"equipmentNumber", "equipmentNumber"));

        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.equipment_location"),"equipmentLocation", "equipmentLocation")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<ProcedureDefinition>> item, final String componentId, final IModel<ProcedureDefinition> rowModel)
            {
                item.add(new Label(componentId, createLabelModel(rowModel))).add(new AttributeAppender("class", new Model<String>("equipment-loc"), ""));
            }
        });

        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.equipment_type"),"asset.type.name", "asset.type.name"));

        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.building"),"building", "building")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<ProcedureDefinition>> item, final String componentId, final IModel<ProcedureDefinition> rowModel)
            {
                item.add(new Label(componentId, createLabelModel(rowModel))).add(new AttributeAppender("class", new Model<String>("building"), ""));
            }
        });

        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.developed_by"),"developedBy.firstName", "developedBy.fullName")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<ProcedureDefinition>> item, final String componentId, final IModel<ProcedureDefinition> rowModel)
            {
                item.add(new Label(componentId, createLabelModel(rowModel))).add(new AttributeAppender("class", new Model<String>("developed-by"), ""));
            }
        });
        columns.add(new ProcedureDateColumn(new FIDLabelModel("label.created"), "created", "created"));

        addCustomColumns(columns);
        addActionColumn(columns);
        return columns;
    }

    protected void addActionColumn(List<IColumn<? extends ProcedureDefinition>> columns) {}

    protected void addCustomColumns(List<IColumn<? extends ProcedureDefinition>> columns) {}

    protected FIDFeedbackPanel getErrorFeedbackPanel() { return null; }

    public FieldIDDataProvider<ProcedureDefinition> getDataProvider() {
        return dataProvider;
    }

}
