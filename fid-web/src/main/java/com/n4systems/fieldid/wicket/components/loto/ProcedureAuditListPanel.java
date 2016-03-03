package com.n4systems.fieldid.wicket.components.loto;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.ProcedureAuditEvent;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by rrana on 2014-06-10.
 */
public class ProcedureAuditListPanel extends Panel {

    public static final int PROCEDURES_PER_PAGE = 30;

    private FieldIDDataProvider<ProcedureAuditEvent> dataProvider;

    public ProcedureAuditListPanel(String id, FieldIDDataProvider<ProcedureAuditEvent> dataProvider) {
        super(id);

        this.dataProvider = dataProvider;

        SimpleDefaultDataTable table;
        add(table = new SimpleDefaultDataTable<Procedure>("procedureTable", getProceduresTableColumns(), dataProvider, PROCEDURES_PER_PAGE));
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

    private List<IColumn<? extends ProcedureAuditEvent>> getProceduresTableColumns() {
        List<IColumn<? extends ProcedureAuditEvent>> columns = Lists.newArrayList();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        final Date date7DaysFromNow = cal.getTime();

        columns.add(new ProcedureAuditDateColumn(new FIDLabelModel("label.schedule_date"),"dueDate", "dueDate"));

        columns.add(new ProcedureAuditDueDateColumn(new FIDLabelModel("label.due"),"dueDate", "dueDate"));

        columns.add(new PropertyColumn<ProcedureAuditEvent>(new FIDLabelModel("label.assigned_to"),"assignedUserOrGroup.displayName", "assignedUserOrGroup.displayName")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<ProcedureAuditEvent>> item, final String componentId, final IModel<ProcedureAuditEvent> rowModel)
            {
                item.add(new Label(componentId, createLabelModel(rowModel))).add(new AttributeAppender("class", new Model<String>("developed-by"), ""));

                if(rowModel.getObject().getDueDate().before(new Date())) {
                    item.add(new AttributeAppender("class", new Model<String>("overdue"), " "));
                } else if (rowModel.getObject().getDueDate().before(date7DaysFromNow)) {
                    item.add(new AttributeAppender("class", new Model<String>("sevenDays"), " "));
                }
            }
        });

        columns.add(new PropertyColumn<ProcedureAuditEvent>(new FIDLabelModel("label.procedure_code"),"procedureDefinition.procedureCode", "procedureDefinition.procedureCode")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<ProcedureAuditEvent>> item, final String componentId, final IModel<ProcedureAuditEvent> rowModel)
            {
                item.add(new Label(componentId, createLabelModel(rowModel))).add(new AttributeAppender("class", new Model<String>("procedure-code"), ""));

                if(rowModel.getObject().getDueDate().before(new Date())) {
                    item.add(new AttributeAppender("class", new Model<String>("overdue"), " "));
                } else if (rowModel.getObject().getDueDate().before(date7DaysFromNow)) {
                    item.add(new AttributeAppender("class", new Model<String>("sevenDays"), " "));
                }
            }
        });


        columns.add(new PropertyColumn<ProcedureAuditEvent>(new FIDLabelModel("label.procedure_type"),"procedureDefinition.procedureType", "procedureDefinition.procedureType.label"){
            @Override
            public void populateItem(final Item<ICellPopulator<ProcedureAuditEvent>> item, final String componentId, final IModel<ProcedureAuditEvent> rowModel)
            {
                item.add(new Label(componentId, createLabelModel(rowModel)));

                if(rowModel.getObject().getDueDate().before(new Date())) {
                    item.add(new AttributeAppender("class", new Model<String>("overdue"), " "));
                } else if (rowModel.getObject().getDueDate().before(date7DaysFromNow)) {
                    item.add(new AttributeAppender("class", new Model<String>("sevenDays"), " "));
                }
            }
        });

        columns.add(new ProcedureAudiAssetColumn(new FIDLabelModel("label.equipment_#"),"procedureDefinition.equipmentNumber", "procedureDefinition.equipmentNumber"));

        columns.add(new PropertyColumn<ProcedureAuditEvent>(new FIDLabelModel("label.equipment_location"),"procedureDefinition.equipmentLocation", "procedureDefinition.equipmentLocation")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<ProcedureAuditEvent>> item, final String componentId, final IModel<ProcedureAuditEvent> rowModel)
            {
                item.add(new Label(componentId, createLabelModel(rowModel))).add(new AttributeAppender("class", new Model<String>("equipment-loc"), ""));

                if(rowModel.getObject().getDueDate().before(new Date())) {
                    item.add(new AttributeAppender("class", new Model<String>("overdue"), " "));
                } else if (rowModel.getObject().getDueDate().before(date7DaysFromNow)) {
                    item.add(new AttributeAppender("class", new Model<String>("sevenDays"), " "));
                }
            }
        });

        columns.add(new PropertyColumn<ProcedureAuditEvent>(new FIDLabelModel("label.equipment_type"),"procedureDefinition.asset.type.name", "procedureDefinition.asset.type.name")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<ProcedureAuditEvent>> item, final String componentId, final IModel<ProcedureAuditEvent> rowModel)
            {
                item.add(new Label(componentId, createLabelModel(rowModel)));

                if(rowModel.getObject().getDueDate().before(new Date())) {
                    item.add(new AttributeAppender("class", new Model<String>("overdue"), " "));
                } else if (rowModel.getObject().getDueDate().before(date7DaysFromNow)) {
                    item.add(new AttributeAppender("class", new Model<String>("sevenDays"), " "));
                }
            }
        });

        columns.add(new PropertyColumn<ProcedureAuditEvent>(new FIDLabelModel("label.building"),"procedureDefinition.building", "procedureDefinition.building")
        {
            @Override
            public void populateItem(final Item<ICellPopulator<ProcedureAuditEvent>> item, final String componentId, final IModel<ProcedureAuditEvent> rowModel)
            {
                item.add(new Label(componentId, createLabelModel(rowModel))).add(new AttributeAppender("class", new Model<String>("building"), " "));

                if(rowModel.getObject().getDueDate().before(new Date())) {
                    item.add(new AttributeAppender("class", new Model<String>("overdue"), " "));
                } else if (rowModel.getObject().getDueDate().before(date7DaysFromNow)) {
                    item.add(new AttributeAppender("class", new Model<String>("sevenDays"), " "));
                }
            }
        });

        addCustomColumns(columns);
        addActionColumn(columns);
        return columns;
    }

    protected void addActionColumn(List<IColumn<? extends ProcedureAuditEvent>> columns) {}

    protected void addCustomColumns(List<IColumn<? extends ProcedureAuditEvent>> columns) {}

    protected FIDFeedbackPanel getErrorFeedbackPanel() { return null; }

    public FieldIDDataProvider<ProcedureAuditEvent> getDataProvider() {
        return dataProvider;
    }

}
