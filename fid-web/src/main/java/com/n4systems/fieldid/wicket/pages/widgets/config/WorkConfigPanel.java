package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.fieldid.wicket.components.AutoCompleteOrgPicker;
import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.model.dashboard.widget.WorkWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.RangeType;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class WorkConfigPanel extends WidgetConfigPanel<WorkWidgetConfiguration> {

    private IModel<WorkWidgetConfiguration> configModel;
    private WebMarkupContainer orgsListContainer;
    private BaseOrg orgToAdd;

    private OrgPicker orgPicker;
    private AjaxButton addOrgButton;
	private DropDownChoice<RangeType> dateRange;

    public WorkConfigPanel(String id, final IModel<WorkWidgetConfiguration> configModel) {
        super(id, configModel);
        this.configModel = configModel;

        addConfigElement(new AutoCompleteOrgPicker("org", new PropertyModel<BaseOrg>(configModel,"org")));

    }

}

