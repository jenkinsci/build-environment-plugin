package org.jenkinsci.plugins.buildenvironment.data;

import java.io.IOException;
import java.util.TreeMap;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.triggers.TriggerDescriptor;

/**
 * Represents information about the project to which this build belongs.
 * 
 * @author yboev
 * 
 */
public class ProjectData extends Data {

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
    public ProjectData(AbstractProject<?, ?> project,
            AbstractBuild<?, ?> build, String name, String id) {
        super(name, id);
        initializeDataMap(project);
    }

    /**
     * Initialize method. Here data is added to the map.
     */
    private void initializeDataMap(AbstractProject<?, ?> project) {
        this.data = new TreeMap<String, String>();
        // get data, add it to map and return the newly created map.
        data.put("Project name", project.getName());
        data.put("Project url", project.getUrl());
        data.put("SCM", project.getScm().toString());
        data.put("SCM type", project.getScm().getType());
        data.put("Quiet period", String.valueOf(project.getQuietPeriod()));
        data.put("Block when downstream building",
                String.valueOf(project.blockBuildWhenDownstreamBuilding()));
        data.put("Block when upstream building",
                String.valueOf(project.blockBuildWhenUpstreamBuilding()));
        data.put("Abort permission",
                String.valueOf(project.hasAbortPermission()));
        data.put("Is buildable", String.valueOf(project.isBuildable()));
        data.put("Is concurrent build",
                String.valueOf(project.isConcurrentBuild()));
        data.put("Is disabled", String.valueOf(project.isDisabled()));
        data.put("Is fingerprint configured",
                String.valueOf(project.isFingerprintConfigured()));
        data.put("Is parameterized", String.valueOf(project.isParameterized()));
        data.put("Is name editable", String.valueOf(project.isNameEditable()));

        // TreeMap<String, String> configFileMap = null;
        // try {
        // data.put("configFileMap", project.getConfigFile()
        // .asString());
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // for (JobPropertyDescriptor i : project.getProperties()
        // .keySet()) {
        // data.put(i.toString(), project.getProperties().get(i)
        // .toString());
        // }
        for (TriggerDescriptor i : project.getTriggers().keySet()) {
            data.put(i.getDisplayName(), project.getTriggers().get(i)
                    .toString());
        }
    }
}
