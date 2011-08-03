package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

public class OrderClause implements ClauseArgument {
	private static final long serialVersionUID = 1L;
	
	private String param;
	private boolean ascending = true;
    private boolean alwaysDropAlias = false;
    private String fieldAfterAlias;
	
	public OrderClause() {}
	
	public OrderClause(String param, boolean ascending) {
		this.param = param;
		this.ascending = ascending;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	public String getClause(FromTable table) throws InvalidQueryException {
		if(param == null) {
			throw new InvalidQueryException("Parameter is requried for an order by clause");
		}
		
		String direction = "ASC";
		if(!ascending) {
			direction = "DESC";
		}

        if (alwaysDropAlias) {
            String alias = table.prepareField(param, true);
            if (fieldAfterAlias != null) {
                alias += "." + fieldAfterAlias;
            }
            return alias + " " + direction;
        } else {
            return table.prepareField(param) + " " + direction;
        }
	}

	@Override
	public int hashCode() {
		return param.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderClause other = (OrderClause) obj;
		if (ascending != other.ascending)
			return false;
		if (param == null) {
			if (other.param != null)
				return false;
		} else if (!param.equals(other.param))
			return false;
		return true;
	}

    public void setAlwaysDropAlias(boolean alwaysDropAlias) {
        this.alwaysDropAlias = alwaysDropAlias;
    }

    public void setFieldAfterAlias(String fieldAfterAlias) {
        this.fieldAfterAlias = fieldAfterAlias;
    }
}
