package com.n4systems.services.search.parser;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.search.AssetIndexField;
import com.n4systems.util.collections.PrioritizedList;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// CAVEAT : it is assumed that analyzer/indexer service forces data to lowercase.
//  .: when generating queries all terms are lowercase.

public class SearchParserService extends FieldIdService {

    private static final Logger logger=Logger.getLogger("SearchLog");

    private Pattern operatorPattern = Pattern.compile("([<=>:]|>=|<=|<>|!=)");

    private @Autowired SimpleParser searchParser;
    private @Autowired AssetTypeService assetTypeService;
    private @Autowired CharArraySet stopWords;

    public SearchQuery createSearchQuery(String search) {
        try {
            SearchQuery searchQuery = parseSearchQuery(search);
            return searchQuery.addSuggestion(makeSuggestions(search, searchQuery));
        } catch (ParseException e) {
            logger.error("can't parse search query [" + search + "]");
        } catch (TokenMgrError te) {
            logger.error("can't parse search query [" + search + "]");
        }
//        // TODO DD : what is proper approach here for handling non-parseable strings???
//        // for now, just search everywhere for whatever they typed in.
        // maybe i should break "search" into words and add separate terms for each?
        SearchQuery searchQuery = new SearchQuery().add(new QueryTerm(null, QueryTerm.Operator.EQ, new SimpleValue(search)));
        return searchQuery.addSuggestion(makeSimpleSuggestion(search));
    }

    private List<String> makeSimpleSuggestion(String search) {
        // TODO DD : should actually look for a spot to add matching quote instead of removing one.
        if (StringUtils.countMatches(search,"'")%2!=0) {
            return Lists.newArrayList(search.replace("'",""));
        }
        if (StringUtils.countMatches(search,"\"")%2!=0) {
            return Lists.newArrayList(search.replace("\"",""));
        }
        // TODO : look for unbalanced quotes, invalid dates, and whatever else might be common...called only if parser throws exception?
        return Lists.newArrayList();
    }

    private List<String> makeSuggestions(String search, SearchQuery searchQuery) {
        List<String> suggestions = Lists.newArrayList();
        String suggestion = null;
        if ((suggestion = multiWordAttribute(search))!=null) {
            suggestions.add(suggestion);
        }
        if ((suggestion = multiWordNoAttribute(search))!=null) {
            suggestions.add(suggestion);
        }
        if (suggestions.size()==0) {
            suggestions.addAll(incorrectAttributes(search, searchQuery));
        }
        return suggestions;
    }

    private List<String> incorrectAttributes(String search, SearchQuery searchQuery) {
        List<String> result = Lists.newArrayList();
        for (QueryTerm term:searchQuery.getQueryTerms()) {
            String attribute = term.getAttribute();
            if (AssetIndexField.fromString(attribute)!=null) {
                continue;
            }
            List<String> suggestions = getAttributesLike(attribute);
            if (!suggestions.isEmpty() && !suggestions.get(0).equalsIgnoreCase(attribute) ) {    // it exists? if not, get the next suggestion.  (perfect match will always be first in list).
                for (String suggestion:suggestions) {
                    result.add(search.replaceAll("(?i)"+attribute, suggestion));
                }
                return result;
            }
        }
        return Lists.newArrayList();
    }

    private List<String> getAttributesLike(final String name) {
        if (StringUtils.isBlank(name)) {
            return Lists.newArrayList();
        }
        List<String> all = assetTypeService.getInfoFieldBeans(securityContext.getUserSecurityFilter().getUser().getTenant());
        // workaround : some users have created attribute names with actual semi-colons on the end.
        //  e.g. "SIZE:"    we'll try to accommodate for this.  (recall: parser will not understand that and discard semi-colon).
        final String normalizedName = name.endsWith(":") ? name.toLowerCase().substring(0, name.length() - 1) : name.toLowerCase();
        final String shortForm = normalizedName.substring(0,Math.min(normalizedName.length(),3));

        List<String> filteredList = ImmutableList.copyOf(Iterables.filter(all, new Predicate<String>() {
                @Override public boolean apply(String input) {
                    return input != null && input.indexOf(shortForm) >= 0;
                }
            }
        ));

        // TODO DD : this should all be replaced with a lucene analyzed index of attribute names.

        return new PrioritizedList<String>(filteredList,5, new Comparator<String>() {
            public @Override int compare(String o1, String o2) {
                if (o1.equalsIgnoreCase(normalizedName)) {
                    return -1;
                } else if (o2.equalsIgnoreCase(normalizedName)) {
                    return 1;
                }
                int index1 = o1.indexOf(shortForm);
                int index2 = o2.indexOf(shortForm);
                if (index1==-1 && index2==-1) {
                    return o1.compareTo(o2);
                }
                if (index1==-1) {
                    return 1;
                } else if (index2==-1) {
                    return -1;
                } else {
                    int r = index1 - index2;
                    return r==0 ? o1.compareTo(o2) : r;
                }
            }
        });

    }

    private String multiWordNoAttribute(String search) {
        // e.g. if user types in {hello there}  which will search for two values, hello & there.
        // instead they mean search for one attribute that has the two word string "hello there".
        String s = search.trim();
        if (s.startsWith("'") || s.startsWith("\"")) {
            return null;  // already quoted. skip it.
        }
        Matcher matcher = operatorPattern.matcher(s);
        if (!matcher.find() && search.indexOf(" ")!=-1) {
            return String.format("'%s'", search);
        }
        return null;
    }

    private String multiWordAttribute(String search) {
        Matcher matcher = operatorPattern.matcher(search);
        if (matcher.find()) {
            String before = search.substring(0,matcher.start()).trim();
            String after = search.substring(matcher.end());
            String operator  = search.substring(matcher.start(),matcher.end());
            if (before.indexOf(" ")!=-1) {
                before = "'" + before  + "'";
                int end = matcher.end();
                if (!matcher.find() && after.indexOf(" ")!=-1) {  // if following operators don't try to fix...too confusing/difficult.
                    after = "'" + search.substring(end) + "'";
                }
                return before + operator + after;
            }
        }
        return null;
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
        if (term.getAttribute()==null) {
            return getAllQueryForTerm(term);
        }
        return getQueryForTerm(term.getAttribute(), term.getValue(), term.getOperator());
    }

    private Query getAllQueryForTerm(QueryTerm term) {
        return getQueryForTerm(AssetIndexField.ALL.getField(), term.getValue(), QueryTerm.Operator.EQ);
    }

    private Query getQueryForTerm(String attribute, Value value, QueryTerm.Operator operator) {
        if (value instanceof RangeValue) {
            return getRangeQueryForTerm(attribute, (RangeValue) value);
        } else if (value instanceof ListValue) {
            return getListQueryForTerm(attribute,(ListValue)value);
        } else {
            Preconditions.checkState(value instanceof SimpleValue, "type of value not supported " + value.getClass().getSimpleName());
            return getSimpleQueryForTerm(attribute, operator, (SimpleValue)value);
        }
    }

    // TODO DD : add custom scoring for date/numeric fields.
    private Query getSimpleQueryForTerm(String attribute, QueryTerm.Operator operator, SimpleValue value) {
        if (value.isNumber()) {
            NumericRangeInfo rangeInfo = new NumericRangeInfo(value.getNumber(), operator);
            return NumericRangeQuery.newDoubleRange(attribute, rangeInfo.getMinDouble(), rangeInfo.getMaxDouble(), rangeInfo.includeMin, rangeInfo.includeMax);
        } else if (value.isDate()) {
            DateRangeInfo rangeInfo = new DateRangeInfo(value.getDateRange(), operator);
            return NumericRangeQuery.newLongRange(attribute, rangeInfo.getMinLong(), rangeInfo.getMaxLong(), rangeInfo.includeMin, rangeInfo.includeMax);
        } else if (value.isString()) {
            if (operator.equals(QueryTerm.Operator.EQ)) {
                return getPhraseQueryForTerm(attribute, value);
            } else {
                StringRangeInfo rangeInfo = new StringRangeInfo(value.getString(), operator);
                return TermRangeQuery.newStringRange(attribute, rangeInfo.min, rangeInfo.max, rangeInfo.includeMin, rangeInfo.includeMax);
            }
        }
        return null;
    }

    private Query getPhraseQueryForTerm(String attribute, SimpleValue value) {
        // TODO DD : is it better to return a single termQuery if there is only one string value??
        PhraseQuery query = new PhraseQuery();
        StringTokenizer stringTokenizer = new StringTokenizer(value.getString());
        while (stringTokenizer.hasMoreTokens()) {
            String word = stringTokenizer.nextToken();
            if (!isCommonWord(word)) {
                query.add(new Term(attribute, word));
            }
        }
        query.setSlop(6);
        return query;
    }

    private boolean isCommonWord(String word) {
        return stopWords.contains(word);
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
            return TermRangeQuery.newStringRange(attribute,value.getFrom().getString(), value.getTo().getString(), true, true);
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


    // -------------------------------------------------------------------------------

    class RangeInfo<T> {
        protected boolean includeMin = false;
        protected boolean includeMax = false;
        protected T min=null;
        protected T max=null;
        protected BooleanClause.Occur occur = BooleanClause.Occur.MUST;
        protected final QueryTerm.Operator operator;
        protected T low;
        private T high;

        public RangeInfo(T low, T high, QueryTerm.Operator operator) {
            this.low = low;
            this.high = high;
            this.operator = operator;
            process();
        }

        public RangeInfo(T value, QueryTerm.Operator operator) {
            this.low = this.high = value;
            this.operator = operator;
            process();
        }

        private void process() {
            switch (operator) {
                case GT:
                    includeMin = false;
                    min = high;
                    break;
                case LT:
                    max = low;
                    includeMax = false;
                    break;
                case GE:
                    min = high;
                    includeMin = true;
                    break;
                case LE:
                    max = low;
                    includeMax = true;
                    break;
                case EQ:
                    min = low;
                    max = high;
                    includeMin = includeMax = true;
                    break;
                case NE:
                    min = low;
                    max = high;
                    includeMin = includeMax = true;
                    occur = BooleanClause.Occur.MUST_NOT;
                    break;
            }
        }

    }


    class NumericRangeInfo extends RangeInfo<Number> {
        Number granularity=null;

        public NumericRangeInfo(Number low, Number high, QueryTerm.Operator operator) {
            super(low, high, operator);
        }

        public NumericRangeInfo(Number value, QueryTerm.Operator operator) {
            super(value, operator);
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


    class StringRangeInfo extends RangeInfo<String> {

        public StringRangeInfo(String value, QueryTerm.Operator operator) {
            super(value, operator);
        }
    }

    class DateRangeInfo extends NumericRangeInfo {

        // NOTE : we can assume all date ranges have granularity incorporated.
        // i.e. June 1,2012  --> June 1 to June 2,2012.       Today = July 2...July 3   etc...
        //  this means that we can NOT do queries on hourly or less basis.   can only search on day granularity.
        // you'll need to change the DateParser if this spec changes.
        public DateRangeInfo(DateRange dateRange, QueryTerm.Operator operator) {
            super(dateRange.getFrom().toDate().getTime(), dateRange.getTo().toDate().getTime(), operator);
        }
    }

}
