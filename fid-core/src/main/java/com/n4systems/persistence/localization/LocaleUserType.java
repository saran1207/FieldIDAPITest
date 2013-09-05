package com.n4systems.persistence.localization;

import org.hibernate.HibernateException;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Locale;

public class LocaleUserType implements UserType {


    @Override
    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

    @Override
    public Class returnedClass() {
        return Locale.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x==null ? y==null : x.equals(y);
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x==null ? Integer.MAX_VALUE : x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
        String value = StandardBasicTypes.STRING.nullSafeGet(rs, names[0]);
        return value== null ? null : StringUtils.parseLocaleString(value);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        Locale locale = (Locale) value;
        String localeString = locale!=null ? locale.toString() : null;
        StandardBasicTypes.STRING.nullSafeSet(st, localeString, index);
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public boolean isMutable() {
        return false;
    }


}
