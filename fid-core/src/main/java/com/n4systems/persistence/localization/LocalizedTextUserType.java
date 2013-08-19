package com.n4systems.persistence.localization;

import org.hibernate.HibernateException;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Locale;

public class LocalizedTextUserType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.BIGINT};
    }

    @Override
    public Class returnedClass() {
        return String.class;
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
        // e.g. code = 1254
        //      language = FRENCH
        //      value = COULEUR
        Long code = StandardBasicTypes.LONG.nullSafeGet(rs, names[0]);
        // TODO DD : add spring filter to set this variable.
        Locale locale = LocaleContextHolder.getLocale();
        // this MUST be cached!!!
        return LocalizedTextCache.getText(code, locale);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
        System.out.println(value);
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public boolean isMutable() {
        return false;
    }

}
