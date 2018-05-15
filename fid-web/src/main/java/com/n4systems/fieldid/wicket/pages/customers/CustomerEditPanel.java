package com.n4systems.fieldid.wicket.pages.customers;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.web.helper.SessionUser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel to either edit an existing CustomerOrg or create a new one
 */
abstract public class CustomerEditPanel extends Panel {

    private static final Logger logger = Logger.getLogger(CustomerEditPanel.class);

    @SpringBean
    private OrgService orgService;

    @SpringBean
    private S3Service s3Service;

    private LoaderFactory loaderFactory;
    private List<ListingPair> internalOrgList;
    private IModel<Long> customerSelectedForEditModel;
    private IModel<SessionUser> sessionUserModel;
    private IModel<WebSessionMap> webSessionMapModel;
    private CustomerOrg currentCustomer;
    private Form form;
    private boolean userHasSelectedImageFile;
    private boolean userHasRemovedImageFile;
    private boolean removeSavedImageFile;

    public CustomerEditPanel(String id,  IModel<Long> customerSelectedForEditModel,
                             IModel<SessionUser> sessionUserModel, IModel<WebSessionMap> webSessionMapModel,
                             LoaderFactory loaderFactory) {

        super(id);
        this.customerSelectedForEditModel = customerSelectedForEditModel;
        this.sessionUserModel = sessionUserModel;
        this.webSessionMapModel = webSessionMapModel;
        this.loaderFactory = loaderFactory;
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/user.css");
        response.renderCSS(".hideElement {display: none;}", null);
    }

    @Override
    protected void onBeforeRender() {
        Long customerId = customerSelectedForEditModel.getObject();
        if (customerId != null) {
            currentCustomer = (CustomerOrg) orgService.findById(customerId);
            if (currentCustomer.getAddressInfo() == null) {
                currentCustomer.setAddressInfo(new AddressInfo());
            }
            if (currentCustomer.getContact() == null) {
                currentCustomer.setContact(new Contact());
            }
        }
        else
            currentCustomer = null;

        userHasSelectedImageFile = false;
        userHasRemovedImageFile = false;
        removeSavedImageFile = false;
        super.onBeforeRender();
    }

    private void addComponents() {

        /* Create the models */

        final IModel<String> customerIdModel = new LoadableDetachableModel<String>() {
            protected String load() {
                return currentCustomer != null ? currentCustomer.getCode() : null;
            }
        };
        final IModel<ListingPair> orgUnitValue = new LoadableDetachableModel<ListingPair>() {
            protected ListingPair load() {
                InternalOrg orgUnit;
                if (currentCustomer != null) {
                    orgUnit = currentCustomer.getParent();
                }
                else {
                    orgUnit = getSessionUserOwner().getInternalOrg();
                }
                return new ListingPair(orgUnit.getId(), orgUnit.getName());
            }
        };
        final IModel<String> customerNameModel = new LoadableDetachableModel<String>() {
            protected String load() {
                return currentCustomer != null ? currentCustomer.getName() : null;
            }
        };
        final IModel<String> customerNotesModel = new LoadableDetachableModel<String>() {
            protected String load() {
                return currentCustomer != null ? currentCustomer.getNotes() : null;
            }
        };
        final IModel<String> customerLogoUrlModel = new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                if (currentCustomer != null) {
                    return getCustomerLogoUrl(currentCustomer.getId());
                }
                else
                    return null;
            }
        };
        final IModel<String> customerContactNameModel = new LoadableDetachableModel<String>() {
            protected String load() {
                if (currentCustomer != null && currentCustomer.getContact() != null)
                     return currentCustomer.getContact().getName();
                else
                    return null;
            }
        };
        final IModel<String> customerAccountManagerEmailModel = new LoadableDetachableModel<String>() {
            protected String load() {
                if (currentCustomer != null && currentCustomer.getContact() != null)
                    return currentCustomer.getContact().getEmail();
                else
                    return null;
            }
        };
        final IModel<String> customerAddressInfoStreetAddressModel = new LoadableDetachableModel<String>() {
            protected String load() {
                if (currentCustomer != null && currentCustomer.getAddressInfo() != null)
                    return currentCustomer.getAddressInfo().getStreetAddress();
                else
                    return null;
            }
        };
        final IModel<String> customerAddressInfoCityModel = new LoadableDetachableModel<String>() {
            protected String load() {
                if (currentCustomer != null && currentCustomer.getAddressInfo() != null)
                    return currentCustomer.getAddressInfo().getCity();
                else
                    return null;
            }
        };
        final IModel<String> customerAddressInfoStateModel = new LoadableDetachableModel<String>() {
            protected String load() {
                if (currentCustomer != null && currentCustomer.getAddressInfo() != null)
                    return currentCustomer.getAddressInfo().getState();
                else
                    return null;
            }
        };;
        final IModel<String> customerAddressInfoZipModel = new LoadableDetachableModel<String>() {
            protected String load() {
                if (currentCustomer != null && currentCustomer.getAddressInfo() != null)
                    return currentCustomer.getAddressInfo().getZip();
                else
                    return null;
            }
        };;
        final IModel<String> customerAddressInfoCountryModel = new LoadableDetachableModel<String>() {
            protected String load() {
                if (currentCustomer != null && currentCustomer.getAddressInfo() != null)
                    return currentCustomer.getAddressInfo().getCountry();
                else
                    return null;
            }
        };;
        final IModel<String> customerAddressInfoPhone1Model = new LoadableDetachableModel<String>() {
            protected String load() {
                if (currentCustomer != null && currentCustomer.getAddressInfo() != null)
                    return currentCustomer.getAddressInfo().getPhone1();
                else
                    return null;
            }
        };;
        final IModel<String> customerAddressInfoPhone2Model = new LoadableDetachableModel<String>() {
            protected String load() {
                if (currentCustomer != null && currentCustomer.getAddressInfo() != null)
                    return currentCustomer.getAddressInfo().getPhone2();
                else
                    return null;
            }
        };
        final IModel<String> customerAddressInfoFax1Model = new LoadableDetachableModel<String>() {
            protected String load() {
                if (currentCustomer != null && currentCustomer.getAddressInfo() != null)
                    return currentCustomer.getAddressInfo().getFax1();
                else
                    return null;
            }
        };

        List<FileUpload> lst = new ArrayList<FileUpload>();
        final IModel<List<FileUpload>> fileUploadModel = new Model();
        fileUploadModel.setObject(lst);


        /* Create the form and define its submission action */

        form = new Form("customerForm");

        form.add(new AjaxSubmitLink("formSubmitLink", form) {
            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }

            @Override
            public void onSubmit(AjaxRequestTarget target, Form form) {

                /* Select/create customer to update/create */
                CustomerOrg customer;
                if (currentCustomer != null)
                    customer = currentCustomer;
                else {
                    customer = new CustomerOrg();
                    customer.setTenant(getWebSessionMap().getSecurityGuard().getTenant());
                }
                customer.setParent((InternalOrg) orgService.findById(orgUnitValue.getObject().getId()));

                customer.setCode(customerIdModel.getObject());
                customer.setName(customerNameModel.getObject());
                customer.setNotes(customerNotesModel.getObject());

                customer.getContact().setName(customerContactNameModel.getObject());
                customer.getContact().setEmail(customerAccountManagerEmailModel.getObject());

                if (customer.getLinkedOrg() == null) {
                    AddressInfo addressInfo = customer.getAddressInfo();
                    addressInfo.setStreetAddress(customerAddressInfoStreetAddressModel.getObject());
                    addressInfo.setCity(customerAddressInfoCityModel.getObject());
                    addressInfo.setState(customerAddressInfoStateModel.getObject());
                    addressInfo.setZip(customerAddressInfoZipModel.getObject());
                    addressInfo.setCountry(customerAddressInfoCountryModel.getObject());
                    addressInfo.setPhone1(customerAddressInfoPhone1Model.getObject());
                    addressInfo.setPhone2(customerAddressInfoPhone2Model.getObject());
                    addressInfo.setFax1(customerAddressInfoFax1Model.getObject());
                }

                boolean savingCustomer = false;
                boolean savingImage = false;
                try {
                    savingCustomer = true;
                    customer.touch();
                    currentCustomer = (CustomerOrg) orgService.saveOrUpdate(customer);
                    savingCustomer = false;
                    FileUpload fileUpload =
                            (fileUploadModel.getObject() != null && fileUploadModel.getObject().size() > 0)
                                    ? fileUploadModel.getObject().get(0) : null;
                    if (fileUpload != null) {
                        logger.info("fileUpload is " + fileUpload.getClientFileName());
                        savingImage = true;
                        addCustomerImage(customer, fileUpload.getClientFileName(), fileUpload.getInputStream());
                    }
                    else
                    if (removeSavedImageFile) {
                        savingImage = true;
                        removeCustomerImage(customer);
                    }
                }
                catch(Exception ex) {
                    if (savingCustomer) {
                        logger.error("Attempt to create customer failed ", ex);
                    }
                    else
                    if (savingImage) {
                        logger.error("Update of customer image failed", ex);
                    }
                    else {
                        logger.error("Update of customer failed", ex);
                    }
                    error(new FIDLabelModel("error.savingcustomer").getObject());
                }
                info(new FIDLabelModel("message.saved").getObject());
                target.addChildren(getPage(), FeedbackPanel.class);
                postSaveAction(currentCustomer.getId(), target);
            }
        });

        form.setOutputMarkupId(true);
        form.setMultiPart(true);

        /* Instructions in case current customer is linked */
        final WebMarkupContainer linkedInstructions = new WebMarkupContainer("linkedCustomerInstructions") {
            @Override
            public boolean isVisible() {
                return currentCustomer != null ? (currentCustomer.getLinkedOrg() != null) : false;
            }
        };
        linkedInstructions.setOutputMarkupId(true);
        linkedInstructions.setOutputMarkupPlaceholderTag(true);
        form.add(linkedInstructions);

        /* Define the data entry fields */

        final TextField customerIdField = new TextField("customerId", customerIdModel);
        customerIdField.setRequired(true);
        form.add(customerIdField);

        final DropDownChoice<ListingPair> orgUnit = new DropDownChoice<ListingPair>("parentOrgId",
                orgUnitValue,
                new LoadableDetachableModel<List<ListingPair>>() {
                    @Override
                    protected List<ListingPair> load() {
                        List<ListingPair> orgs = getParentOrgs();
                        return orgs;
                    }
                },
                new IChoiceRenderer<ListingPair>() {
                    @Override
                    public Object getDisplayValue(ListingPair object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(ListingPair object, int index) {
                        return object.getId().toString();
                    }
                }
        );
        orgUnit.setRequired(true);
        form.add(orgUnit);

        final TextField customerNameField = new TextField("customerName", customerNameModel) {
            @Override
            public boolean isEnabled() {
                return currentCustomer != null ? (currentCustomer.getLinkedOrg() == null) : true;
            }
        };
        customerNameField.setRequired(true);
        form.add(customerNameField);

        final TextArea customerNotes = new TextArea("customerNotes", customerNotesModel);
        form.add(customerNotes);

        final FileUploadField fileUploadField = new FileUploadField("fileToUpload", fileUploadModel);
        fileUploadField.setOutputMarkupId(true);
        fileUploadField.setOutputMarkupPlaceholderTag(true);
        fileUploadField.add(new AttributeModifier("class", "hideElement") {
            /* Overriding setVisible does not work properly with this field so this approach is used instead */
            @Override
            public boolean isEnabled(Component component) {

                return customerLogoUrlModel.getObject() != null && !userHasSelectedImageFile && !userHasRemovedImageFile;
            }
        });
        form.add(fileUploadField);

        final WebMarkupContainer uploadInfo = new WebMarkupContainer("uploadInfo") {
            @Override
            public boolean isVisible() {
                return customerLogoUrlModel.getObject() == null && !userHasSelectedImageFile;
            }
        };
        uploadInfo.setOutputMarkupId(true);
        uploadInfo.setOutputMarkupPlaceholderTag(true);
        form.add(uploadInfo);

        final WebMarkupContainer customerLogoImage = new WebMarkupContainer("customerLogoImage") {
            @Override
            public boolean isVisible() {
                return customerLogoUrlModel.getObject() != null;
            }
        };
        customerLogoImage.setOutputMarkupId(true);
        customerLogoImage.setOutputMarkupPlaceholderTag(true);
        customerLogoImage.add(new AttributeModifier("src", customerLogoUrlModel));
        form.add(customerLogoImage);

        final AjaxLink removeImageButton = new AjaxLink("removeImageButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                userHasSelectedImageFile = false;
                userHasRemovedImageFile = true;
                if (customerLogoUrlModel.getObject() != null)
                    removeSavedImageFile = true;
                customerLogoUrlModel.setObject(null);
                target.add(this);
                target.add(customerLogoImage);
                target.add(fileUploadField);
                target.add(uploadInfo);
            }
            @Override
            public boolean isVisible() {
                return customerLogoUrlModel.getObject() != null || userHasSelectedImageFile;
            }
        };
        removeImageButton.setOutputMarkupId(true);
        removeImageButton.setOutputMarkupPlaceholderTag(true);
        form.add(removeImageButton);

        fileUploadField.add(new FileSelectionIndicator() {
            public void onFileSelectionEvent(AjaxRequestTarget target) {
                userHasSelectedImageFile = true;
                target.add(removeImageButton);
                target.add(uploadInfo);
            }
        });

        final TextField<String> customerContactNameField =
                new TextField<String>("contactName", customerContactNameModel);
        form.add(customerContactNameField);
        final TextField<String> customerAccountManagerEmailField =
                new TextField<String>("accountManagerEmail", customerAccountManagerEmailModel);
        form.add(customerAccountManagerEmailField);

        final TextField<String> customerAddressInfoStreetAddress =
                new TextField<String>("addressInfo.streetAddress", customerAddressInfoStreetAddressModel) {
                    @Override
                    public boolean isEnabled() {
                        return currentCustomer != null ? (currentCustomer.getLinkedOrg() == null) : true;
                    }
                };
        form.add(customerAddressInfoStreetAddress);
        final TextField<String> customerAddressInfoCity =
                new TextField<String>("addressInfo.city", customerAddressInfoCityModel) {
                    @Override
                    public boolean isEnabled() {
                        return currentCustomer != null ? (currentCustomer.getLinkedOrg() == null) : true;
                    }
                };
        form.add(customerAddressInfoCity);
        final TextField<String> customerAddressInfoState =
                new TextField<String>("addressInfo.state", customerAddressInfoStateModel) {
                    @Override
                    public boolean isEnabled() {
                        return currentCustomer != null ? (currentCustomer.getLinkedOrg() == null) : true;
                    }
                };
        form.add(customerAddressInfoState);
        final TextField<String> customerAddressInfoZip =
                new TextField<String>("addressInfo.zip", customerAddressInfoZipModel) {
                    @Override
                    public boolean isEnabled() {
                        return currentCustomer != null ? (currentCustomer.getLinkedOrg() == null) : true;
                    }
                };
        form.add(customerAddressInfoZip);
        final TextField<String> customerAddressInfoCountry =
                new TextField<String>("addressInfo.country", customerAddressInfoCountryModel) {
                    @Override
                    public boolean isEnabled() {
                        return currentCustomer != null ? (currentCustomer.getLinkedOrg() == null) : true;
                    }
                };
        form.add(customerAddressInfoCountry);
        final TextField<String> customerAddressInfoPhone1 =
                new TextField<String>("addressInfo.phone1", customerAddressInfoPhone1Model) {
                    @Override
                    public boolean isEnabled() {
                        return currentCustomer != null ? (currentCustomer.getLinkedOrg() == null) : true;
                    }
                };
        form.add(customerAddressInfoPhone1);
        final TextField<String> customerAddressInfoPhone2 =
                new TextField<String>("addressInfo.phone2", customerAddressInfoPhone2Model) {
                    @Override
                    public boolean isEnabled() {
                        return currentCustomer != null ? (currentCustomer.getLinkedOrg() == null) : true;
                    }
                };
        form.add(customerAddressInfoPhone2);
        final TextField<String> customerAddressInfoFax1 =
                new TextField<String>("addressInfo.fax1", customerAddressInfoFax1Model) {
                    @Override
                    public boolean isEnabled() {
                        return currentCustomer != null ? (currentCustomer.getLinkedOrg() == null) : true;
                    }
                };
        form.add(customerAddressInfoFax1);


        AjaxLink cancelButton = new AjaxLink("cancelButton") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getRequestCycle().setResponsePage(CustomerActionsPage.class,
                        PageParametersBuilder.param(
                                CustomerActionsPage.INITIAL_TAB_SELECTION_KEY,
                                CustomerActionsPage.SHOW_CUSTOMER_LIST_PAGE));
            }
        };
        form.add(cancelButton);

        add(form);
    }

    private void addCustomerImage(CustomerOrg customer,
                              String fileName,
                              InputStream inputStream) throws IOException {

        if (inputStream != null) {
            File uploadedImage = new File(fileName);
            FileUtils.copyInputStreamToFile(inputStream, uploadedImage);
            s3Service.uploadCustomerLogo(uploadedImage, customer.getId());
            uploadedImage.delete();
        }
    }

    private void removeCustomerImage(CustomerOrg customer) {
        s3Service.removeCustomerLogo(customer.getId());
    }

    protected abstract void postSaveAction(Long customerId, AjaxRequestTarget target);

    private String getCustomerLogoUrl(Long customerId) {
        if (customerId == null)
            return null;
        if (!s3Service.customerLogoExists(customerId))
            return null;
        URL logoUrl = s3Service.getCustomerLogoURL(customerId);
        return logoUrl.toString();
        }

    private List<ListingPair> getParentOrgs() {
        if( internalOrgList == null ) {
            List<Listable<Long>> orgListables = getLoaderFactory().createInternalOrgListableLoader().load();
            internalOrgList = ListHelper.longListableToListingPair(orgListables);
        }
        return internalOrgList;
    }

    private SessionUser getSessionUser() {
        return sessionUserModel.getObject();
    }
    private BaseOrg getSessionUserOwner() {
        return getSessionUser().getOwner();
    }
    private WebSessionMap getWebSessionMap() {
        return webSessionMapModel.getObject();
    }
    private LoaderFactory getLoaderFactory() {
        return loaderFactory;
    }


    /**
     * This behavior is used to inform this page when the user selects a file in the image file selection field.
     * The regular Wicket Ajax onchange behaviors do not work with a FileUploadField so this approach is followed.
     */
    //TODO Wicket upgrade may provide an easier way of doing this
    private abstract class FileSelectionIndicator extends AbstractDefaultAjaxBehavior {
        @Override
        public void respond( AjaxRequestTarget target )
        {
            onFileSelectionEvent(target);
        }

        /**
         * This method will be called when the user selects a file.
         *
         * @param target
         */
        public abstract void onFileSelectionEvent(AjaxRequestTarget target);

        @Override
        public void renderHead(Component component, IHeaderResponse response )
        {
            super.renderHead(component, response);
            String script = "document.getElementById('" + component.getMarkupId() +
                    "').onchange = function () {  wicketAjaxGet(\""
                    + this.getCallbackUrl() + "\", function() { }, function() { } ); return true; };";

            response.renderOnDomReadyJavaScript(script);
        }
    }
}
