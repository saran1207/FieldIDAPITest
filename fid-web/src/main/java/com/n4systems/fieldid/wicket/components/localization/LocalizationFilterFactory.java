package com.n4systems.fieldid.wicket.components.localization;

import com.google.common.base.Predicate;
import com.n4systems.model.CriteriaResult;

import java.lang.reflect.Field;

public class LocalizationFilterFactory {




    public Predicate make(Class<?> entityClass) {
        return new EventTypeFilter();
    }



    class EventTypeFilter implements Predicate<Field> {

        @Override
        public boolean apply(Field field) {
             return !(field.getDeclaringClass().isAssignableFrom(CriteriaResult.class) && field.getName().equals("recommendations") ||
                     field.getDeclaringClass().isAssignableFrom(CriteriaResult.class) && field.getName().equals("deficiencies"));
        }

    }









}
