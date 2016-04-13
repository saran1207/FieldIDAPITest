package com.n4systems.fieldid.wicket.components.eventform.details.number;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.feedback.ContainerFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.NumberFieldCriteria;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

public class NumberFieldDetailsPanel extends Panel {
	
	private TextField<Integer> decimalPlaces;
	private ContainerFeedbackPanel feedbackPanel;
	private AjaxLink addEditLogicLink;

	public NumberFieldDetailsPanel(String id, IModel<NumberFieldCriteria> model) {
		super(id, model);
		add(feedbackPanel = new ContainerFeedbackPanel("feedbackPanel", this));
		feedbackPanel.setOutputMarkupId(true);
		feedbackPanel.setOutputMarkupPlaceholderTag(true);

		add(decimalPlaces = new RequiredTextField<>("decimalPlacesInputField", new PropertyModel<>(model, "decimalPlaces")));
		decimalPlaces.add(new UpdateComponentOnChange() {
			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e) {
				feedbackPanel.setVisible(true);
                target.add(feedbackPanel);
			}
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				super.onUpdate(target);
				if(feedbackPanel.isVisible()) {
					feedbackPanel.setVisible(false);
					target.add(feedbackPanel);
				}
			}
		});
		decimalPlaces.add(new RangeValidator<>(0, 10));

		add(addEditLogicLink = new AjaxLink<Void>("addEditLogicLink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				onConfigureCriteriaLogic(target);
			}
		});

		addEditLogicLink.add(getAddEditLinkLabel());

	}

	private Label getAddEditLinkLabel() {
		if (hasRule()) {
			return new Label("addEditLabel", new FIDLabelModel("label.edit_logic"));
		} else {
			return new Label("addEditLabel", new FIDLabelModel("label.add_logic"));
		}

	}

	public void updateAddEditLinkLabel() {
		addEditLogicLink.replace(getAddEditLinkLabel());
	}

	public void onConfigureCriteriaLogic(AjaxRequestTarget target) {}

	public boolean hasRule() {
		return false;
	}
}
