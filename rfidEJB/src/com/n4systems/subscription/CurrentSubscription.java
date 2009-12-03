package com.n4systems.subscription;

public class CurrentSubscription {
	
	private final long contractId;
	private final boolean phonesupport;
	private final boolean creditCardOnFile;
	private final boolean payByCreditCard;
	

	public CurrentSubscription(long contractId, boolean phonesupport, boolean creditCardOnFile, boolean payByCreditCard) {
		super();
		this.contractId = contractId;
		this.phonesupport = phonesupport;
		this.creditCardOnFile = creditCardOnFile;
		this.payByCreditCard = payByCreditCard;
	}


	public Long getContractId() {
		return contractId;
	}


	public boolean isPhonesupport() {
		return phonesupport;
	}


	public boolean isCreditCardOnFile() {
		return creditCardOnFile;
	}


	public boolean isPayingByCreditCard() {
		return payByCreditCard;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (contractId ^ (contractId >>> 32));
		result = prime * result + (creditCardOnFile ? 1231 : 1237);
		result = prime * result + (phonesupport ? 1231 : 1237);
		result = prime * result + (payByCreditCard ? 1231 : 1237);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof CurrentSubscription)) {
			return false;
		}
		CurrentSubscription other = (CurrentSubscription) obj;
		if (contractId != other.contractId) {
			return false;
		}
		if (creditCardOnFile != other.creditCardOnFile) {
			return false;
		}
		if (phonesupport != other.phonesupport) {
			return false;
		}
		if (payByCreditCard != other.payByCreditCard) {
			return false;
		}
		return true;
	}


	public boolean isUpgradeRequiresBillingInformation() {
		return !payByCreditCard;
	}
}
