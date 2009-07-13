package com.n4systems.util.persistence;

import java.io.Serializable;

import com.n4systems.exceptions.InvalidQueryException;

public interface ClauseArgument extends Serializable {
	public String getClause(FromTable table) throws InvalidQueryException;
}
