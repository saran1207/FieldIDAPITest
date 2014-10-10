package com.n4systems.fieldid.wicket.pages.setup;

import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.security.PasswordPolicy;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.apache.wicket.validation.validator.RangeValidator;

public class PasswordPolicyPage extends SetupPage {

	@SpringBean 
	private TenantSettingsService tenantSettingsService;
	
    private FIDFeedbackPanel feedbackPanel;
    public CheckBox checkName;
    
	public PasswordPolicyPage(PageParameters params) {
        super(params);
        setOutputMarkupId(true);
        add(new PasswordPolicyForm("passwordForm"));
    }

    class PasswordPolicyForm extends Form<PasswordPolicy> {
		private static final long serialVersionUID = 3819792960628089473L;
		    	
        public PasswordPolicyForm(String id) {
            super(id);
            PasswordPolicy passwordPolicy = tenantSettingsService.getTenantSettings().getPasswordPolicy();           
            setDefaultModel(new CompoundPropertyModel<PasswordPolicy>(passwordPolicy));

            add(feedbackPanel=new FIDFeedbackPanel("feedbackPanel"));

            add(addIntegerRangeTextField("minLength",6,100));
            add(addIntegerRangeTextField("minNumbers",0,100));
            add(addIntegerRangeTextField("minSymbols",0,100));
            add(addIntegerRangeTextField("minCapitals",0,100));
            add(addIntegerRangeTextField("expiryDays",0));
            add(addIntegerRangeTextField("uniqueness",0));

            checkName = new CheckBox("checkName", Model.of(getModel().getObject().isCheckName()));
            add(checkName);
            
            add(new AjaxButton("saveButton") {
				private static final long serialVersionUID = 1L;
				@Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                    ((PasswordPolicy) form.getModelObject()).setCheckName(Boolean.valueOf(checkName.getValue()));
                    tenantSettingsService.updateTenantPasswordPolicySettings((PasswordPolicy) form.getModelObject());
                	setResponsePage(SecurityPage.class);                    
                }
				@Override protected void onError(AjaxRequestTarget target, Form<?> form) {
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
		private TextField<Integer> addIntegerRangeTextField(String id, Integer min) {
			return addIntegerRangeTextField(id, min, null);
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
		return new Label(labelId, new FIDLabelModel("title.password_policy_page"));
	}
    
}
