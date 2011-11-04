package com.n4systems.fieldid.wicket.model.dashboard;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.dashboard.DashboardColumn;
import com.n4systems.model.dashboard.DashboardLayout;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.WidgetType;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnusedWidgetsModel extends FieldIDSpringModel<List<WidgetType>> {

    private IModel<DashboardLayout> currentLayoutModel;

    public UnusedWidgetsModel(IModel<DashboardLayout> currentLayoutModel) {
        this.currentLayoutModel = currentLayoutModel;
    }

    @Override
    protected List<WidgetType> load() {
        List<WidgetType> types = getAvailableWidgetTypes();

        DashboardLayout layout = currentLayoutModel.getObject();

        for (DashboardColumn column : layout.getColumns()) {
            for (WidgetDefinition widget : column.getWidgets()) {
                types.remove(widget.getWidgetType());
            }
        }

        return types;
    }

    public List<WidgetType> getAvailableWidgetTypes() {
        List<WidgetType> types = new ArrayList<WidgetType>(Arrays.asList(WidgetType.values()));

        if (!FieldIDSession.get().getSessionUser().getOwner().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.Projects)) {
            types.remove(WidgetType.JOBS_ASSIGNED);
        }

        return types;
    }

}
