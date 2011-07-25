package com.n4systems.fieldid.wicket.pages.setup;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;

import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.model.security.PasswordPolicy;

public class PasswordPolicyPage extends SetupPage {

	@SpringBean 
	private TenantSettingsService tenantSettingsService;
	
    private FIDFeedbackPanel feedbackPanel;
    
	public PasswordPolicyPage(PageParameters params) {
        super(params);
        setOutputMarkupId(true);
        add(new PasswordPolicyForm("passwordForm"));
    }

    class PasswordPolicyForm extends Form<PasswordPolicyForm> {
		private static final long serialVersionUID = 3819792960628089473L;
		    	
        public PasswordPolicyForm(String id) {
            super(id);
            PasswordPolicy passwordPolicy = tenantSettingsService.getTenantSettings().getPasswordPolicy();           
            setDefaultModel(new CompoundPropertyModel<PasswordPolicyForm>(passwordPolicy));

            add(feedbackPanel=new FIDFeedbackPanel("feedbackPanel"));

            add(addIntegerRangeTextField("minLength",0,100));
            add(addIntegerRangeTextField("minNumbers",0,100));
            add(addIntegerRangeTextField("minSymbols",0,100));
            add(addIntegerRangeTextField("minCapitals",0,100));
            add(addIntegerRangeTextField("expiryDays",0,Integer.MAX_VALUE));
            add(addIntegerRangeTextField("uniqueness",0,100));
            
            add(new AjaxButton("saveButton") {
				private static final long serialVersionUID = 1L;
				@Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    target.addComponent(feedbackPanel);
                    tenantSettingsService.updateTenantPasswordPolicySettings((PasswordPolicy) form.getModelObject());
                    // TODO DD : add update msg here...                    
                }
				@Override protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.addComponent(feedbackPanel);
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

		private TextField<Integer> addIntegerRangeTextField(String id, int min, int max) {
			TextField<Integer> textField = new TextField<Integer>(id, Integer.class);
			textField.add(new RangeValidator<Integer>(min,max));
			textField.setRequired(true);
			return textField;
		}

    }
    
}
