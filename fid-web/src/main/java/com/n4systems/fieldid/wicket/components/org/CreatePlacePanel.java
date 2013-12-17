package com.n4systems.fieldid.wicket.components.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.addressinfo.AddressPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.builders.CustomerOrgBuilder;
import com.n4systems.model.builders.DivisionOrgBuilder;
import com.n4systems.model.builders.SecondaryOrgBuilder;
import com.n4systems.model.orgs.*;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;

public class CreatePlacePanel extends Panel {

    enum Level {SECONDARY,CUSTOMER};

    private Component form;
    private CompoundPropertyModel<PlaceData> newPlaceModel = new CompoundPropertyModel(new PlaceData());

    public CreatePlacePanel(String id) {
        super(id);

        form = new Form<PlaceData>("form", newPlaceModel)
                .add(new Label("title", getTitleModel()))
                .add(new TextField("id").setRequired(true))
                .add(new TextField("name").setRequired(true))
                .add(new TextArea("notes"))
                .add(new TextField("contactName"))
                .add(new TextField("email"))
                .add(new FidDropDownChoice<Level>("level", new PropertyModel(newPlaceModel,"level"), Lists.newArrayList(Level.values()), new EnumChoiceRenderer<Level>()) {
                    @Override public boolean isVisible() {
                        return newPlaceModel.getObject().parent instanceof PrimaryOrg;
                    }
                })
                .add(new AddressPanel("address",new PropertyModel(newPlaceModel,"address")).withNoMap())
                .add(new AjaxSubmitLink("submit") {
                    @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        // TODO : add feedback of some sort???
                        PlaceData data = (PlaceData) CreatePlacePanel.this.form.getDefaultModelObject();
                        onCreate(data.createNewChildOrg(), target);
                    }
                    @Override protected void onError(AjaxRequestTarget target, Form<?> form) {

                    }
                })
                .add(new AjaxLink("cancel") {
                    @Override public void onClick(AjaxRequestTarget target) {
                        onCancel(target);
                    }
                })
                .setOutputMarkupPlaceholderTag(true);

        add(form);
        add(new WebMarkupContainer("blankSlate") {
            @Override
            public boolean isVisible() {
                return !form.isVisible();
            }
        });
        setOutputMarkupPlaceholderTag(true);
        IModel<String> cssClass = getCssClass();
        if (cssClass!=null) {
            add(new AttributeAppender("class", cssClass));
        }

        form.setVisible(false);
    }

    private IModel<String> getTitleModel() {
        return new Model<String>() {
            @Override public String getObject() {
                PlaceData data = newPlaceModel.getObject();
                if (data.getParent() instanceof PrimaryOrg) {
                    return new FIDLabelModel("label.add_secondary_customer_to",data.parent.getName()).getObject();
                } else if (data.getParent() instanceof SecondaryOrg) {
                    return new FIDLabelModel("label.add_customer_to",data.parent.getName()).getObject();
                } else {
                    return new FIDLabelModel("label.add_division_to",data.parent.getName()).getObject();
                }
            }
        };
    }

    protected IModel<String> getCssClass() {
        return null;
    }

    public CreatePlacePanel showBlankSlate(boolean showBlankSlate) {
        form.setVisible(!showBlankSlate);
        return this;
    }

    public CreatePlacePanel show() {
        form.setVisible(true);
        return this;
    }



    public CreatePlacePanel toggle() {
        form.setVisible(!form.isVisible());
        return this;
    }

    protected void onCancel(AjaxRequestTarget target) { }

    protected void onCreate(BaseOrg org, AjaxRequestTarget target) { }

    public CreatePlacePanel resetModelObject(BaseOrg parent) {
        form.setDefaultModelObject(new PlaceData(parent));
        return this;
    }


    class PlaceData implements Serializable {
        private String id, contactName, email, notes, name;
        private AddressInfo address = new AddressInfo();
        private BaseOrg parent;
        private Level level;

        public PlaceData() {
        }

        PlaceData(BaseOrg parent) {
            this.parent = parent;
        }

        public BaseOrg createNewChildOrg() {
            if (parent instanceof PrimaryOrg) {
                PrimaryOrg primary = (PrimaryOrg) parent;
                // TODO : augment builders to accommodate notes, address, etc..
                return Level.SECONDARY.equals(level) ?
                        SecondaryOrgBuilder.aSecondaryOrg().withPrimaryOrg(primary).withName(name).withId(null).build() :
                        CustomerOrgBuilder.aCustomerOrg().withParent(primary).withName(name).withId(null).build();
            } else if (parent instanceof CustomerOrg) {
                CustomerOrg customer = (CustomerOrg) parent;
                return DivisionOrgBuilder.aDivisionOrg().withCustomerOrg(customer).withName(name).withId(null).build();
            } else if (parent instanceof SecondaryOrg) {
                SecondaryOrg secondary = (SecondaryOrg) parent;
                return CustomerOrgBuilder.aCustomerOrg().withParent(secondary).withName(name).withId(null).build();
            }
            throw new IllegalStateException("can't build child org for " + parent==null?"NULL":parent.getClass().getSimpleName() + " org");
        }

        public BaseOrg getParent() {
            return parent;
        }

        @Override
        public String toString() {
            return "Place{" +
                    "address=" + address +
                    ", level='" + level + '\'' +
                    ", contactName='" + contactName + '\'' +
                    ", email='" + email + '\'' +
                    ", notes='" + notes + '\'' +
                    ", name='" + name + '\'' +
                    ", parent=" + (parent==null? "noParent" : parent.getId()) +
                    '}';
        }
    }

}
