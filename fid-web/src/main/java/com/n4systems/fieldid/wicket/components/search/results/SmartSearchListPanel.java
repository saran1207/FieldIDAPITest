package com.n4systems.fieldid.wicket.components.search.results;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.List;

/**
 * Created by rrana on 2015-06-22.
 */
public class SmartSearchListPanel extends Panel {

    public static final int PROCEDURES_PER_PAGE = 200;

    private FieldIDDataProvider<Asset> dataProvider;


    public SmartSearchListPanel(String id, FieldIDDataProvider<Asset> dataProvider) {
        super(id);

        this.dataProvider = dataProvider;

        SimpleDefaultDataTable table;
        add(table = new SimpleDefaultDataTable<Asset>("assetTable", getProceduresTableColumns(), dataProvider, PROCEDURES_PER_PAGE));

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

    private List<IColumn<? extends Asset>> getProceduresTableColumns() {
        List<IColumn<? extends Asset>> columns = Lists.newArrayList();

        //Select the asset link
        columns.add(new SmartSearchSelectColumn(new FIDLabelModel(""),"", ""));

        //Asset Type
        columns.add(new PropertyColumn<Asset>(new FIDLabelModel("title.asset_type"),"type", "type.name"));

        //ID
        columns.add(new PropertyColumn<Asset>(new FIDLabelModel("label.id"),"identifier", "identifier"));

        //RFID Number
        columns.add(new PropertyColumn<Asset>(new FIDLabelModel("label.rfidnumber"),"rfidNumber", "rfidNumber"));

        //Reference Number
        columns.add(new PropertyColumn<Asset>(new FIDLabelModel("label.reference_number"),"customerRefNumber", "customerRefNumber"));

        //Owner
        columns.add(new PropertyColumn<Asset>(new FIDLabelModel("label.owner"), "owner", "owner.internalOrg.name"));

        //Asset Status
        columns.add(new PropertyColumn<Asset>(new FIDLabelModel("label.assetstatus"),"assetStatus", "assetStatus.name"));

        //Next Scheduled Date
        columns.add(new SmartSearchNextScheduledDateColumn(new FIDLabelModel("label.nextscheduleddate"),"", ""));

        addCustomColumns(columns);
        addActionColumn(columns);
        return columns;
    }

    protected void addActionColumn(List<IColumn<? extends Asset>> columns) {}

    protected void addCustomColumns(List<IColumn<? extends Asset>> columns) {}

    protected FIDFeedbackPanel getErrorFeedbackPanel() { return null; }

    public FieldIDDataProvider<Asset> getDataProvider() {
        return dataProvider;
    }

}
