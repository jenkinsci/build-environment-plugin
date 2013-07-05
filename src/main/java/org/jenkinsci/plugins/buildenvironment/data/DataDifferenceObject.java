package org.jenkinsci.plugins.buildenvironment.data;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jenkinsci.plugins.buildenvironment.actions.utils.StringPair;

/**
 * Object representing the difference between two Data objects.
 * 
 * @author yboev
 * 
 */
public class DataDifferenceObject {

    /**
     * Name of the difference object, the same as the name of the 1st data
     * object.
     */
    private String name;

    /**
     * Map representing the difference between the two Data objects.
     */
    private Map<String, StringPair> difMap;

    /**
     * Constructor method.
     * 
     * @param data1
     *            1st Data object.
     * @param data2
     *            2nd Data object.
     */
    public DataDifferenceObject(Data data1, Data data2) {
        this.name = data1.getName();
        this.calculateDifference(data1, data2);
    }

    /**
     * Returns the name.
     * 
     * @return the name as String.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the difference map.
     * 
     * @return the map object.
     */
    public Map<String, StringPair> getMap() {
        return this.difMap;
    }

    /**
     * Returns the number of entries that are different.
     * 
     * @return this number as int, should be >= 0.
     */
    public int getDifferentCount() {
        return this.getDifferenceOnly().size();
    }

    /**
     * Returns a map objects, containing only the different entries.
     * 
     * @return the map with different entries.
     */
    public Map<String, StringPair> getDifferenceOnly() {
        Map<String, StringPair> difOnlyMap = new TreeMap<String, StringPair>();
        for (String key : this.difMap.keySet()) {
            if (this.difMap.get(key).areDifferent()) {
                difOnlyMap.put(key, difMap.get(key));
            }
        }
        return difOnlyMap;
    }

    /**
     * Calculates and initializes the difference map objects by comparing the
     * two Data objects.
     * 
     * @param data1
     *            1st Data object.
     * @param data2
     *            2nd Data object.
     */
    private void calculateDifference(Data data1, Data data2) {
        this.difMap = new TreeMap<String, StringPair>();
        Set<String> keySet = new TreeSet<String>();
        try {
            if (canMapBeRetrieved(data1)) {
                keySet.addAll(data1.getData().keySet());
            }
            if (canMapBeRetrieved(data2)) {
                keySet.addAll(data2.getData().keySet());
            }
            for (String key : keySet) {
                this.difMap.put(
                        key,
                        new StringPair(checkMapAttribute(data1, key) ? data1
                                .getData().get(key) : "", checkMapAttribute(
                                data2, key) ? data2.getData().get(key) : ""));
            }
        } catch (IOException e) {
            this.difMap = null;
        }
    }

    /**
     * Checks map objects can be retrieved for a given Data object.
     * 
     * @param data
     *            the data object
     * @return true if map can be retrieved, false otherwise.
     * @throws IOException
     */
    private boolean canMapBeRetrieved(Data data) throws IOException {
        return data != null && data.getData() != null;
    }

    /**
     * Checks if the map of a Data object contains a given key.
     * 
     * @param data
     *            the Data object.
     * @param key
     *            the key as String
     * @return true if there is an entry behind this key, false otherwise.
     * @throws IOException
     */
    private boolean checkMapAttribute(Data data, String key) throws IOException {
        return canMapBeRetrieved(data) && data.getData().containsKey(key);
    }
}
