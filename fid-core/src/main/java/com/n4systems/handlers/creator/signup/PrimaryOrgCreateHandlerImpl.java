package com.n4systems.handlers.creator.signup;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.creator.signup.model.AccountCreationInformation;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.tools.EncryptionUtility;

public class PrimaryOrgCreateHandlerImpl implements PrimaryOrgCreateHandler {
	private static final String DEFAULT_DATE_FORMAT = "MM/dd/yy";

	private static final String DEFAULT_IDENTIFIER_FORMAT = "NSA%y-%g";
    private static final String DEFAULT_IDENTIFIER_LABEL = "Serial Number";

	private final OrgSaver orgSaver;
	
	private AccountCreationInformation accountInfo;
	private Tenant tenant;

	
	public PrimaryOrgCreateHandlerImpl(OrgSaver orgSaver) {
		super();
		this.orgSaver = orgSaver;
	}

	public void create(Transaction transaction) {
		createWithUndoInformation(transaction);
	}
	
	public PrimaryOrg createWithUndoInformation(Transaction transaction) {
		guards();
		
		PrimaryOrg primaryOrg = createPrimaryOrg();
		
		orgSaver.save(transaction, primaryOrg);
		return primaryOrg;
	}
	
	private PrimaryOrg createPrimaryOrg() {
		PrimaryOrg primaryOrg = new PrimaryOrg();
		primaryOrg.setTenant(tenant);
		primaryOrg.setUsingSerialNumber(true);
		primaryOrg.setIdentifierFormat(DEFAULT_IDENTIFIER_FORMAT);
        primaryOrg.setIdentifierLabel(DEFAULT_IDENTIFIER_LABEL);
		primaryOrg.setDateFormat(DEFAULT_DATE_FORMAT);
		primaryOrg.setName(accountInfo.getCompanyName());
		primaryOrg.setCertificateName(accountInfo.getCompanyName());
		primaryOrg.setDefaultTimeZone(accountInfo.getFullTimeZone());
		primaryOrg.setExternalUserName(accountInfo.getEmail());
		primaryOrg.setExternalPassword(generateExternalPassword());
		
		primaryOrg.setAddressInfo(convertToAddressInfo());
		
		return primaryOrg;
	}
	
	private AddressInfo convertToAddressInfo() {
		com.n4systems.subscription.AddressInfo inputAddress = accountInfo.getBillingAddress();
		
		AddressInfo address = new AddressInfo();
		address.setStreetAddress(inputAddress.getAddressLine1(), inputAddress.getAddressLine2());
		address.setCity(inputAddress.getCity());
		address.setState(inputAddress.getState());
		address.setCountry(inputAddress.getCountryFullName());
		address.setZip(inputAddress.getPostal());
		address.setPhone1(accountInfo.getPhone());
		
		return address;
	}

	private String generateExternalPassword() {
		// Can be a maximum of 16 characters
		return EncryptionUtility.getSHA1HexHash(accountInfo.getCompanyName()+accountInfo.getEmail()).substring(0,15);
	}
	
	private void guards() {
		if (accountInfo == null) {
			throw new InvalidArgumentException("you must specify an " + AccountCreationInformation.class.getName());
		}
		
		if (invalidTenant()) {
			throw new InvalidArgumentException("you must set a saved tenant.");
		}
		
	}

	private boolean invalidTenant() {
		return tenant == null || tenant.isNew();
	}
	
	
	public void undo(Transaction transaction, PrimaryOrg primaryOrgToRemove) {
		orgSaver.remove(transaction, primaryOrgToRemove);
	}
	
	public PrimaryOrgCreateHandler forAccountInfo(AccountCreationInformation accountInfo) {
		this.accountInfo = accountInfo;
		return this;
	}


	public PrimaryOrgCreateHandler forTenant(Tenant tenant) {
		this.tenant = tenant;
		return this;
	}

}
