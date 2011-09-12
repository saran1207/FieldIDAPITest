package com.n4systems.fieldid.wicket.components.eventform.details;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.feedback.ContainerFeedbackPanel;

public class NumberFieldDetailsPanel extends Panel {
	
	private TextField<Integer> decimalPlaces;
	private ContainerFeedbackPanel feedbackPanel;

	public NumberFieldDetailsPanel(String id, IModel<?> model) {
		super(id, model);
		add(feedbackPanel = new ContainerFeedbackPanel("feedbackPanel", this));
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.setOutputMarkupPlaceholderTag(true);

		add(decimalPlaces = new TextField<Integer>("decimalPlacesInputField", new PropertyModel<Integer>(model, "decimalPlaces")));
		decimalPlaces.add(new UpdateComponentOnChange() {
			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e) {
				feedbackPanel.setVisible(true);
                target.addComponent(feedbackPanel);
			}
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				super.onUpdate(target);
				if(feedbackPanel.isVisible()) {
					feedbackPanel.setVisible(false);
					target.addComponent(feedbackPanel);
				}
			}
		});
		decimalPlaces.add(new RangeValidator<Integer>(0, 10));
	}


}
