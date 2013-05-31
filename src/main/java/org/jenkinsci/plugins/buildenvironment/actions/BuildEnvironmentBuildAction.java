package org.jenkinsci.plugins.buildenvironment.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.mail.MessagingException;
import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.jenkinsci.plugins.buildenvironment.actions.utils.Constants;
import org.jenkinsci.plugins.buildenvironment.actions.utils.StringPair;
import org.jenkinsci.plugins.buildenvironment.data.Data;
import org.jenkinsci.plugins.buildenvironment.data.EnvVarsData;
import org.jenkinsci.plugins.buildenvironment.data.ProjectData;
import org.jenkinsci.plugins.buildenvironment.data.SlaveData;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Actionable;
import hudson.model.Run;
import hudson.util.RunList;

public class BuildEnvironmentBuildAction  extends Actionable implements Action {

    /**
     * The current build.
     */
    private AbstractBuild<?, ?> build;
    private AbstractProject<?, ?> project;

    private List<Data> dataHolders;

    private AbstractBuild<?, ?> build1;
    private AbstractBuild<?, ?> build2;
    private String diffOption;

    /**
     * Constructor method.
     * 
     * @param build
     *            build for which the action is constructed.
     */
    public BuildEnvironmentBuildAction(Run<?, ?> build) {
        super();
        this.build = (AbstractBuild<?, ?>) build;
        this.project = (AbstractProject<?, ?>) build.getParent();
        this.build1 = this.build;
        this.build2 = this.build;
        addDataHolders();
    }

    public String getDisplayName() {
        return Constants.NAME;
    }

    public String getIconFileName() {
        return Constants.ICONFILENAME;
    }

    public String getUrlName() {
        return Constants.URL;
    }

    public List<Data> getDataHoldersList() {
        return this.dataHolders;
    }

    public String getSearchUrl() {
        return Constants.URL;
    }

    public void doConfigSubmit(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        final JSONObject form = req.getSubmittedForm();
        this.build1 = this.project.getBuildByNumber(Integer.parseInt(form.getString("build1")));
        this.build2 = this.project.getBuildByNumber(Integer.parseInt(form.getString("build2")));
        this.diffOption = form.getString("diffOption");
        rsp.sendRedirect(this.getRedirectUrl());
    }


    public RunList<?> getBuilds() {
        if (this.project == null)
            return null; // fix it!        
        return this.project.getBuilds();
    }
    
    public List<Map<String, StringPair>> getDifference() {
        if(build1 == null || build2 == null) {
            return null;
        }
        return getDifference(this.build1, this.build2);
    }

    public List<Map<String, StringPair>> getDifference(
            AbstractBuild<?, ?> build1, AbstractBuild<?, ?> build2) {
        BuildEnvironmentBuildAction action1 = build1
                .getAction(BuildEnvironmentBuildAction.class);
        BuildEnvironmentBuildAction action2 = build2
                .getAction(BuildEnvironmentBuildAction.class);

        List<Map<String, StringPair>> mapList = new ArrayList<Map<String, StringPair>>();
        for (Data data1 : action1.getDataHoldersList()) {
            boolean flag = false;
            for (Data data2 : action2.getDataHoldersList()) {
                if (data1.getClass().getName()
                        .equals(data2.getClass().getName())) {
                    // Data class listed in both builds, calculate difference
                    // and add it to the mapList
                    mapList.add(compareTwoDataMaps(data1, data2));
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                // @TODO
                // Data instance is listed in action1, but no in action2
                // Maybe show this also????
            }
        }

        // for (Data data2 : action2.getDataHoldersList()) {
        // boolean flag = true;
        // for (Data data1 : action2.getDataHoldersList()) {
        // if (data1.getClass().getName()
        // .equals(data2.getClass().getName())) {
        // flag = false;
        // break;
        // }
        // }
        // if (flag) {
        // // @TODO
        // // Data instance is listed in action2 this time, but no in action1
        // // Maybe show this also????
        // }
        // }

        return mapList;
    }

    private Map<String, StringPair> compareTwoDataMaps(Data data1, Data data2) {
        Map<String, StringPair> cmpList = new TreeMap<String, StringPair>();
        Set<String> keySet = new TreeSet<String>();
        try {
            if (canMapBeRetrieved(data1)) {
                keySet.addAll(data1.getData().keySet());
            }
            if (canMapBeRetrieved(data2)) {
                keySet.addAll(data2.getData().keySet());
            }
            for (String key : keySet) {
                cmpList.put(key, new StringPair(
                        checkMapAttribute(data1, key) ? data1.getData()
                                .get(key) : "",
                        checkMapAttribute(data2, key) ? data2.getData()
                                .get(key) : ""));
            }
            return cmpList;
        } catch (IOException e) {
            return null;
        }
    }

    private boolean canMapBeRetrieved(Data data) throws IOException {
        return data != null && data.getData() != null;
    }

    private boolean checkMapAttribute(Data data, String key) throws IOException {
        return canMapBeRetrieved(data) && data.getData().containsKey(key);
    }
    
    private String getRedirectUrl() {
        return this.build.getUrl();
    }

    private void addDataHolders() {
        // add the 3 classes to the list here and then test if information is
        // held untouched.
        // for Friday 24.05.2013
        this.dataHolders = new ArrayList<Data>();
        this.dataHolders.add(new EnvVarsData(this.project, this.build,
                "Environment Variables"));
        this.dataHolders.add(new SlaveData(this.project, this.build,
                "Slave Information"));
        this.dataHolders.add(new ProjectData(this.project, this.build,
                "Project Information"));
    }

    // public TreeMap<String, String> getProjectMap() throws IOException {
    // for (Data data : this.dataHolders) {
    // if (data instanceof ProjectData) {
    // return data.getData();
    // }
    // }
    // TreeMap<String, String> m = new TreeMap<String, String>();
    // m.put("MapNull", "not found in the list");
    // return m;
    // }
    //
    //
    // public TreeMap<String, String> getSlaveMap() throws IOException {
    // for (Data data : this.dataHolders) {
    // if (data instanceof SlaveData) {
    // return data.getData();
    // }
    // }
    // TreeMap<String, String> m = new TreeMap<String, String>();
    // m.put("MapNull", "not found in the list");
    // return m;
    // }
    //
    // public TreeMap<String, String> getEnvVarsMap() throws IOException {
    // for (Data data : this.dataHolders) {
    // if (data instanceof EnvVarsData) {
    // return data.getData();
    // }
    // }
    // TreeMap<String, String> m = new TreeMap<String, String>();
    // m.put("MapNull", "not found in the list");
    // return m;
    // }

}
