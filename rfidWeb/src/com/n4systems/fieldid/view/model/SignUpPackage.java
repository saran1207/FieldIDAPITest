package com.n4systems.fieldid.view.model;

public class SignUpPackage {
	private Long id;
	private String name;
	private int priceInDollars;
	private boolean preferred;
	private Long numberOfUsers;
	
	
	public SignUpPackage(Long id, String name, int priceInDollars, boolean preferred, Long numberOfUsers) {
		super();
		this.id = id;
		this.name = name;
		this.priceInDollars = priceInDollars;
		this.preferred = preferred;
		this.numberOfUsers = numberOfUsers;
	}

	public String getName() {
		return name;
	}

	public int getPriceInDollars() {
		return priceInDollars;
	}

	public boolean isPreferred() {
		return preferred;
	}

	public Long getNumberOfUsers() {
		return numberOfUsers;
	}

	public Long getId() {
		return id;
	}

}
