package com.n4systems.fieldid.wicket.pages.setup.security;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.setup.SecurityPage;
import com.n4systems.fieldid.wicket.pages.setup.SetupPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.MinimumValidator;
import org.apache.wicket.validation.validator.RangeValidator;

import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.model.security.AccountPolicy;

public class AccountPolicyPage extends AbstractSecurityPage {
	private FIDFeedbackPanel feedback;
	
	@SpringBean
	private TenantSettingsService tenantSettingsService;

    public AccountPolicyPage(PageParameters params) {
        super(params);
        add(new AccountPolicyForm("accountPolicyForm"));
    }

    class AccountPolicyForm extends Form<AccountPolicy> {
		private static final long serialVersionUID = 3819792960628089473L;
		
        public AccountPolicyForm(String id) {
            super(id);
            setOutputMarkupId(true);
            AccountPolicy accountPolicy = tenantSettingsService.getTenantSettings().getAccountPolicy();
            setDefaultModel(new CompoundPropertyModel<AccountPolicy>(accountPolicy));

            add(feedback = new FIDFeedbackPanel("feedbackPanel"));

            add(addIntegerRangeTextField("maxAttempts",1,10));
            add(addTextField("lockoutDuration",0));
			add(new CheckBox("lockoutOnMobile"));
            
            add(new AjaxButton("saveButton") {
				private static final long serialVersionUID = 1L;
				@Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					udpateAccountPolicy((AccountPolicy) form.getModelObject());
                    target.add(AccountPolicyForm.this);
                	setResponsePage(SecurityPage.class);
                }
				@Override protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedback);
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

		protected void udpateAccountPolicy(AccountPolicy accountPolicy) {			
			tenantSettingsService.updateTenantAccountPolicySettings(accountPolicy);
		}

		private TextField<Integer> addTextField(String id, int min) {
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
		return new Label(labelId, new FIDLabelModel("title.account_policy_page"));
	}

}
