package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by agrabovskis on 2018-02-09.
 */
abstract public class CustomerDivisionsEditPanel extends Panel {

    private static final Logger logger = Logger.getLogger(CustomerDivisionsEditPanel.class);

    @SpringBean
    private OrgService orgService;

    private CompoundPropertyModel currentDivision;
    private IModel<Long> customerSelectedForEditModel;

    public CustomerDivisionsEditPanel(String id, CompoundPropertyModel currentDivision, IModel<Long> customerSelectedForEditModel) {
        super(id);
        this.currentDivision = currentDivision;
        this.customerSelectedForEditModel = customerSelectedForEditModel;
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/user.css");
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        if (currentDivision.getObject() == null) {
            DivisionOrg division = new DivisionOrg();
            division.setParent((CustomerOrg) orgService.findById(customerSelectedForEditModel.getObject()));
        }
    }

    private void addComponents() {

        final Form form = new Form("form") ;
        add(form);

        form.add(new TextField<String>("divisionID", currentDivision.bind("code")).setRequired(true));
        form.add(new TextField<String>("name", currentDivision.bind("name")).setRequired(true));
        form.add(new TextField<String>("divisionNotes", currentDivision.bind("notes")));
        form.add(new TextField<String>("contact.name", currentDivision.bind("contact.name")));
        form.add(new TextField<String>("contact.email", currentDivision.bind("contact.email")));
        form.add(new TextField<String>("addressInfo.streetAddress", currentDivision.bind("addressInfo.streetAddress")));
        form.add(new TextField<String>("addressInfo.city", currentDivision.bind("addressInfo.city")));
        form.add(new TextField<String>("addressInfo.state", currentDivision.bind("addressInfo.state")));
        form.add(new TextField<String>("addressInfo.zip", currentDivision.bind("addressInfo.zip")));
        form.add(new TextField<String>("addressInfo.country", currentDivision.bind("addressInfo.country")));
        form.add(new TextField<String>("addressInfo.phone1", currentDivision.bind("addressInfo.phone1")));
        form.add(new TextField<String>("addressInfo.phone2", currentDivision.bind("addressInfo.phone2")));
        form.add(new TextField<String>("addressInfo.fax1", currentDivision.bind("addressInfo.fax1")));

        form.add(new AjaxSubmitLink("submit", form) {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                System.out.println("form submitted");
                System.out.println("... object is " + currentDivision.getObject());
                target.addChildren(getPage(), FeedbackPanel.class);
                try {
                    orgService.saveOrUpdate((DivisionOrg) currentDivision.getObject());
                    CustomerOrg customer = (CustomerOrg) orgService.findById(customerSelectedForEditModel.getObject());
                    customer.touch();
                    orgService.update(customer);
                    info(getString("message.division_saved"));
                    postSaveAction(target);
                } catch (Exception e) {
                    logger.error("Failed updating division", e);
                    error(getString("error.saving_divsion"));
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
        });

        form.add(new AjaxSubmitLink("cancel") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                System.out.println("Cancel link clicked");
                postCancelAction(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }

        });
    }

    abstract void postCancelAction(AjaxRequestTarget target);
    abstract void postSaveAction(AjaxRequestTarget target);
}
