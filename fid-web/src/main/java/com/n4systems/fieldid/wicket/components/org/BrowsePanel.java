package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.orgs.CustomersUnderInternalOrgModel;
import com.n4systems.fieldid.wicket.model.orgs.DivisionsUnderCustomerOrgModel;
import com.n4systems.fieldid.wicket.model.orgs.InternalOrgsModel;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class BrowsePanel extends Panel {

    private BaseOrg selectedInternalOrg;
    private BaseOrg selectedCustomerOrg;
    private BaseOrg selectedDivisionOrg;

    private IModel<BaseOrg> orgModel;

    public BrowsePanel(String id, IModel<BaseOrg> orgModel) {
        super(id);
        this.orgModel = orgModel;
        setOutputMarkupPlaceholderTag(true);

        add(new OrgForm("orgForm"));
    }

    class OrgForm extends Form {

        CustomersUnderInternalOrgModel customersUnderInternalOrgModel;
        DivisionsUnderCustomerOrgModel divisionsUnderCustomerOrgModel ;

        public OrgForm(String id) {
            super(id);
            setOutputMarkupId(true);
            selectedInternalOrg = FieldIDSession.get().getPrimaryOrg();

            add(new AjaxButton("selectButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    if (selectedDivisionOrg != null) {
                        orgModel.setObject(selectedDivisionOrg);
                    } else if (selectedCustomerOrg != null) {
                        orgModel.setObject(selectedCustomerOrg);
                    } else {
                        orgModel.setObject(selectedInternalOrg);
                    }
                    onOrgSelected(target);
                }
            });
            PropertyModel<BaseOrg> selectedInternalOrgModel = new PropertyModel<BaseOrg>(BrowsePanel.this, "selectedInternalOrg");
            PropertyModel<BaseOrg> selectedCustomerOrgModel = new PropertyModel<BaseOrg>(BrowsePanel.this, "selectedCustomerOrg");

            customersUnderInternalOrgModel = new CustomersUnderInternalOrgModel(selectedInternalOrgModel);
            divisionsUnderCustomerOrgModel = new DivisionsUnderCustomerOrgModel(selectedCustomerOrgModel);

            add(new DropDownChoice<BaseOrg>("orgSelect", selectedInternalOrgModel, new InternalOrgsModel(), new ListableChoiceRenderer<BaseOrg>()).add(createUpdateSelectsOnChangeBehavior(true, true)));
            add(new DropDownChoice<BaseOrg>("customerSelect", selectedCustomerOrgModel, customersUnderInternalOrgModel, new ListableChoiceRenderer<BaseOrg>()).setNullValid(true).add(createUpdateSelectsOnChangeBehavior(false, true)));
            add(new DropDownChoice<BaseOrg>("divisionSelect", new PropertyModel<BaseOrg>(BrowsePanel.this, "selectedDivisionOrg"), divisionsUnderCustomerOrgModel).setNullValid(true).add(createUpdateSelectsOnChangeBehavior(false, false)));
            add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    onCancelClicked(target);
                }
            });
        }

        protected AbstractBehavior createUpdateSelectsOnChangeBehavior(final boolean clearCustomerOnChange, final boolean clearDivisionOnChange) {
            return new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    if (clearCustomerOnChange) {
                        selectedCustomerOrg = null;
                    }
                    if (clearDivisionOnChange) {
                        selectedDivisionOrg = null;
                    }
                    target.addComponent(BrowsePanel.this);
                }
            };
        }

    }

    protected void onOrgSelected(AjaxRequestTarget target) { }

    protected void onCancelClicked(AjaxRequestTarget target) { }

    public void clearSelections() {
        selectedCustomerOrg = selectedDivisionOrg = null;
        selectedInternalOrg = FieldIDSession.get().getPrimaryOrg();
    }

}
