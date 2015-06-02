package org.jenkinsci.plugins.buildenvironment.data;

import java.io.IOException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import hudson.util.LogTaskListener;

/**
 * Represents information about the environment variables used during the build.
 * 
 * @author yboev
 * 
 */
public class EnvVarsData extends Data {
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(EnvVarsData.class.getName());

    /**
     * Constructor method.
     * 
     * @param build
     *            AbstractBuild.
     * @param name
     *            Name as String.
     * @param id
     *            ID as String.
     */
    public EnvVarsData(AbstractBuild<?, ?> build, String name, String id) {
        super(name, id);
        initializeDataMap(build);
    }

    /**
     * Initialize method. Here data is added to the map.
     * 
     * @param build
     *            Current build.
     */
    private void initializeDataMap(AbstractBuild<?, ?> build) {
        TreeMap<String, String> data = this.initEmptyMap();
        // get data, add it to map and return the newly created map.
     
        try {
            data.putAll(build.getEnvironment((TaskListener) new LogTaskListener(LOGGER, Level.INFO)));
        } catch (IOException e) {

        } catch (InterruptedException e) {

        }
        
       
    }
}
