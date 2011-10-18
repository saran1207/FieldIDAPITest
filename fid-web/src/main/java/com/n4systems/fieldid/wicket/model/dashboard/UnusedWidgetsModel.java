package com.n4systems.fieldid.wicket.model.dashboard;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
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
        List<WidgetType> types = new ArrayList<WidgetType>(Arrays.asList(WidgetType.values()));

        DashboardLayout layout = currentLayoutModel.getObject();

        for (DashboardColumn column : layout.getColumns()) {
            for (WidgetDefinition widget : column.getWidgets()) {
                types.remove(widget.getWidgetType());
            }
        }

        return types;
    }

}
