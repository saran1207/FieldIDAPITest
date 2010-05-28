package com.n4systems.commands;

import java.util.UUID;

import org.apache.commons.lang.builder.EqualsBuilder;

public class SystemMessage {

	private final UUID identifier;
	
	public SystemMessage() {
		super();
		this.identifier = UUID.randomUUID();
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
