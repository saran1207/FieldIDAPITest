package com.n4systems.services.brainforest;

public class scrap {

/**
        public List<SearchResultsRecord> search(String term) throws InstantiationException, IllegalAccessException {
            SimpleParser parser = new SimpleParser(new StringReader(term));  // make immutable. will parse on construction.
            SearchHandler searchHandler = getSearchHandler(parser);
            return searchHandler.search(parser);
        }

        // scans search term...is it asset search, event search, user search, location search etc....
        private SearchHandler getSearchHandler(SimpleParser parser) throws IllegalAccessException, InstantiationException {
            Map<QueryTermType, Long> types = new HashMap<QueryTermType,Long>();
            List<QueryTerm> queryTerms = parser.getQueryTerms();
            int unknowns = 0;
            for (QueryTerm queryTerm : queryTerms) {
                QueryTermType type = getQueryTermTypeOf(queryTerm.getAttribute());
                // ignore UNKNOWN.  they are like wildcards and can apply to any search type.
                if (!type.equals(QueryTermType.UNKNOWN)) {
                    Long count = types.get(types);
                    types.put(type,count==null ? 1 : count+1);
                } else {
                    unknowns++;
                }
            }
            // check to see if there is a common type?  or if all UNKNOWN?
            if (types.size()>1) {
                ///hmmm...what to do.  where should i search.  throw exception....?
                return null;
            } else if (types.size()==0) {
                // either parser has no terms or they are all of unknown type.  e.g. foo=bar.
                // (as opposed to   status=bar).
                return new EveryWhereSearchHandler();  // need to scan existent table for all attribute names.
            } else {
                QueryTermType type = types.keySet().iterator().next();
                return type.getSearchHandler().newInstance();
            }

            // need a "did you mean".

        }

        private QueryTermType getQueryTermTypeOf(String attribute) {
            PredefinedAttribute predefinedAttribute = PredefinedAttribute.valueOf(attribute);
            if (predefinedAttribute==null) {
                return QueryTermType.UNKNOWN;
            }
            return predefinedAttribute.getType();
        }


        class SearchResultsRecord {
            //select id, type, attribute, value, displayText;
        }


        interface SearchHandler {
            List<SearchResultsRecord> search(SimpleParser parser);
        }

        class AssetSearchHandler implements SearchHandler {
            private SimpleParser parser;

            public AssetSearchHandler(SimpleParser parser) {
                this.parser = parser;
            }

            @Override
            public List<SearchResultsRecord> search(SimpleParser parser) {
                // select * from asset_attribute_view
                //  join assets asset on aav.id=asset.id where (a=b and foo=bar) or (asdf=sdd) etc...
                // asset.text contains "blah" and asset.date=xyz
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }

        class EveryWhereSearchHandler implements SearchHandler {

            @Override
            public List<SearchResultsRecord> search(SimpleParser parser) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }


        class QueryTerm {
            private Value value;
            private String attribute;
            private String operand;

            public Value getValue() {
                return value;
            }

            public void setValue(Value value) {
                this.value = value;
            }

            public String getAttribute() {
                return attribute;
            }

            public void setAttribute(String attribute) {
                this.attribute = attribute;
            }

            public String getOperand() {
                return operand;
            }

            public void setOperand(String operand) {
                this.operand = operand;
            }
        }

        enum QueryTermType {
            ASSET(AssetSearchHandler.class), EVENT(AssetSearchHandler.class), UNKNOWN(AssetSearchHandler.class);
            private Class<? extends SearchHandler> clazz;

            QueryTermType(Class<? extends SearchHandler> searchHandlerClass) {
                this.clazz = searchHandlerClass;
            }
            public Class<? extends SearchHandler> getSearchHandler() {
                return clazz;
            }
        }

        enum PredefinedAttribute {
            IDENTIFIED(QueryTermType.ASSET), OWNER(QueryTermType.ASSET), IDENTIFIED_BY(QueryTermType.ASSET);
            private QueryTermType type;

            PredefinedAttribute(QueryTermType type) {
                this.type = type;
            }

            public QueryTermType getType() {
                return type;
            }

            public PredefinedAttribute fromString(String attribute) {
                if (attribute==null) {
                    return null;
                }
                String x = attribute.trim().toUpperCase();
                for (PredefinedAttribute value:values()) {
                    if (x.equals(value.name())) {
                        return value;
                    }
                }
                return null;
            }
        }


*/


//asset_text_view = text, display_Text? (assettype?)
// self join on asset_attribute_view???  union = select -1 -1 -1 a b c from this union select x y z -1 -1 -1 from foobar

    }


