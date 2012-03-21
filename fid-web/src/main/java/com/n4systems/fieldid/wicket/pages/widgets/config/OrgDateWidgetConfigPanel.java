package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.fieldid.wicket.utils.EnumDropDownChoiceRenderer;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.RangeType;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Arrays;

@SuppressWarnings("serial")
public abstract class OrgDateWidgetConfigPanel<T extends WidgetConfiguration> extends WidgetConfigPanel<T> {

    private IModel<T> configModel;
    protected OrgPicker orgPicker;
    protected DropDownChoice<RangeType> dateRangeSelect;
    
    public OrgDateWidgetConfigPanel(String id, final IModel<T> configModel) {
        super(id, configModel);
        this.configModel = configModel;       
        addConfigElement(orgPicker = createOrgPicker(configModel));        
		addConfigElement(dateRangeSelect = createDateRangeSelect()); 
    }

	private OrgPicker createOrgPicker(final IModel<T> configModel) {
        OrgPicker picker = new OrgPicker("picker", new PropertyModel<BaseOrg>(configModel, "org"));
        picker.setTranslateContainerSelector(".w_content");
        return picker;
	}

	protected DropDownChoice<RangeType> createDateRangeSelect() {
		DropDownChoice<RangeType> d = new DropDownChoice<RangeType>("dateRangeSelect", new PropertyModel<RangeType>(configModel,"rangeType"),
                Arrays.asList(RangeType.allFloatingButFutureTypes().toArray(new RangeType[]{})), new EnumDropDownChoiceRenderer());
		d.setNullValid(false);
		return d;
	}	

}
