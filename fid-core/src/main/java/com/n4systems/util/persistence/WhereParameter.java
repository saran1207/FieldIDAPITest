package com.n4systems.util.persistence;

import javax.persistence.Query;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.util.BitField;

public class WhereParameter<T> implements WhereClause<T> {
	private static final long serialVersionUID = 1L;
	private static final String WILDCARD_CHAR = "%";
	public static final int WILDCARD_LEFT = (1 << 0);
	public static final int WILDCARD_RIGHT = (1 << 1);
	public static final int IGNORE_CASE = (1 << 2);
	public static final int TRIM = (1 << 3);
	public static final int WILDCARD_BOTH = WILDCARD_LEFT | WILDCARD_RIGHT;

	public enum Comparator {
		// TODO: add support for BETWEEN's
		EQ("="), NE("<>"), GT(">"), LT("<"), GE(">="), LE("<="), LIKE("LIKE"), NOTLIKE("NOT LIKE"), NULL("IS NULL"), NOTNULL("IS NOT NULL"), EMPTY("IS EMPTY"), NOTEMPTY("IS NOT EMPTY"), IN("IN"), NOTIN(
				"NOT IN"), EQ_OR_NULL("=", true);

		private String operator;
		private boolean combination;

		Comparator(String operator) {
			this(operator, false);
		}

		Comparator(String operator, boolean combination) {
			this.operator = operator;
			this.combination = combination;
		}

		public String getOperator() {
			return operator;
		}

		public boolean isCombination() {
			return combination;
		}
	}

	private String name;
	private String param;
	private T value;
	private Comparator comparator = Comparator.EQ;
	private ChainOp chainOp = ChainOp.AND;
	private int options = 0;
	private boolean dropAlias = false;
	private boolean literalParameter = false;
	private String key;

	public WhereParameter() {
	}

	public WhereParameter(Comparator comparator, String param) {
		this(comparator, param.replace('.', '_'), param, null, null, false);
	}

	public WhereParameter(Comparator comparator, String name, String param) {
		this(comparator, name, param, null, null, false);
	}

	public WhereParameter(Comparator comparator, String param, T value) {
		/*
		 * note that .'s are replaced with _'s for the parameter name. This is
		 * to handle cases for sub, object lookups (eg/ book.id ) since the
		 * where parameter name, is used as the bind variable name and a bind
		 * variable name cannot contain .'s.
		 * 
		 * eg/ .... WHERE some.field = :some.field ... is corrected to WHERE
		 * some.field = :some_field
		 */
		this(comparator, param.replace('.', '_'), param, value, null, false);
	}

	public WhereParameter(Comparator comparator, String name, String param, T value) {
		this(comparator, name, param, value, null, false);
	}

	public WhereParameter(Comparator comparator, String name, String param, T value, Integer options, boolean dropAlias) {
		this(comparator, name, param, value, options, dropAlias, null);
	}

	public WhereParameter(Comparator comparator, String name, String param, T value, Integer options, boolean dropAlias, ChainOp chainOp) {
		this.comparator = comparator;
		this.name = name;
		this.param = param;
		this.value = value;
		this.dropAlias = dropAlias;

		if (chainOp != null) {
			this.chainOp = chainOp;
		}

		if (options != null) {
			this.options = options;
		}
	}

	@Override
	public String getName() {
		// if name is null, use the param name (or null)
		return (name != null) ? name : (param != null) ? param.replace('.', '_') : null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public T getValue() {
		return value;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public Comparator getComparator() {
		return comparator;
	}

	@Override
	public ChainOp getChainOperator() {
		return chainOp;
	}

	public void setChainOperator(ChainOp chainOp) {
		this.chainOp = chainOp;
	}

	public int getOptions() {
		return options;
	}

	public void setOptions(int options) {
		this.options = options;
	}

	@Override
	public void bind(Query query) throws InvalidQueryException {
		if (literalParameter)
			return;

		switch (comparator) {
		case EQ:
		case NE:
		case GT:
		case LT:
		case GE:
		case LE:
		case LIKE:
		case NOTLIKE:
		case EQ_OR_NULL:
			// prepare the value when options have been set
			if (options > 0) {
				query.setParameter(getName(), prepareStringValue());
			} else {
				query.setParameter(getName(), value);
			}
			break;
		case IN:
		case NOTIN:
			query.setParameter(getName(), value);
			break;

		default:
			break;
		}
	}

	@Override
	public String getClause(FromTable table) throws InvalidQueryException {
		return clausePrefix() + getComparison(table) + clauseSuffix();
	}

	private String getComparison(FromTable table) {
		String comparison = prepareParam(table) + " " + comparator.getOperator();

		switch (comparator) {
		case EQ:
		case NE:
		case GT:
		case LT:
		case GE:
		case LE:
		case LIKE:
		case NOTLIKE:
			comparison += literalParameter ? getName() : " :" + getName();
			break;
		case IN:
		case NOTIN:
			comparison += "( :" + getName() + " ) ";
			break;
		case EQ_OR_NULL:
			comparison += " :" + getName() + " OR " + prepareParam(table) + " " + Comparator.NULL.getOperator();
		default:
			// null and empty ops have no right side
			break;
		}
		return comparison;
	}

	private String clausePrefix() {
		return (comparator.combination) ? "(" : "";
	}

	private String clauseSuffix() {
		return (comparator.combination) ? ")" : "";
	}

	private String prepareParam(FromTable table) {
		BitField bits = new BitField(options);
		String param = table.prepareField(this.param, dropAlias);

		if (bits.isSet(IGNORE_CASE)) {
			param = applyFunction("lower", param);
		}

		return param;
	}

	private String applyFunction(String fn, String param) {
		return fn + "(" + param + ")";
	}

	private String prepareStringValue() throws InvalidQueryException {
		BitField bits = new BitField(options);

		if (!(value instanceof String)) {
			throw new InvalidQueryException("Only values of type String can use options in a where clause");
		}

		String prepValue = (String) value;
		
		if (bits.isSet(TRIM)) {
			prepValue = prepValue.trim();
		}
		if (bits.isSet(IGNORE_CASE)) {
			prepValue = prepValue.toLowerCase();
		}

		if (bits.isSet(WILDCARD_LEFT)) {
			prepValue = WILDCARD_CHAR + prepValue;
		}

		if (bits.isSet(WILDCARD_RIGHT)) {
			prepValue = prepValue + WILDCARD_CHAR;
		}
		return prepValue;
	}

	public void setLiteralParameter(boolean literalParameter) {
		this.literalParameter = literalParameter;
	}

	@Override
	public String getKey() {
		if (key != null) {
			return key;
		}
		return getName();
	}

	public void setKey(String key) {
		this.key = key;
	}
}
