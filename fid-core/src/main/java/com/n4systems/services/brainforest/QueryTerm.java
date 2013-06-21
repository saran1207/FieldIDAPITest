package com.n4systems.services.brainforest;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

public class QueryTerm implements Serializable {

    enum Conjunction { AND, OR };

    public enum Operator {
        GT(">"), LT("<"), GE(">="), LE("<="), EQ("=",":"), NE("<>","!=");
        private  List<String> text;
        Operator(String... s) { this.text = Lists.newArrayList(s); }
        public static Operator fromString(String s){
            for (Operator op:values()) {
                if (op.text.contains(s.trim())) { return op; }
            }
            return null;
        }

        public String getText() {
            return text.get(0);
        }
    }


    private Value value;
    private Operator operator;
    private String attribute;

    public QueryTerm(String attribute, String operator, Value value) {
        this(attribute, Operator.fromString(operator), value);
    }

    public QueryTerm(String attribute, Operator operator, Value value) {
        Preconditions.checkArgument(operator!=null, "must supply a valid operator.");
        setAttribute(attribute);
        this.operator = operator;
        this.value = value;
    }

    private void setAttribute(String attribute) {
        String s = null;
        if (attribute!=null) {
            s = attribute.trim();
            if (s.startsWith("\"") && s.endsWith("\"")) {
                s = s.substring(1,s.length()-1);
            }
            if (s.startsWith("'") && s.endsWith("'")) {
                s = s.substring(1,s.length()-1);
            }
        }
        this.attribute = s;
    }

    @Override
    public String toString() {
        return "{"+(attribute==null?"<default>":attribute) + " " + operator.getText() + " " + value.toVerboseString()+"}";
    }

    public String getAttribute() {
        return attribute.toLowerCase();
    }

    public String getAttributeOriginalCase() {
        return attribute;
    }

    public Operator getOperator() {
        return operator;
    }

    public Value getValue() {
        return value;
    }

}
