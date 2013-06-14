package com.n4systems.services.search;

import com.google.common.collect.Maps;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.highlight.Highlighter;
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

    private Map<String,String> results = Maps.newHashMap();


    public SearchResult(Document doc) {
        for (IndexableField field:doc.getFields()) {
            results.put(field.name(), field.stringValue());
        }
    }

    public SearchResult(Document doc, Highlighter highlighter, Analyzer analyzer) {

        for (IndexableField field:doc.getFields()) {

            String result = "";
            try {
                result = highlighter.getBestFragment(analyzer, field.name(), field.stringValue());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidTokenOffsetsException e) {
                e.printStackTrace();
            }

            if (result != null && result.length()>0) {
                results.put(field.name(), result);
            } else {
                results.put(field.name(), field.stringValue());
            }


        }
    }

    public String get(String key) {
        // TODO : add formatting.
        DateTime dateTime = getDateTime(key);
        return dateTime==null ? results.get(key) : fmt.print(dateTime);
    }

    public String getString(String key) {
        return results.get(key);
    }

    public String getKeyValueString(String key) {
        // TODO : pass in format string??
        String value = results.get(key);
        if (value!=null) {
            return key + "=" + value;
        }
        return null;
    }

    public Long getLong(String key) {
        try {
            return Long.parseLong(getString(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public DateTime getDateTime(String key) {
        Long ms = getLong(key);
        if (ms==null) {
            return null;
        }
        DateTime dateTime = new DateTime(ms);
        if (dateTime.getYear()<2000 || dateTime.getYear()>2050) {   //arbitrarily picked 2000 as cutoff point.   we don't want "1970" type dates sneaking through.
            return null;    // it's a number but not a valid year...
        }
        return dateTime;
    }

    public Set<String> getFields() {
        return results.keySet();
    }
}
