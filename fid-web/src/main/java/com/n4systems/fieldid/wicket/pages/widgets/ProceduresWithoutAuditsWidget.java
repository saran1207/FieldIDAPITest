package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.components.loto.ProcedureWithoutAuditsAssetColumn;
import com.n4systems.fieldid.wicket.components.loto.ProcedureWithoutAuditsActionColumn;
import com.n4systems.fieldid.wicket.components.table.SimpleDataTable;
import com.n4systems.fieldid.wicket.data.ProceduresWithoutAuditsDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.widgets.config.ProceduresWithoutAuditsConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.ProceduresWithoutAuditsWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnclosureContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class ProceduresWithoutAuditsWidget extends Widget<ProceduresWithoutAuditsWidgetConfiguration>{

   public ProceduresWithoutAuditsWidget(String id, WidgetDefinition<ProceduresWithoutAuditsWidgetConfiguration> widgetDefinition) {
       super(id, new Model<WidgetDefinition<ProceduresWithoutAuditsWidgetConfiguration>>(widgetDefinition));

       List<IColumn<ProcedureDefinition>> columns = new ArrayList<IColumn<ProcedureDefinition>>();
       columns.add(new ProcedureWithoutAuditsAssetColumn(new FIDLabelModel("label.equipment_#"),"equipmentNumber"));
       columns.add(new PropertyColumn<ProcedureDefinition>(new FIDLabelModel("label.procedure_code"), "procedureCode"));
       columns.add(new PropertyColumn(new FIDLabelModel("label.location"), "equipmentLocation"));
       columns.add(new PropertyColumn(new FIDLabelModel("label.type"),"procedureType"));
       columns.add(new ProcedureWithoutAuditsActionColumn(new FIDLabelModel("label.create_audit"), "asset"));

       final ProceduresWithoutAuditsDataProvider dataProvider = new ProceduresWithoutAuditsDataProvider(getOrg());

       SimpleDataTable<ProcedureDefinition> dataTable = new SimpleDataTable<ProcedureDefinition>("proceduresWithoutAuditsTable", columns, dataProvider, 25) {
           @Override
           public boolean isVisible() {
               return dataProvider.size() > 0;
           }
       };
       dataTable.setDisplayPagination(false);
       dataTable.setCssClass("simpleTable decorated");

       EnclosureContainer dataContainer = new EnclosureContainer("proceduresWithoutAuditsEnclosure", dataTable);
       dataContainer.add(dataTable);
       add(dataContainer);

       dataContainer.add(new Label("totalProceduresWithoutAudits", dataProvider.size() + ""));

       add(new WebMarkupContainer("allProceduresHaveAuditsMessage") {
           @Override
           public boolean isVisible() {
               return dataProvider.size() == 0;
           }
       });
   }

    private BaseOrg getOrg() {
        ProceduresWithoutAuditsWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        return config.getOrg();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/dashboard/widgets/proceduresWithoutAudits.css");
    }

    @Override
    public Component createConfigPanel(String id) {
        IModel<ProceduresWithoutAuditsWidgetConfiguration> configModel = new Model<ProceduresWithoutAuditsWidgetConfiguration>(getWidgetDefinition().getObject().getConfig());
        return new ProceduresWithoutAuditsConfigPanel(id,configModel);
    }

    @Override
    protected IModel<String> getSubTitleModel() {
        if(getOrg() == null){
            return Model.of("");
        } else {
            return Model.of("Procedures without audits for " + getOrg().getDisplayName());
        }
    }

}
