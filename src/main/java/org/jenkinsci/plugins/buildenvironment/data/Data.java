package org.jenkinsci.plugins.buildenvironment.data;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.util.TreeMap;

/**
 * Basic definition of a Data object(holds specific information). Methods and
 * Attributes every such object should have.
 * 
 * @author yboev
 * 
 */
public abstract class Data {
    // private static final Logger LOGGER = Logger.getLogger(Data.class
    // .getName());

    /**
     * The Project with which this data object is connected.
     */
    private AbstractProject<?, ?> project;

    /**
     * The build witch which this data object is connected.
     */
    private AbstractBuild<?, ?> build;

    /**
     * Display name of the data object.
     */
    String name;

    /**
     * ID of the data object.
     */
    String id;

    /**
     * This is where information is being held, relation variable<->value as
     * map.
     */
    protected TreeMap<String, String> data;

    /**
     * Constructor method.
     * 
     * @param project
     *            AbstractProject.
     * @param build
     *            AbstractBuild.
     * @param name
     *            Name as String.
     * @param id
     *            ID as String.
     */
    public Data(AbstractProject<?, ?> project, AbstractBuild<?, ?> build,
            String name, String id) {
        this.project = project;
        this.build = build;
        this.name = name;
        this.id = id;
    }

    /**
     * Returns the project.
     * 
     * @return the project object.
     */
    public AbstractProject<?, ?> getProject() {
        return this.project;
    }

    /**
     * Returns the build.
     * 
     * @return the build object.
     */
    public AbstractBuild<?, ?> getBuild() {
        return this.build;
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
}
