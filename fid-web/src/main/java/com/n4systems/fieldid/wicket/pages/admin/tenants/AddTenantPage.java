package com.n4systems.fieldid.wicket.pages.admin.tenants;

import com.n4systems.fieldid.wicket.components.addressinfo.AddressInfoInputPanel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.timezone.TimeZoneSelectorPanel;
import com.n4systems.fieldid.wicket.model.ArrayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.admin.tenants.AddTenantModel;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.services.tenant.TenantCreationService;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import org.apache.log4j.Logger;
import org.apache.wicket.extensions.model.AbstractCheckBoxModel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator.LengthBetweenValidator;

import java.util.Set;

public class AddTenantPage extends FieldIDAdminPage {
	private static final Logger logger = Logger.getLogger(AddTenantPage.class);
	
	@SpringBean
	private TenantCreationService tenantCreateService;
	
	public AddTenantPage() {
		add(new FIDFeedbackPanel("feedbackPanel"));
		add(new AddTenantForm("addTenantForm", new Model<AddTenantModel>(getDefaultTenantModel())));
	}
	
	private AddTenantModel getDefaultTenantModel() {
		AddTenantModel model = new AddTenantModel();
		model.getAdminUser().setTimeZoneID(ConfigContext.getCurrentContext().getString(ConfigEntry.DEFAULT_TIMEZONE_ID));
		
		return model;
	}

	private class AddTenantForm extends Form<AddTenantModel> {
        WebMarkupContainer extendedFeaturesContainer;

		public AddTenantForm(String id, final IModel<AddTenantModel> addTenantModel) {
			super(id, new CompoundPropertyModel<AddTenantModel>(addTenantModel));
            getMarkupId();

			add(new RequiredTextField<String>("tenant.name"));
			add(new RequiredTextField<Integer>("tenant.settings.userLimits.maxEmployeeUsers", Integer.class));
			add(new RequiredTextField<Integer>("tenant.settings.userLimits.maxLiteUsers", Integer.class));
			add(new RequiredTextField<Integer>("tenant.settings.userLimits.maxReadOnlyUsers", Integer.class));

            add(extendedFeaturesContainer = new WebMarkupContainer("extendedFeaturesContainer"));
            extendedFeaturesContainer.setOutputMarkupId(true);
			extendedFeaturesContainer.add(new ListView<ExtendedFeature>("extendedFeatures", new ArrayModel<ExtendedFeature>(ExtendedFeature.values())) {
				@Override
				protected void populateItem(ListItem<ExtendedFeature> item) {
					ExtendedFeature feature = item.getModelObject();
					item.add(new Label("featureName", new FIDLabelModel(feature.getLabel())));
					item.add(new CheckBox("feature", new ExtendedFeatureCheckBoxModel(feature, addTenantModel.getObject().getPrimaryOrg().getExtendedFeatures())));
				}
			});
            extendedFeaturesContainer.add(new CheckBox("tenant.settings.secondaryOrgsEnabled"));
            extendedFeaturesContainer.add(new CheckBox("primaryOrg.plansAndPricingAvailable"));

			add(new RequiredTextField<String>("adminUser.userID").add(new LengthBetweenValidator(1,255)));
			add(new RequiredTextField<String>("adminUser.firstName"));
			add(new RequiredTextField<String>("adminUser.lastName"));
			add(new RequiredTextField<String>("adminUser.emailAddress"));
			add(new TimeZoneSelectorPanel("timeZoneContainer", new PropertyModel<String>(getModel(), "adminUser.timeZoneID")));
			add(new RequiredTextField<String>("primaryOrg.name"));
			add(new AddressInfoInputPanel("addressInfoContainer", new PropertyModel<AddressInfo>(getModel(), "primaryOrg.addressInfo")));
			add(new TextArea<String>("primaryOrg.notes"));
		}
		
		@Override
        protected void onSubmit() {
			AddTenantModel addTenantModel = getModelObject();
			try {
				tenantCreateService.createTenant(addTenantModel.getTenant(), addTenantModel.getPrimaryOrg(), addTenantModel.getAdminUser());
                redirect("/admin/organizations.action");
			} catch (Exception e) {
				logger.error("Failed creating Tenant [" + addTenantModel.getTenant().getName() + "]", e);
				error("Failed creating Tenant: " + e.getMessage());
			}
        }

		@Override
		protected void onDetach() {
			super.onDetach();
			AddTenantModel addTenantModel = getModelObject();
			addTenantModel.getTenant().reset();
			addTenantModel.getTenant().getSettings().reset();
			addTenantModel.getPrimaryOrg().reset();
			addTenantModel.getPrimaryOrg().getAddressInfo().reset();
			addTenantModel.getAdminUser().reset();
		}
	}


    private class ExtendedFeatureCheckBoxModel extends AbstractCheckBoxModel {
		private final ExtendedFeature feature;
		private final Set<ExtendedFeature> selectedFeatures;
		
		public ExtendedFeatureCheckBoxModel(ExtendedFeature feature, Set<ExtendedFeature> selectedFeatures) {
			this.feature = feature;
			this.selectedFeatures = selectedFeatures;
		}
		
		@Override
		public boolean isSelected() {
			return selectedFeatures.contains(feature);
		}

		@Override
		public void select() {
			selectedFeatures.add(feature);
		}

		@Override
		public void unselect() {
			selectedFeatures.remove(feature);
		}
	}
}
