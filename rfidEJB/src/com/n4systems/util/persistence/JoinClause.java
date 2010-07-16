package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

public class JoinClause implements ClauseArgument {
	private static final long serialVersionUID = 1L;
	
	public static JoinClause createFetchJoin(String param) {
		return new JoinClause(JoinType.FETCH, param, null, false);
	}
	
	public enum JoinType {
		LEFT("LEFT JOIN", true), 
		RIGHT("RIGHT JOIN", true), 
		FETCH("LEFT JOIN FETCH", false), 
		INNER("INNER JOIN", true);
		
		private String joinSql;
		private boolean usesAlias;
		
		JoinType(String joinSql, boolean usesAlias) {
			this.joinSql = joinSql;
			this.usesAlias = usesAlias;
		}
		
		public String getJoinSql() {
			return joinSql;
		}
		
		public boolean usesAlias() {
			return usesAlias;
		}
	};
	
	private final JoinType type;
	private final String param;
	private final String alias;
	private final boolean required;
	
	public JoinClause(JoinType type, String param, String alias, boolean required) {
		this.type = type;
		this.param = param;
		this.alias = alias;
		this.required = required;
	}

	public String getParam() {
		return param;
	}

	public JoinType getType() {
		return type;
	}

	public String getAlias() {
		return alias;
	}

	public boolean isRequired() {
		return required;
	}
	
	public String getClause(FromTable table) throws InvalidQueryException {
		if(type == null || param == null || param.length() < 1) {
			throw new InvalidQueryException("The fetch parameter and type and requried for a FetchClause");
		}
		
		String clause = type.getJoinSql() + " " + table.prepareField(param);
		if(type.usesAlias() && alias != null) {
			clause += " " + alias;
		}
		
		return clause;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof JoinClause) {
			JoinClause target = (JoinClause)obj;
			
			if(target.getParam().equals(param) && target.getType().equals(type)) {
				return true;
			}
		}
		
		return false;
	}
	
	
}
