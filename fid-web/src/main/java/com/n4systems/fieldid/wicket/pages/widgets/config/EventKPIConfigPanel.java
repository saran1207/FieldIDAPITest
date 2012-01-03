package com.n4systems.fieldid.wicket.pages.widgets.config;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.model.dashboard.widget.EventKPIWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.ChartDateRange;

public class EventKPIConfigPanel extends WidgetConfigPanel<EventKPIWidgetConfiguration> {

    private IModel<EventKPIWidgetConfiguration> configModel;
    private WebMarkupContainer orgsListContainer;
    private BaseOrg orgToAdd;

    private OrgPicker orgPicker;
    private AjaxButton addOrgButton;
	private DropDownChoice<ChartDateRange> dateRange;

    public EventKPIConfigPanel(String id, final IModel<EventKPIWidgetConfiguration> configModel) {
        super(id, configModel);
        this.configModel = configModel;

        orgsListContainer = new WebMarkupContainer("orgsListContainer");
        orgsListContainer.setOutputMarkupId(true);

        final PropertyModel<List<BaseOrg>> orgsListModel = new PropertyModel<List<BaseOrg>>(configModel, "orgs");
        orgsListContainer.add(new ListView<BaseOrg>("orgs", orgsListModel) {
            @Override
            protected void populateItem(final ListItem<BaseOrg> item) {
                ContextImage deleteImage;
                item.add(deleteImage = new ContextImage("deleteImage", "images/small-x.png"));
                deleteImage.add(createDeleteBehavior(item, orgsListModel));
                item.add(new Label("orgNameLabel", new PropertyModel<String>(item.getModel(), "displayName")));
            }
        });

        addConfigElement(orgPicker = new OrgPicker("picker", new PropertyModel<BaseOrg>(this, "orgToAdd")) {
            @Override
            protected void onPickerClosed(AjaxRequestTarget target) {
                target.add(addOrgButton);
            }
        });
        orgPicker.setTranslateContainerSelector(".w_content");

        addConfigElement(orgsListContainer);

        addConfigElement(addOrgButton = new AjaxButton("addOrgButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                configModel.getObject().getOrgs().add(orgToAdd);
                orgToAdd = null;
                target.add(orgsListContainer);
                target.add(orgPicker);
                target.add(this);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }

            @Override
            public boolean isEnabled() {
                return orgToAdd != null;
            }
        });
        addOrgButton.setOutputMarkupId(true);
        
        addConfigElement(dateRange = createDateRangeSelect());
        
    }

	protected DropDownChoice<ChartDateRange> createDateRangeSelect() {
        IChoiceRenderer<ChartDateRange> renderer = new IChoiceRenderer<ChartDateRange>() {       
			@Override public Object getDisplayValue(ChartDateRange object) {
				return object.getDisplayName();
			}
			@Override public String getIdValue(ChartDateRange object, int index) {	
				return object.name();
			}
		};		
		
		DropDownChoice<ChartDateRange> d = new DropDownChoice<ChartDateRange>("dateRangeSelect", new PropertyModel<ChartDateRange>(configModel,"dateRange"), Arrays.asList(ChartDateRange.chartDateRanges()), renderer);		
		d.setNullValid(false);
		return d;
	}	        
    
    private AjaxEventBehavior createDeleteBehavior(final ListItem<BaseOrg> item, final PropertyModel<List<BaseOrg>> orgsListModel) {
        return new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                orgsListModel.getObject().remove(item.getIndex());
                target.add(orgsListContainer);
            }
        };
    }

}

