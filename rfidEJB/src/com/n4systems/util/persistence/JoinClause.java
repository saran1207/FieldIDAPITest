package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

public class JoinClause implements ClauseArgument {
	private static final long serialVersionUID = 1L;
	
	public enum JoinType {
		LEFT("LEFT JOIN", true), RIGHT("RIGHT JOIN", true), FETCH("LEFT JOIN FETCH", false), INNER( "INNER JOIN", true ),
		REQUIRED_LEFT("LEFT JOIN",true);
		
	
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
	
	private JoinType type;
	private String param;
	private String alias;
	
	public JoinClause() {}
	
	public JoinClause(String param) {
		this(JoinType.FETCH, param, null);
	}
	
	public JoinClause(String param, String alias) {
		this(JoinType.LEFT, param, alias);
	}
	
	public JoinClause(JoinType type, String param, String alias) {
		this.type = type;
		this.param = param;
		this.alias = alias;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public JoinType getType() {
		return type;
	}

	public void setType(JoinType type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
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
