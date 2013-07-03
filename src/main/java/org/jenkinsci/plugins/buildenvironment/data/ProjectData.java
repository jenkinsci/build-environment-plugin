package org.jenkinsci.plugins.buildenvironment.data;

import java.io.IOException;
import java.util.TreeMap;

import hudson.model.JobPropertyDescriptor;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.triggers.TriggerDescriptor;

public class ProjectData extends Data {

    public ProjectData(AbstractProject<?, ?> project,
            AbstractBuild<?, ?> build, String name, String id) {
        super(project, build, name, id);
        initializeDataMap();
    }

    private void initializeDataMap() {
        this.data = new TreeMap<String, String>();
        // get data, add it to map and return the newly created map.
        data.put("Project name", this.getProject().getName());
        data.put("Project url", this.getProject().getUrl());
        data.put("SCM", this.getProject().getScm().toString());
        data.put("SCM type", this.getProject().getScm().getType());
        data.put("Quiet period",
                String.valueOf(this.getProject().getQuietPeriod()));
        data.put("Block when downstream building", String.valueOf(this
                .getProject().blockBuildWhenDownstreamBuilding()));
        data.put("Block when upstream building", String.valueOf(this
                .getProject().blockBuildWhenUpstreamBuilding()));
        data.put("Abort permission",
                String.valueOf(this.getProject().hasAbortPermission()));
        data.put("Is buildable",
                String.valueOf(this.getProject().isBuildable()));
        data.put("Is concurrent build",
                String.valueOf(this.getProject().isConcurrentBuild()));
        data.put("Is disabled", String.valueOf(this.getProject().isDisabled()));
        data.put("Is fingerprint configured",
                String.valueOf(this.getProject().isFingerprintConfigured()));
        data.put("Is parameterized",
                String.valueOf(this.getProject().isParameterized()));
        data.put("Is name editable",
                String.valueOf(this.getProject().isNameEditable()));

        // TreeMap<String, String> configFileMap = null;
        try {
            data.put("configFileMap", this.getProject().getConfigFile().asString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // for (JobPropertyDescriptor i : this.getProject().getProperties()
        // .keySet()) {
        // data.put(i.toString(), this.getProject().getProperties().get(i)
        // .toString());
        // }
        for (TriggerDescriptor i : this.getProject().getTriggers().keySet()) {
            data.put(i.getDisplayName(), this.getProject().getTriggers().get(i)
                    .toString());
        }
    }
}
