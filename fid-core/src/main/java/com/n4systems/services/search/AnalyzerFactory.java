package com.n4systems.services.search;

import com.google.common.collect.Maps;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Reader;
import java.util.Map;

public class AnalyzerFactory {

    public static final Version FIELD_ID_LUCENE_VERSION = Version.LUCENE_43;

    public enum Type { STANDARD, KEYWORD, SIMPLE, WHITESPACE; }

    private @Autowired CharArraySet stopWordSet;

    private Type defaultType = Type.WHITESPACE;


    public Analyzer create(Type type) {
        switch (type) {
            case STANDARD:
                // Caveat : english stopwords only currently supported.
                return new StandardAnalyzer(FIELD_ID_LUCENE_VERSION, stopWordSet);
            case WHITESPACE:
                return new Analyzer() {
                    @Override
                    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
                        WhitespaceTokenizer tokenizer = new WhitespaceTokenizer(FIELD_ID_LUCENE_VERSION, reader);
                        LowerCaseFilter filter = new LowerCaseFilter(FIELD_ID_LUCENE_VERSION,tokenizer);
                        return new TokenStreamComponents(tokenizer,filter);
                    }
                };
            case SIMPLE:
                return new SimpleAnalyzer(FIELD_ID_LUCENE_VERSION);
            case KEYWORD:
                return new KeywordAnalyzer();
            default:
                throw new IllegalArgumentException("invalid analyzer type '" + type + "'");
        }
    }

    public Analyzer createAnalyzer(HasAnalyzerType[] fields) {
        Map<Type,Analyzer> analyzerTypes = Maps.newHashMap();
        for (Type type:Type.values()) {
            analyzerTypes.put(type, create(type));
        }
        Map<String, Analyzer> fieldAnalyzers = Maps.newHashMap();
        for (HasAnalyzerType field:fields) {
            Type type = field.getAnalyzerType();
            fieldAnalyzers.put(field.getField(),analyzerTypes.get(type));
        }
        return new PerFieldAnalyzerWrapper(analyzerTypes.get(defaultType), fieldAnalyzers);
    }

}

