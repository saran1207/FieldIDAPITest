package com.n4systems.fieldid.service.search;

import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.persistence.search.terms.SimpleTerm;
import com.n4systems.util.persistence.search.terms.WildcardTerm;

public class SimpleOrWildcardTermFactory {

    public static SearchTermDefiner createSimpleOrWildcardTerm(String field, String value, boolean dropAlias) {
        String valueString = StringUtils.clean(value);

        SimpleTerm<String> searchTerm = null;

        if (valueString != null && !"*".equals(value)) {
            if (isWildcard(valueString)) {
                searchTerm = new WildcardTerm(field, valueString);
            } else {
                searchTerm = new SimpleTerm<String>(field, valueString);
            }
            searchTerm.setDropAlias(dropAlias);
        }

        return searchTerm;
    }

    protected static boolean isWildcard(String value) {
        return value.startsWith("*") || value.endsWith("*");
    }

}
