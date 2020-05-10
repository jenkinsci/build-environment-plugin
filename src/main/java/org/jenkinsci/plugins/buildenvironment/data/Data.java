package org.jenkinsci.plugins.buildenvironment.data;

import java.util.TreeMap;

/**
 * Basic definition of a Data object(holds specific information). Methods and
 * Attributes every such object should have.
 * 
 * @author yboev
 * 
 */
public abstract class Data {

    /**
     * Display name of the data object.
     */
    private String name;

    /**
     * ID of the data object.
     */
    private String id;

    /**
     * This is where information is being held, relation variable<->value as
     * map.
     */
    private TreeMap<String, String> data;

    /**
     * Constructor method.
     * 
     * @param name
     *            Name as String.
     * @param id
     *            ID as String.
     */
    public Data(String name, String id) {
        // this.project = project;
        // this.build = build;
        this.name = name;
        this.id = id;
    }

    /**
     * Returns the name.
     * 
     * @return name as String.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the data.
     * 
     * @return the data map.
     */
    public TreeMap<String, String> getData() {
        return this.data;
    }

    /**
     * Returns id.
     * 
     * @return id as String.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Setter method for the data map.
     * 
     * @param newData
     *            the new map that should be stored.
     */
    public void setData(TreeMap<String, String> newData) {
        this.data = newData;
    }

    /**
     * Sets an empty new map object and returns it.
     * @return data as TreeMap.
     */
    public TreeMap<String, String> initEmptyMap() {
        this.data = new TreeMap<String, String>();
        return this.data;
    }
}
