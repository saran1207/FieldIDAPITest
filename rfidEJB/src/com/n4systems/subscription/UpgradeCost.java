package com.n4systems.subscription;

public class UpgradeCost {

	private final float immediateCharge;
	private final float nextPayment;
	private final String nextPaymentDate;
	
	public UpgradeCost(float immediateCharge, float nextPayment, String nextPaymentDate) {
		this.immediateCharge = immediateCharge;
		this.nextPayment = nextPayment;
		this.nextPaymentDate = nextPaymentDate != null ? nextPaymentDate : "";
	}

	public float getImmediateCharge() {
		return immediateCharge;
	}

	public float getNextPayment() {
		return nextPayment;
	}

	public String getNextPaymentDate() {
		return nextPaymentDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(immediateCharge);
		result = prime * result + Float.floatToIntBits(nextPayment);
		result = prime * result + ((nextPaymentDate == null) ? 0 : nextPaymentDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UpgradeCost other = (UpgradeCost) obj;
		if (Float.floatToIntBits(immediateCharge) != Float.floatToIntBits(other.immediateCharge))
			return false;
		if (Float.floatToIntBits(nextPayment) != Float.floatToIntBits(other.nextPayment))
			return false;
		if (nextPaymentDate == null) {
			if (other.nextPaymentDate != null)
				return false;
		} else if (!nextPaymentDate.equals(other.nextPaymentDate))
			return false;
		return true;
	}

}
