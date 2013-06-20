package com.n4systems.services.search;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.lang.WordUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class SearchResult implements Serializable {

    // TODO : get this from tenant settings.
    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("MMMM d,yyyy");

    private Map<String,ResultValue> results = Maps.newHashMap();

    public SearchResult(Document doc, FieldIdHighlighter highlighter, Analyzer analyzer) {
        for (IndexableField field:doc.getFields()) {
            results.put(field.name(), new ResultValue(field, highlighter, analyzer));
        }
    }

    public String get(String key) {
        ResultValue value = results.get(key);
        return value!=null ? value.getValue() : null;
    }

    public Long getLong(String key) {
        return results.get(key).getLongValue();
    }

    public boolean isHighlighted(String key) {
        ResultValue value = results.get(key);
        return value!=null ? value.isHighlighted() : false;
    }

    public String getKeyValueStringCapitalized(String key) {
        String value = results.get(key).getValue();
        if (value!=null) {
            return WordUtils.capitalize(key) + "=" + value;
        }
        return null;

    }

    public String getKeyValueString(String key) {
        // TODO : pass in format string??
        String value = results.get(key).getValue();
        if (value!=null) {
            return key + "=" + value;
        }
        return null;
    }

    public Set<String> getFields() {
        return results.keySet();
    }

    // -----------------------------------------------------------------------------------------------------

    class ResultValue implements Serializable {
        private String originalValue;
        private String highlightedValue;
        private String dateValue;
        private Long longValue;
        private Number numberValue;

        public ResultValue(IndexableField field, FieldIdHighlighter highlighter, Analyzer analyzer) {
            this.originalValue = field.stringValue();
            initValues(field);
            maybeHighlight(field, highlighter, analyzer);
        }

        private void maybeHighlight(IndexableField field, FieldIdHighlighter highlighter, Analyzer analyzer) {
            if (highlighter==null) {
                return;
            }

            try {
                if (dateValue!=null || numberValue!=null && field.numericValue()!=null) {
                    // TODO : need to have field Id SearchQuery object
                    highlightedValue = highlighter.getBestNumericFragment(field.name(), numberValue, getMostAppropriateValue() );
                } else {
                    highlightedValue = highlighter.getBestTextFragment(analyzer, field.name(), field.stringValue());
                }
            } catch (IOException e) {
                ;  // do nothing...just leave it unhighlighted.
            } catch (InvalidTokenOffsetsException e) {
                ;// do nothing...just leave it unhighlighted.
            }
        }

        private void initValues(IndexableField field) {
            try {
                numberValue = field.numericValue();
                longValue = numberValue==null ? null : numberValue.longValue();
                dateValue = getDateTime(numberValue);
            } catch (NumberFormatException e) {} // not a date or long.  that's ok.
        }

        private String getMostAppropriateValue() {
            if (dateValue!=null) {
                return dateValue;
            } else if (longValue!=null) {
                return longValue+"";
            } else {
                return originalValue;
            }
        }

        public String getValue() {
            return (isHighlighted()) ? highlightedValue : getMostAppropriateValue();
        }

        private String getDateTime(Number value) {
            if (value==null) {
                return null;
            }
            DateTime dateTime = new DateTime(value.longValue());
            if (dateTime.getYear()<2000 || dateTime.getYear()>2050) {   //arbitrarily picked 2000 as cutoff point.   we don't want "1970" type dates sneaking through.
                return null;    // it's a number but not a valid year...
            }
            return fmt.print(dateTime);
        }

        private boolean isHighlighted() {
            return highlightedValue != null;
        }

        public Long getLongValue() {
            Preconditions.checkState(numberValue!=null,"trying to get numeric value from non-numeric string '" + originalValue + "'");
            return numberValue.longValue();
        }

    }



}
