package com.n4systems.subscription.netsuite.client;

import java.io.IOException;

import com.n4systems.subscription.netsuite.model.GetSubscriptionDetailsResponse;
import com.n4systems.subscription.netsuite.model.NetSuiteUpgradeSubscription;
import com.n4systems.subscription.netsuite.model.UpgradeSubscriptionResponse;

public class RestfulTest {

	public static void main(String args[]) {
		
		/*
		UpgradeSubscription upgradeSubscription = new UpgradeSubscription();
		upgradeSubscription.setContractExternalId(143L);
		upgradeSubscription.setTenantExternalId(5354L);
		upgradeSubscription.setShowPriceOnly(true);
		
		UpgradeSubscriptionClient upgradeSubscriptionClient = new UpgradeSubscriptionClient();
		upgradeSubscriptionClient.setUpgradeSubscription(upgradeSubscription);
		
		try {
			UpgradeSubscriptionResponse subResponse = upgradeSubscriptionClient.execute();
			System.out.println("Upgrade cost:" +subResponse.getUpgradesubscription().getUpgrade_cost());
			System.out.println("Next payment:"+subResponse.getUpgradesubscription().getNext_payment());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ProductDetailsClient productDetailsLoader = new ProductDetailsClient();
		
		GetItemDetailsResponse detailsResponse = null;
		
		try {
			detailsResponse= productDetailsLoader.execute();
			System.out.println("item list: "+detailsResponse.getItemlist());
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
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
		tenant.setAdminEmail("dev@fieldid.com");
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
			response = clientSaver.setFirstName("N4").setLastName("TEST").setEmail("n4test@fieldid.com")
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
		*/
		
		/*
		NetSuiteSubscriptionAgent agent = new NetSuiteSubscriptionAgent();
		
		try {
			SignUpTenantResponse tenantResponse = agent.buy(populateTestSubscription(), populateTestTenant(), populateTestClient());
			
			UploadNoteClient uploadNoteClient = new UploadNoteClient();
			uploadNoteClient.setTenantId(tenantResponse.getTenant().getExternalId());
			//uploadNoteClient.setTenantId(5149L);
			uploadNoteClient.setNote("This is me testing out the note thing.");
			uploadNoteClient.setTitle("TEST Note ** Jesse");
			Response response = uploadNoteClient.execute();
			
			System.out.println("Note repsonse:"+response.getResult());
			//System.out.println("Tenant netsuite id: "+tenantResponse.getTenant().getExternalId());
			//System.out.println("CLient netsuite id: "+tenantResponse.getClient().getExternalId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/

		/*
		ValidatePromoCodeClient promoClient = new ValidatePromoCodeClient();
		promoClient.setCode("SOMETHING");
		
		try {
			NetSuiteValidatePromoCodeResponse response = promoClient.execute();
			System.out.println("Valid? "+response.isValid());
			System.out.println("Reason:"+response.getReason());

			promoClient.setCode("R5678");
			response = promoClient.execute();
			System.out.println("Valid? "+response.isValid());
			System.out.println("Reason:"+response.getReason());
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
		/*
		ProductDetailsClient productDetailsClient = new ProductDetailsClient();
		try {
			GetItemDetailsResponse response = productDetailsClient.execute();
			System.out.println(response.getDetails());
		} catch (IOException e) {
			
		}
		*/

		SubscriptionDetailsClient detailsClient = new SubscriptionDetailsClient();
		// detailsClient.setTenantExternalId(6457324234L); // Bullshit 
		// detailsClient.setTenantExternalId(1142L); // Known but no subscription record 
		detailsClient.setTenantExternalId(6457L); // Known
		try {
			GetSubscriptionDetailsResponse subResponse = detailsClient.execute();
			if (subResponse.getSubscription() == null) {
				System.out.println("No active subscription");
			} else {
				System.out.println("Account type: "+subResponse.getSubscription().getAccountType());
				System.out.println("Contract id: "+subResponse.getSubscription().getContractId());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		UpgradeSubscriptionClient upgradeClient = new UpgradeSubscriptionClient();
		NetSuiteUpgradeSubscription upgradeSubscription = new NetSuiteUpgradeSubscription();
		upgradeSubscription.setTenantExternalId(6490L);
		upgradeSubscription.setContractExternalId(143L);
		upgradeSubscription.setNewUsers(0);
		upgradeClient.setUpgradeSubscription(upgradeSubscription);
		
		UpgradeSubscriptionResponse upgradeResponse = null;
		
		try {
			upgradeResponse = upgradeClient.execute();
			System.out.println("Upgrade cost: " + upgradeResponse.getUpgradesubscription().getUpgrade_cost());
			System.out.println("Next payment: " + upgradeResponse.getUpgradesubscription().getNext_payment());
			System.out.println("Was ok?" + upgradeResponse.requestRespondedWithSuccess());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		/*
		PricingDetailsClient pricingClient = new PricingDetailsClient();
		pricingClient.setSubscription(populateTestSubscription());
		
		try {
			GetPricingDetailsResponse priceResponse = pricingClient.execute();
			System.out.println("Pricing:"+priceResponse.getPricing().getFirstPaymentTotal());
			System.out.println("Standard price:" +priceResponse.getPricing().getStandardPrice());
			System.out.println("Discount price:" +priceResponse.getPricing().getDiscountPrice());
			System.out.println("Contract value:" +priceResponse.getPricing().getContractValue());
			System.out.println("Storage pricing:" +priceResponse.getPricing().getStoragePrice());
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}
	
	/*private static NetsuiteSubscription populateTestSubscription() {
		NetsuiteSubscription subscription = new NetsuiteSubscription();
		subscription.setFrequency(PaymentFrequency.Monthly);
		subscription.setMonths(12);
		subscription.setNsrecordid(143L);
		subscription.setPurchasingPhoneSupport(false);
		subscription.setUsers(5);
		subscription.setPromoCode("");
		
		return subscription;
		
		
		
	}
	
	private static NetsuiteClient populateTestClient() {
		NetsuiteClient client = new NetsuiteClient();
		
		client.setN4Id("515");
		client.setFirstName("Guy Not So");
		client.setLastName("Incdognito");
		
		return client;
	}
	
	private static NetsuiteTenant populateTestTenant() {
		NetsuiteTenant tenant = new NetsuiteTenant();
		tenant.setBillingAddress(populateTestAddress());
		tenant.setCompanyName("TEST - Jesse Manufacturing With A PO");
		tenant.setCreditCard(populateTestCreditCard());
		tenant.setEmail("badseemail@bad.com");
		tenant.setN4Id("555242");
		tenant.setPhone("416-555-5555");
		tenant.setShippingAddress(populateTestAddress());
		tenant.setUsingCreditCard(false);
		tenant.setPurchaseOrderNumber("TEST PO 123456");
		
		return tenant;
	}
	
	private static CreditCard populateTestCreditCard() {
		CreditCard creditCard = new CreditCard();
		creditCard.setExpiryMonth(1);
		creditCard.setExpiryYear(11);
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
	}*/
	
}
