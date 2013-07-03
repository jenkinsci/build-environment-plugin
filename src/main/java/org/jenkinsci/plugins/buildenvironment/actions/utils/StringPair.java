package org.jenkinsci.plugins.buildenvironment.actions.utils;

public class StringPair {

    private String first;
    private String second;

    public StringPair(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return this.first;
    }

    public String getSecond() {
        return this.second;
    }

    public boolean areDifferent() {
        if (first == null && second == null) {
            return false;
        }
        try {
            return !(this.first.trim().equals(this.second.trim()));
        } catch (NullPointerException e) {
            return true;
        }
    }
}
