package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

import java.io.Serializable;

public interface ClauseArgument extends Serializable {
	public String getClause(FromTable table) throws InvalidQueryException;
}
