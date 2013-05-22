package org.jenkinsci.plugins.buildenvironment.data;

import java.io.IOException;
import java.util.TreeMap;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Node;
import hudson.model.JobPropertyDescriptor;
import hudson.slaves.NodeProperty;

public class SlaveData extends Data {
    Node node;

    public SlaveData(AbstractProject<?, ?> project, AbstractBuild<?, ?> build) {
        super(project, build);
        this.node = build.getBuiltOn();
    }

    @Override
    protected String getFileName() {
        return "slave.xml";
    }

    protected TreeMap<String, String> createDataMap() {
        TreeMap<String, String> data = new TreeMap<String, String>();
        // get data, add it to map and return the newly created map.
        data.put("Node name", node.getDisplayName());
        data.put("Number of executors", String.valueOf(node.getNumExecutors()));
        try {
            data.put("clock difference", node.getClockDifference().toString());
        } catch (IOException e) {
            // cannot be retrieved
        } catch (InterruptedException e) {
            // cannot be retrieved
        }
        data.put("Property size", this.node.getNodeProperties().size() + "");
        return data;
    }
}
