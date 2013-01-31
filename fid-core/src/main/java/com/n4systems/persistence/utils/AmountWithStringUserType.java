package com.n4systems.persistence.utils;

import com.n4systems.model.common.AmountWithString;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.jscience.physics.amount.Amount;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

// save the string, the amount & the unit.
public class AmountWithStringUserType implements CompositeUserType, Serializable {

    private static final String[] PROPERTY_NAMES = new String[]{"text", "value", "unit"};

    private static final Type[] TYPES = new Type[]{
            StandardBasicTypes.STRING,
            StandardBasicTypes.DOUBLE,
            StandardBasicTypes.STRING};

    private static final int TEXT_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    private static final int UNIT_INDEX = 2;

    public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException {
        return (Serializable) value;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        return x.equals(y);
    }

    public String[] getPropertyNames() {
        return PROPERTY_NAMES;
    }

    public Type[] getPropertyTypes() {
        return TYPES;
    }

    public Object getPropertyValue(Object component, int property) throws HibernateException {
        AmountWithString amountWithString = (AmountWithString) component;
        if (property == TEXT_INDEX) {
            return amountWithString.getText();
        } else if (property == VALUE_INDEX) {
            double value = amountWithString.getAmount().isExact() ? (double) amountWithString.getAmount().getExactValue() : amountWithString.getAmount().getEstimatedValue();
            return value;
        } else if (property == UNIT_INDEX) {
            return amountWithString.getAmount().getUnit();
        }
        throw new IllegalArgumentException("attempt to access invalid property" + property + " in class " + amountWithString.getClass().getSimpleName());
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public boolean isMutable() {
        return false;  // TODO : should this be true???
    }

    public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner)
            throws HibernateException, SQLException {
        if (resultSet == null) {
            return null;
        }
        AmountWithString amountWithString = new AmountWithString();
        String text = resultSet.getString(names[TEXT_INDEX]);
        Double value = resultSet.getDouble(names[VALUE_INDEX]);
        String unit = resultSet.getString(names[UNIT_INDEX]);

        if (unit!=null && text!=null) {
            amountWithString.setText(text);
            amountWithString.setAmount(Amount.valueOf(value, getUnit(unit)));
        }
        return amountWithString;
    }

    private Unit<? extends Quantity> getUnit(String unit) {
        return unit == null ? null : Unit.valueOf(unit);
    }

    public void nullSafeSet(PreparedStatement statement, Object value, int index, SessionImplementor session)
            throws HibernateException, SQLException {
        if (value == null) {
            statement.setNull(index, StandardBasicTypes.STRING.sqlType());
            statement.setNull(index + 1, StandardBasicTypes.DOUBLE.sqlType());
            statement.setNull(index + 2, StandardBasicTypes.STRING.sqlType());
            return;
        }
        AmountWithString amountWithString = (AmountWithString) value;
        statement.setString(index, amountWithString.getText());
        statement.setDouble(index + 1, getValue(amountWithString.getAmount()));
        statement.setString(index + 2, amountWithString.getAmount().getUnit().toString());
    }

    private double getValue(Amount amount) {
        return amount.isExact() ? (double) amount.getExactValue() : amount.getEstimatedValue();
    }

    private Timestamp asTimeStamp(DateTime time) {
        return new Timestamp(time.getMillis());
    }

    public Object replace(Object original, Object target, SessionImplementor session, Object owner)
            throws HibernateException {
        return original;
    }

    public Class returnedClass() {
        return Interval.class;
    }

    public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
        throw new UnsupportedOperationException("Immutable Interval");
    }

}

