package com.n4systems.services.brainforest;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class SearchQuery implements Serializable {

    public enum Conjunction {
        AND,OR;

        public static Conjunction fromString(String text) {
            text = text.toLowerCase();
            for (Conjunction j:values()) {
                if (j.name().toLowerCase().equals(text)) {
                    return j;
                }
            }
            return null;
        }
    };

    private List<QueryTerm> terms = Lists.newArrayList();
    private List<Conjunction> conjunctions = Lists.newArrayList();

    public SearchQuery add(QueryTerm term) {
        terms.add(term);
        return this;
    }

    public List<QueryTerm> getQueryTerms() {
        return terms;
    }

    public SearchQuery add(String conjunction) {
        // TODO : assert that you have n-1 conjuctions where n is # of terms.
        conjunctions.add(Conjunction.fromString(conjunction));
        return this;
    }

    public SearchQuery add(Conjunction conjunction) {
        conjunctions.add(conjunction);
        return this;
    }

    public Iterator<Conjunction> conjunctions() {
        Preconditions.checkState(conjunctions.size()==terms.size()-1,"somethings wrong with this query...need to have N-1 AND/OR conjunctions for N terms.");
        return conjunctions.iterator();
    }

    public int getNumberOfTerms() {
        return terms.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        QueryTerm term = terms.get(0);
        builder.append(term.toString());
        for (int i=1;i<terms.size();i++) {
            Conjunction conjunction = conjunctions.get(i-1);
            builder.append(" ").append(conjunction).append("  ");
            term = terms.get(i);
            builder.append(term.toString());
        }
        return builder.toString();
    }

}
