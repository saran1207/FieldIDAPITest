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
import com.n4systems.fieldid.wicket.components.table.SimpleDataTable;
import com.n4systems.fieldid.wicket.data.PopulatorLogBeanDataProvider;

public class AccountPolicyPage extends SetupPage {

    PopulatorLogBeanDataProvider provider;
    SimpleDataTable resultsTable;


    public AccountPolicyPage(PageParameters params) {
        super(params);

        add(new AccountPolicyForm("accountPolicyForm"));
    }

    class AccountPolicyForm extends Form<AccountPolicyForm> {
		private static final long serialVersionUID = 3819792960628089473L;
		
		private String maxAttempts;
		private String lockoutDuration;
    	
        public AccountPolicyForm(String id) {
            super(id);
            setDefaultModel(new CompoundPropertyModel<AccountPolicyForm>(this));

            add(new FIDFeedbackPanel("feedbackPanel"));

            add(addTextField("maxAttempts",1,10));
            add(addTextField("lockoutDuration",0));
            
            add(new AjaxButton("runButton") {
				private static final long serialVersionUID = 1L;
				@Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    target.addComponent(this);
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
			return addTextField(id, min, Integer.MAX_VALUE);
		}
		
		private TextField<Integer> addTextField(String id, int min, int max) {
			TextField<Integer> textField = new TextField<Integer>(id, Integer.class);
			textField.add(new RangeValidator<Integer>(min,max));
			return textField;
		}

    }

}
