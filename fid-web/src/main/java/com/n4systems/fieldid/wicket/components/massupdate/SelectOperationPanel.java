package com.n4systems.fieldid.wicket.components.massupdate;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.search.SearchCriteria;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class SelectOperationPanel extends AbstractMassUpdatePanel {

	private MassUpdateOperation selected;
    private Component submit;

    public SelectOperationPanel(String id, final SearchCriteria searchCriteria, List<MassUpdateOperation> operationList, String updateType) {
		super(id);
		
		RadioChoice<MassUpdateOperation> optType = new RadioChoice<MassUpdateOperation>("operations", new PropertyModel<MassUpdateOperation>(this, "selected"),
                operationList, new MassUpdateOperationChoiceRenderer(updateType));
		
		Form<Void> massUpdateForm = new Form<Void>("form"){
			@Override
			protected void onSubmit() {
				onOperationSelected(selected);
			}
		};
		
		add(massUpdateForm);
        massUpdateForm.add(submit = new Button("submit").setEnabled(false).setOutputMarkupId(true));
		massUpdateForm.add(optType);
		massUpdateForm.add(new Label("selectOperationMessage", new FIDLabelModel("message.mass_update_select_operation",
                searchCriteria.getSelection().getNumSelectedIds(), updateType)));
		massUpdateForm.add(new Link("cancelLink") {
			@Override
			public void onClick() {
                onCancel();
			}
		});
        optType.add(new AjaxFormComponentUpdatingBehavior("onclick") {
            @Override protected void onUpdate(AjaxRequestTarget target) {
                submit.setEnabled(true);
                target.add(submit);
            }
        });
	}

    protected void onOperationSelected(MassUpdateOperation operation) {}

    protected void onCancel() {};

}
