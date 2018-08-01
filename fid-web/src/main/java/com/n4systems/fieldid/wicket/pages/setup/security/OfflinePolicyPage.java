package com.n4systems.fieldid.wicket.pages.setup.security;

import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.setup.SecurityPage;
import com.n4systems.model.security.OfflinePolicy;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.apache.wicket.validation.validator.RangeValidator;

public class OfflinePolicyPage extends AbstractSecurityPage {

	@SpringBean
	private TenantSettingsService tenantSettingsService;

    private FIDFeedbackPanel feedbackPanel;

	public OfflinePolicyPage(PageParameters params) {
        super(params);
        setOutputMarkupId(true);
        add(new OfflinePolicyForm("offlinePolicyForm"));
    }

    class OfflinePolicyForm extends Form<OfflinePolicy> {
		private static final long serialVersionUID = 3819792960628089473L;
		    	
        public OfflinePolicyForm(String id) {
            super(id);
            OfflinePolicy offlinePolicy = tenantSettingsService.getTenantSettings().getOfflinePolicy();
            setDefaultModel(new CompoundPropertyModel<OfflinePolicy>(offlinePolicy));

            add(feedbackPanel=new FIDFeedbackPanel("feedbackPanel"));

            add(addIntegerRangeTextField("maxOfflineDays",1,60));
            
            add(new AjaxSubmitLink("saveButton") {
				private static final long serialVersionUID = 1L;
				@Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                    tenantSettingsService.updateTenantOfflinePolicySettings((OfflinePolicy) form.getModelObject());
                	setResponsePage(SecurityPage.class);
                }
				@Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedbackPanel);
				}
            });
            
            add(new AjaxLink<String>("cancelLink") {
				private static final long serialVersionUID = 1L;
                @Override
                public void onClick(AjaxRequestTarget target) {
                	setResponsePage(SecurityPage.class);
                }
            });
            
        }
		
		private TextField<Integer> addIntegerRangeTextField(String id, Integer min, Integer max) {
			TextField<Integer> textField = new TextField<Integer>(id, Integer.class);
			if (max==null) { 
				textField.add(new MinimumValidator<Integer>(min));
			} else { 
				textField.add(new RangeValidator<Integer>(min,max));
			}
			textField.setRequired(true);
			return textField;
		}

    }

	@Override
	protected Label createTitleLabel(String labelId) {
		return new Label(labelId, new FIDLabelModel("title.offline_policy_page"));
	}

}
