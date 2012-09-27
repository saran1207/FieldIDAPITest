package com.n4systems.api.conversion.event;

import com.google.common.collect.Lists;
import com.n4systems.util.StringUtils;

import java.util.List;
import java.util.StringTokenizer;

public class LocationSpecification {

        private List<String> nodes = Lists.newArrayList();
        private String freeForm = null;
        private String location;

        public LocationSpecification(String location) {
            this.location = location;
            initialize();
        }

        private void initialize() throws IllegalArgumentException {
            if (location==null) {
                return;
            }
            int colonIndex = location.indexOf(":");
            if (colonIndex!=-1) {
                parseHierarchy(location.substring(0,colonIndex));
                freeForm = location.substring(colonIndex+1).trim();
            } else {
                parseHierarchy(location);
            }
            if (!StringUtils.isEmpty(location) && (freeForm==null && nodes.isEmpty())) {
                throw new IllegalArgumentException("'" + location + "' does not follow the naming convention  (e.g.   apple > banana > pear : foobar");
            }
        }

        private void parseHierarchy(String hierarchy) {
            if (hierarchy.trim().endsWith(">") || hierarchy.trim().startsWith(">")) {
                return;
            }
            for (StringTokenizer stringTokenizer = new StringTokenizer(hierarchy.trim(),">"); stringTokenizer.hasMoreTokens(); ) {
                String s = stringTokenizer.nextToken();
                nodes.add(s.trim());
            }
        }

        public String getFreeForm() {
            return freeForm;
        }

        public List<String> getHierarchy() {
            return nodes;
        }
}
