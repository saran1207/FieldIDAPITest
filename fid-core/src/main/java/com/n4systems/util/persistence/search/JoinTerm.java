package com.n4systems.util.persistence.search;

import com.n4systems.util.persistence.JoinClause;
import com.n4systems.util.persistence.JoinClause.JoinType;

import java.io.Serializable;

public class JoinTerm implements Serializable {
	public enum JoinTermType { LEFT, RIGHT, INNER }
	
	private final JoinTermType type;
	private final String path;
	private final String alias;
	private final boolean required;
	
	public JoinTerm(JoinTermType type, String path, String alias, boolean required) {
		this.type = type;
		this.path = path;
		this.alias = alias;
		this.required = required;
	}

	public JoinClause toJoinClause() {
		JoinType joinType = null;
		switch(type) {
			case LEFT:
				joinType = JoinType.LEFT;
				break;
			case RIGHT:
				joinType = JoinType.RIGHT;
				break;
			case INNER:
				joinType = JoinType.INNER;
				break;
		}
		return new JoinClause(joinType, path, alias, required);
	}
	
}
