package org.jenkinsci.plugins.buildenvironment.actions.utils;

/**
 * Represents a String pair object.
 * 
 * @author yboev
 * 
 */
public class StringPair {

    /**
     * First String.
     */
    private String first;

    /**
     * Second String.
     */
    private String second;

    /**
     * Constructor method.
     * 
     * @param first
     *            1st String object.
     * @param second
     *            2nd String object.
     */
    public StringPair(String first, String second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first String object of the pair.
     * 
     * @return the 1st String.
     */
    public String getFirst() {
        return this.first;
    }

    /**
     * Returns the 2nd String object of the pair.
     * 
     * @return the 2nd String.
     */
    public String getSecond() {
        return this.second;
    }

    /**
     * Checks if the two pair Strings are different.
     * 
     * @return true if the two Strins are different, false otherwise.
     */
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
