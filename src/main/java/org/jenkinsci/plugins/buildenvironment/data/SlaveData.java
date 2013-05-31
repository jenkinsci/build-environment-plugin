package org.jenkinsci.plugins.buildenvironment.data;

import java.io.IOException;
import java.util.TreeMap;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Node;

public class SlaveData extends Data {

    public SlaveData(AbstractProject<?, ?> project, AbstractBuild<?, ?> build, String name) {
        super(project, build, name);
        initializeDataMap(build.getBuiltOn());
    }

    private void initializeDataMap(Node node) {
        this.data = new TreeMap<String, String>();
        // get data, add it to map and return the newly created map.
        if(node == null) {
            data.put("Node was null", "build.getBuildOn() was null");
            return;
        }
        data.put("Node name", node.getDisplayName());
        data.put("Number of executors", String.valueOf(node.getNumExecutors()));
        data.put("Time", System.currentTimeMillis() + "");
    }
}
