package com.n4systems.services.brainforest;

public abstract class MultiValue extends Value {

    public enum Delimiter {
        NONE(""), RANGE("..."), COMMA(",");

        private final String text;

        Delimiter(String s) {
            this.text = s;
        }
        public String getText() {
            return text;
        }
    }

    public MultiValue() {
        super();
    }



}
