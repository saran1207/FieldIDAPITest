package com.n4systems.fieldid.wicket.components.loto;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Event;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Created by rrana on 2014-04-10.
 */
public class ProcedureListPanel extends Panel {

    public static final int PROCEDURES_PER_PAGE = 100;
    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;

    private FieldIDDataProvider<ProcedureDefinition> dataProvider;

    public ProcedureListPanel(String id, FieldIDDataProvider<ProcedureDefinition> dataProvider) {
        super(id);

        this.dataProvider = dataProvider;

        SimpleDefaultDataTable table;
        add(table = new SimpleDefaultDataTable<Event>("procedureTable", getProceduresTableColumns(), dataProvider, PROCEDURES_PER_PAGE));

        table.add(new AttributeAppender("class", getTableStyle()).setSeparator(" "));
    }

    private IModel<String> getTableStyle() {

        return  new Model<String>() {
            @Override
            public String getObject() {
                String attribute = "";
                if(dataProvider.size() == 0) {
                    attribute = "no_records";
                }else if ( (dataProvider.size()/PROCEDURES_PER_PAGE) == 0 || dataProvider.size() == PROCEDURES_PER_PAGE) {
                    attribute = "no_paging";
                }
                return attribute;
            }
        };
    }

    private List<IColumn<? extends ProcedureDefinition>> getProceduresTableColumns() {
        List<IColumn<? extends ProcedureDefinition>> columns = Lists.newArrayList();

        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.procedure_code"),"procedureCode", "procedureCode"));
        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.revision_#"),"revisionNumber", "revisionNumber"));
        columns.add(new ProcedureAssetColumn(new FIDLabelModel("label.equipment_#"),"equipmentNumber", "equipmentNumber"));
        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.equipment_location"),"equipmentLocation", "equipmentLocation"));
        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.equipment_type"),"asset.type.name", "asset.type.name"));
        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.building"),"building", "building"));
        columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.developed_by"),"developedBy.firstName", "developedBy.fullName"));
        columns.add(new ProcedureDateColumn(new FIDLabelModel("label.created"), "created", "created"));

        addActionColumn(columns);
        return columns;
    }

    protected void addActionColumn(List<IColumn<? extends ProcedureDefinition>> columns) {}

    protected void addCustomColumns(List<IColumn<? extends ProcedureDefinition>> columns) {}

    public FieldIDDataProvider<ProcedureDefinition> getDataProvider() {
        return dataProvider;
    }

}
