package com.n4systems.services.brainforest;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.FieldIdService;
import com.n4systems.services.SecurityContext;
import com.n4systems.services.search.AssetIndexField;
import org.apache.log4j.Logger;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.util.BytesRef;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;


public class SearchParserService extends FieldIdService {

    private static final Logger logger=Logger.getLogger("SearchLog");

    private @Autowired SecurityContext securityContext;
    private @Autowired SimpleParser searchParser;



    public SearchQuery createSearchQuery(String search) {
        try {
            return parseSearchQuery(search);
        } catch (ParseException e) {
            logger.error("can't parse search query [" + search + "]");
        } catch (TokenMgrError te) {
            logger.error("can't parse search query [" + search + "]");
        }
//        // TODO DD : what is proper approach here for handling non-parseable strings???
//        // for now, just search everywhere for whatever they typed in.
        // maybe i should break "search" into words and add separate terms for each?
        return new SearchQuery().add(new QueryTerm(null, QueryTerm.Operator.EQ,  new SimpleValue(search)));
    }


    private SearchQuery parseSearchQuery(String search) throws ParseException,TokenMgrError {
        return searchParser.parseQuery(search);
    }

    public Query convertToLuceneQuery(SearchQuery searchQuery) {
        if (searchQuery.getNumberOfTerms()==0) {
            return null;
        }
        BooleanQuery booleanQuery = new BooleanQuery();
        Iterator<SearchQuery.Conjunction> conjunctions = searchQuery.conjunctions();
        Iterator<QueryTerm> terms = searchQuery.getQueryTerms().iterator();

        QueryTerm term = terms.next();// CAVEAT : assumes that there is at least one term.
        booleanQuery.add(getQueryForTerm(term), BooleanClause.Occur.MUST);

        while (conjunctions.hasNext()) {
            term = terms.next();
            booleanQuery.add(getQueryForTerm(term), getBooleanOccur(conjunctions.next()));
        }
        return booleanQuery;
    }

    private Query getQueryForTerm(QueryTerm term) {
        String attribute = term.getAttribute()!=null ? term.getAttribute().toLowerCase() : AssetIndexField.ALL.getField();
        Value value = term.getValue();
        return getQueryForTerm(attribute, value, term.getOperator());
    }

    private Query getQueryForTerm(String attribute, Value value, QueryTerm.Operator operator) {
        if (value instanceof RangeValue) {
            return getRangeQueryForTerm(attribute, (RangeValue)value);
        } else if (value instanceof ListValue) {
            return getListQueryForTerm(attribute,(ListValue)value);
        } else {
            Preconditions.checkState(value instanceof SimpleValue, "type of value not supported " + value.getClass().getSimpleName());
            return getSimpleQueryForTerm(attribute, operator, (SimpleValue)value);
        }
    }

    private Query getSimpleQueryForTerm(String attribute, QueryTerm.Operator operator, SimpleValue value) {
        if (value.isNumber()) {
            NumberRangeInfo rangeInfo = new NumberRangeInfo(value.getNumber(), operator);
            if (rangeInfo.type==Long.class) {
                return NumericRangeQuery.newLongRange(attribute,rangeInfo.getMinLong(), rangeInfo.getMaxLong(), rangeInfo.includeMin, rangeInfo.includeMax);
            } else if (rangeInfo.type==Double.class) {
                return NumericRangeQuery.newDoubleRange(attribute, rangeInfo.getMinDouble(), rangeInfo.getMaxDouble(), rangeInfo.includeMin, rangeInfo.includeMax);
            }
        } else if (value.isDate()) {
            DateTime date = value.getDate();
            NumberRangeInfo rangeInfo = new NumberRangeInfo(date.toDate().getTime(), operator).withGranularity(TimeUnit.DAYS.toMillis(1));
            return NumericRangeQuery.newLongRange(attribute, rangeInfo.getMinLong(), rangeInfo.getMaxLong(), rangeInfo.includeMin, rangeInfo.includeMax);
        } else if (value.isString()) {
            return getPhraseQueryForTerm(attribute, value);
        }
        return null;
    }

    private Query getPhraseQueryForTerm(String attribute, SimpleValue value) {
        // TODO DD : is it better to return a single termQuery if there is only one string value??
        PhraseQuery query = new PhraseQuery();
        StringTokenizer stringTokenizer = new StringTokenizer(value.getString());
        while (stringTokenizer.hasMoreTokens()) {
            query.add(new Term(attribute,stringTokenizer.nextToken().toLowerCase()));
        }
        return query;
    }

    private Query getListQueryForTerm(String attribute, ListValue value) {
        BooleanQuery query = new BooleanQuery();
        List<Value> values = value.getValues();
        // what about date values/numeric values...need to handle them here.
        for (Value v:values) {
            query.add(getQueryForTerm(attribute, v, QueryTerm.Operator.EQ), BooleanClause.Occur.SHOULD);
        }
        return query;
    }

    private Query getRangeQueryForTerm(String attribute, RangeValue value) {
        if (value.isNumber()) {
            double from = value.getFrom().getNumber().doubleValue();
            double to = value.getTo().getNumber().doubleValue();
            return NumericRangeQuery.newDoubleRange(attribute, from, to, true, true);
        } else if (value.isDate()) {
            long from = value.getFrom().getDate().toDate().getTime();
            long to = value.getTo().getDate().toDate().getTime();
            return NumericRangeQuery.newLongRange(attribute, from, to, true, false);
        } else if (value.isString()) {
            return new TermRangeQuery(attribute, new BytesRef(value.getFrom().getString()), new BytesRef(value.getTo().getString()), true, true);
        }
        return null;
    }

    private BooleanClause.Occur getBooleanOccur(SearchQuery.Conjunction conjunction) {
        switch (conjunction) {
            case OR:
                return BooleanClause.Occur.SHOULD;
            default:
            case AND:
                return BooleanClause.Occur.MUST;
        }
    }

    class NumberRangeInfo {
        boolean includeMin = false;
        boolean includeMax = false;
        Number min=null;
        Number max=null;
        BooleanClause.Occur occur = BooleanClause.Occur.MUST;
        Class<? extends Number> type;
        Number granularity=0.0;

        public NumberRangeInfo(Number number, QueryTerm.Operator operator) {
            type = number.getClass();
            double value = number.doubleValue();
            switch (operator) {
                case GT:
                    includeMin = false;
                    min = value;
                    break;
                case LT:
                    max = value;
                    includeMax = false;
                    break;
                case GE:
                    min = value;
                    includeMin = true;
                    break;
                case LE:
                    max = value;
                    includeMax = true;
                    break;
                case EQ:
                    min = value;
                    max = new Double(min.doubleValue() + granularity.doubleValue());
                    includeMin = includeMax = true;
                    break;
                case NE:
                    min = max = value;
                    includeMin = includeMax = true;
                    occur = BooleanClause.Occur.MUST_NOT;
                    break;
            }
        }

        public NumberRangeInfo withGranularity(Number granularity){
            this.granularity = granularity;
            return this;
        }

        public Long getMinLong() {
            return min == null ? null : min.longValue();
        }
        public Long getMaxLong() {
            return max == null ? null : max.longValue();
        }
        public Double getMinDouble() {
            return min == null ? null : min.doubleValue();
        }
        public Double getMaxDouble() {
            return max == null ? null : max.doubleValue();
        }
    }


}

