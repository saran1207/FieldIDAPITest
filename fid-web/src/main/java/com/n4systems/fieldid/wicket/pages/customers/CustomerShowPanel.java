package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.user.AddCustomerUserPage;
import com.n4systems.fieldid.wicket.pages.useraccount.notificationsettings.NotificationSettingsListPage;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrgsForCustomerOrgLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserPaginatedLoader;
import com.n4systems.tools.Pager;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.Constants;

import java.net.URL;

/**
 * Created by agrabovskis on 2018-02-02.
 */
abstract public class CustomerShowPanel extends Panel {

    final private static Logger logger = Logger.getLogger(CustomerShowPanel.class);

    private IModel<SecurityFilter> securityFilterModel;
    private IModel<Long> customerSelectedForEditModel;
    private CustomerOrg currentCustomer;

    @SpringBean
    private OrgService orgService;

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private PlaceService placeService;

    public CustomerShowPanel(String id, IModel<Long> customerSelectedForEditModel, IModel<SecurityFilter> securityFilterModel) {

        super(id);
        this.securityFilterModel = securityFilterModel;
        this.customerSelectedForEditModel = customerSelectedForEditModel;
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/user.css");
        response.renderCSSReference("style/legacy/pageStyles/customers.css");
    }

    @Override
    protected void onBeforeRender() {
        Long customerId = customerSelectedForEditModel.getObject();
        currentCustomer = (CustomerOrg) orgService.findById(customerId);
        super.onBeforeRender();
    }

    private void addComponents() {

        /* Create models */
        final IModel<String> customerLogoURLModel = new IModel<String>() {
            public String getObject() {
                return getCustomerLogoUrl(currentCustomer.getId());
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerNameModel = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getName();
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerIdModel = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getCode();
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerOrganizationModel = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getParent() != null ? currentCustomer.getParent().getName() : "";
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerDivisionsLinkLabelModel = new IModel<String>() {
            public String getObject() {
                return String.format("%d %s",
                        getDivisionCount(currentCustomer), new FIDLabelModel("label.divisions").getObject());
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerUsersLinkLabelModel = new IModel<String>() {
            public String getObject() {
                return String.format("%d %s",
                        getUserCount(currentCustomer), new FIDLabelModel("label.user_accounts").getObject());
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerNotesModel = new IModel<String>() {
            public String getObject() {
                return replaceCR(currentCustomer.getNotes());
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerContactNameModel = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getContact() != null ? currentCustomer.getContact().getName() : "";
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerContactEmailModel = new IModel<String>() {
            public String getObject() {
                return String.format("mailto:%s",
                        currentCustomer.getContact() != null ? currentCustomer.getContact().getEmail() : "");
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerContactEmailLabelModel = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getContact() != null ? currentCustomer.getContact().getEmail() : "";
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerStreetAddressModel = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getAddressInfo() != null ? currentCustomer.getAddressInfo().getStreetAddress() : "";
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerCityModel = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getAddressInfo() != null ? currentCustomer.getAddressInfo().getCity() : "";
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerStateModel = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getAddressInfo() != null ? currentCustomer.getAddressInfo().getState() : "";
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerZipModel = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getAddressInfo() != null ? currentCustomer.getAddressInfo().getZip() : "";
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerCountryModel = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getAddressInfo() != null ? currentCustomer.getAddressInfo().getCountry() : "";
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerPhone1Model = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getAddressInfo() != null ? currentCustomer.getAddressInfo().getPhone1() : "";
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerPhone2Model = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getAddressInfo() != null ? currentCustomer.getAddressInfo().getPhone2() : "";
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };
        final IModel<String> customerFaxModel = new IModel<String>() {
            public String getObject() {
                return currentCustomer.getAddressInfo() != null ? currentCustomer.getAddressInfo().getFax1() : "";
            }

            public void setObject(final String object) {
            }

            public void detach() {
            }

        };

        /* Create controls */

        add(new Link("archiveLink") {
            @Override
            public void onClick() {
                doArchive(currentCustomer);
                postArchiveAction();
            }
        }.add(new AttributeAppender("onclick", new IModel() {
            @Override
            public Object getObject() {
                Long assetCount = getPlaceService().getAssetCount(currentCustomer.getId());
                if (assetCount != null && assetCount > 0) {
                    return "alert('" +
                            new FIDLabelModel("message.customer_owns_assets").getObject() + "'); return false;";
                } else {
                    return new StringBuilder("if(!confirm('").append(
                            new FIDLabelModel("message.customer_archive_warning").getObject()).
                            append("')) { return false; };");

                }
            }
            @Override
            public void setObject(Object object) {
            }
            @Override
            public void detach() {
            }
        })));

        add(new Link("mergeLink") {
                @Override
                public void onClick() {
                    mergeInvokedAction();
                }
            });

        add(new Link("addUserLink") {
            @Override
            public void onClick() {
                getRequestCycle().setResponsePage(
                        AddCustomerUserPage.class,
                        PageParametersBuilder.param("customerId", currentCustomer.getId()));
            }
        });

        add(new Link("setupEmailSettingsLink") {
            @Override
            public void onClick() {
                getRequestCycle().setResponsePage(NotificationSettingsListPage.class);
            }
        });

        WebMarkupContainer logoSection = new WebMarkupContainer("logoSection") {
            @Override
            public boolean isVisible() {
                return customerLogoExists(currentCustomer.getId());
            }
        };
        add(logoSection);
        final WebMarkupContainer customerLogoImage = new WebMarkupContainer("customerLogoImage") {
            @Override
            public boolean isVisible() {
                return customerLogoURLModel.getObject() != null;
            }
        };
        customerLogoImage.setOutputMarkupId(true);
        customerLogoImage.setOutputMarkupPlaceholderTag(true);
        customerLogoImage.add(new AttributeModifier("src", customerLogoURLModel));
        logoSection.add(customerLogoImage);

        WebMarkupContainer customerDetailsSection = new WebMarkupContainer("customerDetailsSection");
        customerDetailsSection.add(new AttributeModifier("class", "noImage") {
            @Override
            public boolean isEnabled(Component component) {
                return !customerLogoExists(currentCustomer.getId());
            }

            @Override
            protected String newValue(final String currentValue, final String replacementValue) {
                return currentValue + " " + replacementValue;
            }
        });

        customerDetailsSection.add(new Label("customerName", customerNameModel));
        customerDetailsSection.add(new Label("customerId", customerIdModel));
        customerDetailsSection.add(new Label("customerOrganization", customerOrganizationModel));
        customerDetailsSection.add(new Link("showDivisionsLink") {
            @Override
            public void onClick() {
                listDivisionsAction();
            }
        }.add(new Label("divisionsLinkLabel", customerDivisionsLinkLabelModel)));
        customerDetailsSection.add(new Label("usersLinkLabel", customerUsersLinkLabelModel));
        add(customerDetailsSection);


        add(new Label("customerNotes", customerNotesModel));

        add(new Label("contactName", customerContactNameModel));
        add(new ExternalLink("contactEmailLink", customerContactEmailModel).
                add(new Label("contactEmailLinkLabel", customerContactEmailLabelModel)));

        add(new Label("streetAddress", customerStreetAddressModel));
        add(new Label("city", customerCityModel));
        add(new Label("state", customerStateModel));
        add(new Label("zip", customerZipModel));
        add(new Label("country", customerCountryModel));
        add(new Label("phone1", customerPhone1Model));
        add(new Label("phone2", customerPhone2Model));
        add(new Label("fax", customerFaxModel));
    }

    private boolean customerLogoExists(Long customerId) {
        if (customerId == null)
            return false;
        else
            return s3Service.customerLogoExists(customerId);
    }

    private String getCustomerLogoUrl(Long customerId) {
        if (customerId == null)
            return null;
        if (!s3Service.customerLogoExists(customerId))
            return null;
        URL logoUrl = s3Service.getCustomerLogoURL(customerId);
        return logoUrl.toString();
    }

    private int getDivisionCount(CustomerOrg customer) {
        DivisionOrgsForCustomerOrgLoader loader = new DivisionOrgsForCustomerOrgLoader(getSecurityFilter());
        return loader.parent(customer).load().size();
    }

    private long getUserCount(CustomerOrg customer) {
        Pager<User> page = new UserPaginatedLoader(getSecurityFilter())
                .withCustomer(customer)
                .setPage(1)
                .setPageSize(Constants.PAGE_SIZE)
                .load();

        return page.getTotalResults();
    }

    private SecurityFilter getSecurityFilter() {
        return securityFilterModel.getObject();
    }

    private PlaceService getPlaceService() {
        return placeService;
    }

    private void doArchive(CustomerOrg customer) {
        placeService.archive(customer);
        Session.get().info("Archive successful");
        //target.addChildren(getPage(), FeedbackPanel.class);
    }

    private String replaceCR(String string) {
        if (string == null || string.isEmpty())
            return string;
        else
            return string.replace("\n", "<br />");
    }

    abstract protected void postArchiveAction();
    abstract protected void mergeInvokedAction();
    abstract protected void listDivisionsAction();
}
