package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.components.loto.ProcedureLockoutAssetColumn;
import com.n4systems.fieldid.wicket.components.loto.ProcedureLockoutDateColumn;
import com.n4systems.fieldid.wicket.components.table.SimpleDataTable;
import com.n4systems.fieldid.wicket.data.LockedoutProceduresForUserDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.widgets.config.AssetsLockedOutConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.LockedoutProceduresWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.procedure.Procedure;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.EnclosureContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rrana on 2014-05-19.
 */
public class AssetsLockedOutWidget extends Widget<LockedoutProceduresWidgetConfiguration> {

    @SpringBean
    private OrgDateRangeSubtitleHelper orgDateRangeSubtitleHelper;

    public AssetsLockedOutWidget(String id, WidgetDefinition<LockedoutProceduresWidgetConfiguration> widgetDefinition) {
        super(id, new Model<WidgetDefinition<LockedoutProceduresWidgetConfiguration>>(widgetDefinition));

        List<IColumn<Procedure>> columns = new ArrayList<IColumn<Procedure>>();
        columns.add(new ProcedureLockoutAssetColumn(new FIDLabelModel("label.equipment_#"),"type.equipmentNumber"));
        columns.add(new PropertyColumn<Procedure>(new FIDLabelModel("label.procedure_code"), "type.procedureCode"));
        columns.add(new ProcedureLockoutDateColumn(new FIDLabelModel("label.lockout_date"), "lockDate"));

        final LockedoutProceduresForUserDataProvider lockedoutProceduresForUserDataProvider = new LockedoutProceduresForUserDataProvider(getOrg());


        SimpleDataTable<Procedure> jobsTable = new SimpleDataTable<Procedure>("jobsTable", columns, lockedoutProceduresForUserDataProvider, 10) {
            @Override
            public boolean isVisible() {
                return lockedoutProceduresForUserDataProvider.size() > 0;
            }
        };
        jobsTable.setDisplayPagination(false);
        jobsTable.setCssClass("simpleTable decorated");

        EnclosureContainer jobsContainer = new EnclosureContainer("jobsAssignedEnclosure", jobsTable);
        jobsContainer.add(jobsTable);
        add(jobsContainer);

        jobsContainer.add(new Label("totalJobs", lockedoutProceduresForUserDataProvider.size() + ""));

        add(new WebMarkupContainer("noJobsAssignedMessage") {
            @Override
            public boolean isVisible() {
                return lockedoutProceduresForUserDataProvider.size() == 0;
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/dashboard/widgets/jobsassigned.css");
    }

    private BaseOrg getOrg() {
        LockedoutProceduresWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        return config.getOrg();
    }

    @Override
    public Component createConfigPanel(String id) {
        IModel<LockedoutProceduresWidgetConfiguration> configModel = new Model<LockedoutProceduresWidgetConfiguration>(getWidgetDefinition().getObject().getConfig());
        return new AssetsLockedOutConfigPanel(id, configModel);
    }

    @Override
    protected IModel<String> getSubTitleModel() {
        if(getOrg() == null){
            return Model.of("");
        } else {
            return Model.of("Currently locked out for " + getOrg().getDisplayName());
        }
    }

}
