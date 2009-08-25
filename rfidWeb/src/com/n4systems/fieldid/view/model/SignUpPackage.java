package com.n4systems.fieldid.view.model;

public class SignUpPackage {
	private Long id;
	private int priceInDollars;
	private boolean preferred;
	private com.n4systems.model.signuppackage.SignUpPackage signUpPackage;
	

	public SignUpPackage(Long id, String name, int priceInDollars, boolean preferred, Long numberOfUsers) {
		super();
		this.id = id;
		this.signUpPackage = com.n4systems.model.signuppackage.SignUpPackage.valueOf(name);
		this.priceInDollars = priceInDollars;
		this.preferred = preferred;
	}

	public com.n4systems.model.signuppackage.SignUpPackage getSignUpPackage() {
		return signUpPackage;
	}
	
	public String getName() {
		return signUpPackage.getName();
	}

	public int getPriceInDollars() {
		return priceInDollars;
	}

	public boolean isPreferred() {
		return preferred;
	}

	public Long getNumberOfUsers() {
		return signUpPackage.getUsers();
	}

	public Long getId() {
		return id;
	}

}
