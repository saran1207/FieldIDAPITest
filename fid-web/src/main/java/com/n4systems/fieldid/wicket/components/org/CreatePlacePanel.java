package com.n4systems.fieldid.wicket.components.org;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.addressinfo.AddressPanel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.feedback.TopFeedbackPanel;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.timezone.RegionListModel;
import com.n4systems.fieldid.wicket.components.timezone.RegionModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.orgs.CountryFromAddressModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.Region;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
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

    private Form<PlaceData> form;
    private Component feedback;
    private CompoundPropertyModel<PlaceData> newPlaceModel = new CompoundPropertyModel(new PlaceData());
    private MarkupContainer timeZoneContainer;
    private AddressPanel address;

    private String defaultTimeZone;

    public CreatePlacePanel(String id) {
        super(id);

        final IModel<String> timeZoneIdModel = new PropertyModel(this,"defaultTimeZone");
        final IModel<Country> countryModel = new CountryFromAddressModel(new PropertyModel<AddressInfo>(newPlaceModel, "address"));
        final IModel<Region> regionModel = new RegionModel(timeZoneIdModel,countryModel);

        form = new Form<PlaceData>("form", newPlaceModel);

        form.add(new Label("title", getTitleModel()))
            .add(feedback = new FIDFeedbackPanel("feedback").setOutputMarkupId(true))
            .add(new TextField("code").setRequired(true))
            .add(new TextField("name").setRequired(true))
            .add(new TextArea("notes"))
            .add(new TextField("contactName"))
            .add(new TextField("email"))
            .add(new FidDropDownChoice<Level>("level", new PropertyModel(newPlaceModel, "level"), Lists.newArrayList(Level.values()), new EnumChoiceRenderer<Level>()) {
                @Override public boolean isVisible() {
                    return newPlaceModel.getObject().parent instanceof PrimaryOrg;
                }
            }.setRequired(true).add(new OnChangeAjaxBehavior() {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.add(timeZoneContainer);
                }
            }))
            .add(address = new AddressPanel("address", new PropertyModel(newPlaceModel, "address")) {
                @Override
                protected void onCountryChange(AjaxRequestTarget target) {
                    target.add(timeZoneContainer);
                }
            }.withNoMap())
            .add(timeZoneContainer = new WebMarkupContainer("timeZoneContainer") {
                @Override
                public boolean isVisible() {
                    return newPlaceModel.getObject().level == Level.SECONDARY && address.isVisible();
                }
            }
                .add(new FidDropDownChoice<Region>("timeZone", regionModel, new RegionListModel(countryModel), new ListableChoiceRenderer<Region>()) {
                })
            )
            .add(new AjaxSubmitLink("submit") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    PlaceData data = (PlaceData) CreatePlacePanel.this.form.getDefaultModelObject();
                    BaseOrg childOrg = data.createNewChildOrg();
                    onCreate(childOrg, target);
                    info(new FIDLabelModel("label.create_place", childOrg.getName()).getObject());
                    target.add(getTopFeedbackPanel());
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedback);
                }
            })
            .add(new AjaxLink("cancel") {
                @Override public void onClick(AjaxRequestTarget target) {
                    onCancel(target);
                }
            })
            .setOutputMarkupPlaceholderTag(true);
        timeZoneContainer.setOutputMarkupPlaceholderTag(true);

        add(form);

        boolean canManageCustomers = FieldIDSession.get().getUserSecurityGuard().isAllowedManageEndUsers();

        add(new WebMarkupContainer("blankSlate").setVisible(!form.isVisible() && canManageCustomers));

        setOutputMarkupPlaceholderTag(true);
        IModel<String> cssClass = getCssClass();
        if (cssClass!=null) {
            add(new AttributeAppender("class", cssClass));
        }

        form.setVisible(false);
    }

    public BaseOrg getParentOrg() {
        PlaceData data = (PlaceData) form.getDefaultModelObject();
        return data.getParent();
    }

    public TopFeedbackPanel getTopFeedbackPanel() {
        if ( getPage() instanceof FieldIDTemplatePage) {
            return ((FieldIDTemplatePage)getPage()).getTopFeedbackPanel();
        }
        throw new IllegalStateException("current page doesn't have " + TopFeedbackPanel.class.getSimpleName());
    }

    public CreatePlacePanel forParentOrg(BaseOrg parent) {
        resetModelObject(parent);
        return this;
    }

    private IModel<String> getTitleModel() {
        return new Model<String>() {
            @Override public String getObject() {
                PlaceData data = newPlaceModel.getObject();
                Preconditions.checkArgument(data.getParent()!=null,"need to know what org you are adding to. ");
                String name = data.getParent().getName();
                if (data.getParent() instanceof PrimaryOrg) {
                    return new FIDLabelModel("label.add_secondary_customer", name).getObject();
                } else if (data.getParent() instanceof SecondaryOrg) {
                    return new FIDLabelModel("label.add_customer", name).getObject();
                } else {
                    return new FIDLabelModel("label.add_division", name).getObject();
                }
            }
        };
    }

    protected IModel<String> getCssClass() {
        return null;
    }

    public CreatePlacePanel show() {
        form.setVisible(true);
        return this;
    }

    public CreatePlacePanel toggle() {
        form.setVisible(!form.isVisible());
        return this;
    }

    public boolean isFormVisible() {
        return form.isVisible();
    }

    protected void onCancel(AjaxRequestTarget target) { }

    protected void onCreate(BaseOrg org, AjaxRequestTarget target) { }

    public CreatePlacePanel resetModelObject(BaseOrg parent) {
        form.setDefaultModelObject(new PlaceData(parent));
        return this;
    }

    public CreatePlacePanel resetModelObject() {
        PlaceData data = form.getModel().getObject();
        form.setDefaultModelObject(new PlaceData(data.getParent()));
        return this;
    }


    class PlaceData implements Serializable {
        private String code, contactName, email, notes, name;
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
                        OrgBuilder.aSecondaryOrg()
                                .withContact(contactName, email)
                                .withCode(code)
                                .withAddress(address)
                                .withNotes(notes)
                                .withParent(primary)
                                .withName(name)
                                .withId(null).build() :
                        OrgBuilder.aCustomerOrg()
                                .withCode(code)
                                .withContact(contactName, email)
                                .withAddress(address)
                                .withNotes(notes)
                                .withParent(primary)
                                .withName(name)
                                .withId(null).build();
            } else if (parent instanceof CustomerOrg) {
                CustomerOrg customer = (CustomerOrg) parent;
                return OrgBuilder.aDivisionOrg()
                        .withCode(code)
                        .withContact(contactName, email)
                        .withAddress(address)
                        .withNotes(notes)
                        .withParent(customer)
                        .withName(name)
                        .withId(null).build();
            } else if (parent instanceof SecondaryOrg) {
                SecondaryOrg secondary = (SecondaryOrg) parent;
                return OrgBuilder.aCustomerOrg()
                        .withCode(code)
                        .withContact(contactName, email)
                        .withAddress(address)
                        .withNotes(notes)
                        .withParent(secondary)
                        .withName(name)
                        .withId(null).build();
            }
            throw new IllegalStateException("can't build child org for " + parent==null?"NULL":parent.getClass().getSimpleName() + " org");
        }

        public BaseOrg getParent() {
            return parent;
        }

        @Override
        public String toString() {
            return "PlaceData{" +
                    "address=" + address +
                    ", code='" + code + '\'' +
                    ", contactName='" + contactName + '\'' +
                    ", email='" + email + '\'' +
                    ", notes='" + notes + '\'' +
                    ", name='" + name + '\'' +
                    ", parent=" + parent +
                    ", level=" + level +
                    '}';
        }
    }

}
