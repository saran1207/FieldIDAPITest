package com.n4systems.subscription.netsuite.client;

import java.io.IOException;

import com.n4systems.subscription.AddressInfo;
import com.n4systems.subscription.PaymentFrequency;
import com.n4systems.subscription.netsuite.model.NetsuiteClient;
import com.n4systems.subscription.netsuite.model.ContractLength;
import com.n4systems.subscription.netsuite.model.CreditCard;
import com.n4systems.subscription.netsuite.model.CreditCardType;
import com.n4systems.subscription.netsuite.model.GetItemDetailsResponse;
import com.n4systems.subscription.netsuite.model.GetPricingDetailsResponse;
import com.n4systems.subscription.netsuite.model.ProductInformation;
import com.n4systems.subscription.netsuite.model.NetsuiteSignUpTenantResponse;
import com.n4systems.subscription.netsuite.model.NetsuiteSubscription;
import com.n4systems.subscription.netsuite.model.NetsuiteTenant;
import com.n4systems.subscription.netsuite.model.NetSuiteValidatePromoCodeResponse;

public class RestfulTest {

	public static void main(String args[]) {
		
		ProductDetailsClient productDetailsLoader = new ProductDetailsClient();
		
		GetItemDetailsResponse detailsResponse = null;
		
		try {
			detailsResponse= productDetailsLoader.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		TenantRestfulSaver tenantSaver = new TenantRestfulSaver();

		Tenant tenant = new Tenant();
		tenant.setDisplayName("N4 TEST");
		tenant.setName("N4TEST");
		
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setCity("Toronto");
		addressInfo.setCountry("Canada");
		addressInfo.setId(500L);
		addressInfo.setPhone1("416-599-6466");
		addressInfo.setState("ON");
		addressInfo.setStreetAddress("179 John St. Suite 101");
		addressInfo.setZip("M5T 1X4");
		
		tenant.setAddressInfo(addressInfo);
		tenant.setAdminEmail("dev@n4systems.com");
		tenant.setId(500L);

		String response = "";
		try {
			response = tenantSaver.setTenant(tenant).setNetsuiteRecordId(3790L).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

		/*
		ClientRestfulSaver clientSaver = new ClientRestfulSaver();
		
		String response = "";
		try {
			response = clientSaver.setFirstName("N4").setLastName("TEST").setEmail("n4test@n4systems.com")
				.setPhoneNumber("416-599-6464").setFieldId(0).setTenantId(0).setPrimary(true)
				.Save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		
		/*
		for (ProductInformation productInformation : detailsResponse.getItemlist()) {
			if (productInformation.getContractlengths() != null) {
				for (ContractLength contractLength : productInformation.getContractlengths()) {
					System.out.println(productInformation.getName()+" = "+contractLength.getPrice());
				}
			} else {
				System.out.println(productInformation.getName()+" = Free!");
			}
		}
		
		System.out.println("Result: "+detailsResponse.getResult());
		System.out.println("Itemlist:" +detailsResponse.getItemlist());
		
		GetPricingDetailsResponse pricingDetailsResponse = null;
		PricingDetailsClient pricingDetails = new PricingDetailsClient();
		try {
			pricingDetails.setContractLength(12);
			pricingDetails.setFrequency("Monthly");
			pricingDetails.setItemId("143");
			pricingDetails.setUsers(5);
			pricingDetailsResponse = pricingDetails.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Contract value:"+pricingDetailsResponse.getPricing().getContract_value());
		System.out.println("Everything:"+pricingDetailsResponse.getPricing().toString());
		
		SignUpTenantClient signUpTenantClient = new SignUpTenantClient();
		signUpTenantClient.setPerson(populateTestClient());
		signUpTenantClient.setSubscription(populateTestSubscription());
		signUpTenantClient.setCompany(populateTestTenant());
		
		try {
			NetsuiteSignUpTenantResponse tenantResponse = signUpTenantClient.execute();
			
			System.out.println("Tenant netsuite id: "+tenantResponse.getTenant().getExternalId());
			System.out.println("CLient netsuite id: "+tenantResponse.getClient().getExternalId());
			System.out.println("Subscription netsuite id: "+tenantResponse.getSubscription().getExternalId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		ValidatePromoCodeClient promoClient = new ValidatePromoCodeClient();
		promoClient.setCode("SOMETHING");
		
		try {
			NetSuiteValidatePromoCodeResponse response = promoClient.execute();
			System.out.println("Result:"+response.getResult());
			System.out.println("Details:"+response.getDetails());

			promoClient.setCode("R5678");
			response = promoClient.execute();
			System.out.println("Result:"+response.getResult());
			System.out.println("Details:"+response.getDetails());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static NetsuiteSubscription populateTestSubscription() {
		NetsuiteSubscription subscription = new NetsuiteSubscription();
		subscription.setFrequency(PaymentFrequency.Monthly);
		subscription.setMonths(12);
		subscription.setNetsuiteRecordId(143L);
		subscription.setPurchasingPhoneSupport(true);
		subscription.setUsers(5);
		
		return subscription;
	}
	
	private static NetsuiteClient populateTestClient() {
		NetsuiteClient client = new NetsuiteClient();
		
		client.setN4Id("6");
		client.setFirstName("Guy");
		client.setLastName("Incognito");
		
		return client;
	}
	
	private static NetsuiteTenant populateTestTenant() {
		NetsuiteTenant tenant = new NetsuiteTenant();
		tenant.setBillingAddress(populateTestAddress());
		tenant.setCompanyName("TEST - Jesse Manufacturing");
		tenant.setCreditCard(populateTestCreditCard());
		tenant.setEmail("guyincognito@n4systems.com");
		tenant.setN4Id("5");
		tenant.setPhone("416-555-5555");
		tenant.setShippingAddress(populateTestAddress());
		tenant.setUsingCreditCard(true);
		
		return tenant;
	}
	
	private static CreditCard populateTestCreditCard() {
		CreditCard creditCard = new CreditCard();
		creditCard.setExpiry("1/11");
		creditCard.setName("Guy Incognito");
		creditCard.setNumber("4111111111111111");
		creditCard.setType(CreditCardType.Visa);
		
		return creditCard;
	}
	
	private static AddressInfo populateTestAddress() {
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setAddressLine1("179 John St");
		addressInfo.setAddressLine2("Suite 101");
		addressInfo.setCity("Toronto");
		addressInfo.setState("ON");
		addressInfo.setCountry("CA");
		addressInfo.setPostal("M5T1X4");
		
		return addressInfo;
	}
	
}
