package com.n4systems.fieldid.wicket.pages.setup;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;

public class AccountPolicyPage extends SetupPage {
	private FIDFeedbackPanel feedback;

    public AccountPolicyPage(PageParameters params) {
        super(params);
        add(new AccountPolicyForm("accountPolicyForm"));
    }

    class AccountPolicyForm extends Form<AccountPolicyForm> {
		private static final long serialVersionUID = 3819792960628089473L;
		
        public AccountPolicyForm(String id) {
            super(id);
            setDefaultModel(new CompoundPropertyModel<AccountPolicyForm>(new AccountPolicy()));

            add(feedback = new FIDFeedbackPanel("feedbackPanel"));

            add(addIntegerRangeTextField("maxAttempts",1,10));
            add(addTextField("lockoutDuration",0));
            
            add(new AjaxButton("saveButton") {
				private static final long serialVersionUID = 1L;
				@Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    target.addComponent(this);
                }
				@Override protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.addComponent(feedback);
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

		private TextField<Integer> addTextField(String id, int min) {
			return addIntegerRangeTextField(id, min, Integer.MAX_VALUE);
		}
		
		private TextField<Integer> addIntegerRangeTextField(String id, int min, int max) {
			TextField<Integer> textField = new TextField<Integer>(id, Integer.class);
			textField.add(new RangeValidator<Integer>(min,max));
			textField.setRequired(true);
			return textField;
		}

    }

}
