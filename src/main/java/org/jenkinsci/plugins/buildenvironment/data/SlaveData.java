package org.jenkinsci.plugins.buildenvironment.data;

import java.io.IOException;
import java.util.TreeMap;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Node;

/**
 * Represents information connected with the slave on which the build has been
 * built.
 * 
 * @author yboev
 * 
 */
public class SlaveData extends Data {

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
    public SlaveData(AbstractProject<?, ?> project, AbstractBuild<?, ?> build,
            String name, String id) {
        super(project, build, name, id);
        initializeDataMap(build.getBuiltOn());
    }

    /**
     * Initialize method. Here data is added to the map.
     */
    @SuppressWarnings("deprecation")
    private void initializeDataMap(Node node) {
        this.data = new TreeMap<String, String>();
        // get data, add it to map and return the newly created map.
        if (node == null) {
            data.put("Node was null", "build.getBuildOn() was null");
            return;
        }
        data.put("Node display name", node.getDisplayName());
        data.put("Number of executors", String.valueOf(node.getNumExecutors()));
        data.put("Node label", node.getLabelString());
        data.put("Node name", node.getNodeName());
        data.put("Node mode", node.getMode().toString());
        data.put("Node root path", node.getRootPath().toString());
        data.put("is Hold off launch until save",
                String.valueOf(node.isHoldOffLaunchUntilSave()));
        try {
            data.putAll(node.toComputer().getEnvVars());
            data.put("Busy executors",
                    String.valueOf(node.toComputer().countBusy()));
            data.put("Host name", node.toComputer().getHostName());
            data.put("idle executors",
                    String.valueOf(node.toComputer().countIdle()));
            data.put("Computer connect time",
                    String.valueOf(node.toComputer().getConnectTime()));
            data.put("Demand start in ms", String.valueOf(node.toComputer()
                    .getDemandStartMilliseconds()));
            data.put("Computer Heap dump", node.toComputer().getHeapDump()
                    .toString());
            data.put("Computer retention strategy", node.toComputer()
                    .getRetentionStrategy().toString());
            for (Object obj : node.toComputer().getSystemProperties().keySet()) {
                data.put(obj.toString(), node.toComputer()
                        .getSystemProperties().get(obj).toString());
            }
            data.put("Is accepting tasks",
                    String.valueOf(node.toComputer().isAcceptingTasks()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
