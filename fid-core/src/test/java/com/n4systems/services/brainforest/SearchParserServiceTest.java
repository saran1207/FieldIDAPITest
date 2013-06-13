package com.n4systems.services.brainforest;

import com.n4systems.fieldid.junit.FieldIdServiceTest;
import com.n4systems.services.SecurityContext;
import com.n4systems.test.TestMock;
import com.n4systems.test.TestTarget;
import org.apache.lucene.search.Query;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

public class SearchParserServiceTest extends FieldIdServiceTest {

    @TestTarget private SearchParserService searchParserService;

    @TestMock private SecurityContext securityContext;
    @TestMock private SimpleParser searchParser;

    @Test
    public void testSearch() throws Exception {
        expect(securityContext.getUserSecurityFilter()).andReturn(null);
        replay(securityContext);
        SearchQuery searchQuery = new SearchQuery().add(new QueryTerm("x", QueryTerm.Operator.EQ,new SimpleValue("100")));
        expect(searchParser.parseQuery("x=100")).andReturn(searchQuery);
        replay(searchParser);

        Query query = searchParserService.createSearchQuery("x=100");

        // TODO DD : assertions on this stuff....
        // check for lowercase, proper use of phraseTerms, and all that jazz.
    }


    static protected class ReaderMatcher implements IArgumentMatcher {


        private final String source;

        public ReaderMatcher(String source) {
            this.source = source;
        }

        public static Reader eq(String source) {
            EasyMock.reportMatcher(new ReaderMatcher(source));
            return null;
        }

        @Override
        public void appendTo(StringBuffer buffer) {
            buffer.append("'").append(source).append("'");
        }

        @Override
        public boolean matches(Object argument) {
             char[] buff = new char[1000];
            if (!(argument instanceof StringReader) ) {
                return false;
            }
            StringReader actual = (StringReader) argument;
            try {
                actual.reset();
                int read = actual.read(buff);
                return source.equals(new String(buff,0,read));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }

    }





}

