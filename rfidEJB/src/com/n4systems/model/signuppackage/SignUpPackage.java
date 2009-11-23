package com.n4systems.model.signuppackage;

import java.util.Collections;
import java.util.List;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.subscription.PaymentOption;

public class SignUpPackage {
	
	
	private SignUpPackageDetails signPackageDetails;
	private List<ContractPricing> paymentOptions;

	
	public SignUpPackage(SignUpPackageDetails signPackageDetails, List<ContractPricing> paymentOptions) {
		super();
		this.signPackageDetails = signPackageDetails;
		this.paymentOptions = paymentOptions;
		Collections.sort(this.paymentOptions);
	}
	
	public SignUpPackageDetails getSignPackageDetails() {
		return signPackageDetails;
	}

	public List<ContractPricing> getPaymentOptions() {
		return paymentOptions;
	}

	public ContractPricing getDefaultPaymentOption() {
		for (ContractPricing paymentOption : paymentOptions) {
			if (paymentOption.getPaymentOption().isDefault()) {
				return paymentOption;
			}
		}
		throw new InvalidArgumentException("there is no contract that matches the default payment option.");
	}
	
	
	public ContractPricing getPaymentOptionWithType(String targetPaymentOption) {
		PaymentOption option = PaymentOption.valueOf(targetPaymentOption);
		for (ContractPricing paymentOption : paymentOptions) {
			if (paymentOption.getPaymentOption() == option) {
				return paymentOption;
			}
		}
		throw new InvalidArgumentException("there is no contract that matches the payment option. "  + targetPaymentOption);
	}
	
	
	public Long getAssets() {
		return signPackageDetails.getAssets();
	}

	public Long getDiskSpaceInMB() {
		return signPackageDetails.getDiskSpaceInMB();
	}

	public ExtendedFeature[] getExtendedFeatures() {
		return signPackageDetails.getExtendedFeatures();
	}

	public String getName() {
		return signPackageDetails.getName();
	}

	public Long getUsers() {
		return signPackageDetails.getUsers();
	}
	
	public boolean isPreferred() {
		return signPackageDetails.isPreferred();
	}

	public float getDefaultPricePerUserPerMonth() {
		return getDefaultPaymentOption().getPricePerUserPerMonth();
	}

	public Long getContractId(PaymentOption paymentOption) {
		return getContract(paymentOption).getExternalId();
	}
	
	public ContractPricing getContract(PaymentOption paymentOption) {
		for (ContractPricing contract : paymentOptions) {
			if (contract.getPaymentOption() == paymentOption) {
				return contract;
			}
		}
		throw new InvalidArgumentException("there is no contract that matches the given payment option.");
	}
	
	
	public boolean isFree() {
		return getDefaultPricePerUserPerMonth() == 0; 
	}
	
	public boolean includes(String feature) {
		ExtendedFeature featureToLookUp = ExtendedFeature.valueOf(feature);
		return signPackageDetails.includesFeature(featureToLookUp);
	}

	@Override
	public int hashCode() {
		return signPackageDetails.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		SignUpPackage other = (SignUpPackage) obj;
				
		return (signPackageDetails.equals(other.signPackageDetails));
	}

	
	
	
	
	
}
