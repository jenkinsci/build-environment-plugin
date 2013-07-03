package org.jenkinsci.plugins.buildenvironment.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.jenkinsci.plugins.buildenvironment.actions.utils.Constants;
import org.jenkinsci.plugins.buildenvironment.actions.utils.Utils;
import org.jenkinsci.plugins.buildenvironment.data.Data;
import org.jenkinsci.plugins.buildenvironment.data.DataDifferenceObject;
import org.jenkinsci.plugins.buildenvironment.data.EnvVarsData;
import org.jenkinsci.plugins.buildenvironment.data.ProjectData;
import org.jenkinsci.plugins.buildenvironment.data.SlaveData;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Actionable;
import hudson.model.Job;
import hudson.model.Run;
import hudson.util.RunList;

public class BuildEnvironmentBuildAction extends Actionable implements Action {

    // private static final Logger LOGGER = Logger
    // .getLogger(BuildEnvironmentBuildAction.class.getName());

    /**
     * The current build.
     */
    final private AbstractBuild<?, ?> build;

    private AbstractBuild<?, ?> build1;
    private AbstractBuild<?, ?> build2;
    private String diffOption;

    private List<Data> dataHolders;
    private List<DataDifferenceObject> dataDifference;

    /**
     * Constructor method.
     * 
     * @param build
     *            build for which the action is constructed.
     */
    public BuildEnvironmentBuildAction(Run<?, ?> build) {
        super();
        this.build = (AbstractBuild<?, ?>) build;
        this.build1 = this.build;
        this.build2 = this.build;
        this.diffOption = String.valueOf(false);
        this.addDataHolders();
    }

    public String getDisplayName() {
        return Constants.NAME;
    }

    public String getIconFileName() {
        return Constants.MENUICONFILENAME;
    }

    public String getSummaryIconFilename() {
        return Constants.SUMMARYICONFILENAME;
    }

    public String getUrlName() {
        return Constants.URL;
    }

    public String getSearchUrl() {
        return Constants.URL;
    }

    public List<Data> getDataHoldersList() {
        return this.dataHolders;
    }

    public String trueFalseToYesNo(boolean value) {
        if (value) {
            return "yes";
        }
        return "no";
    }

    public List<AbstractBuild<?, ?>> getBuildsWithAction() {

        List<AbstractBuild<?, ?>> list = new ArrayList<AbstractBuild<?, ?>>();
        AbstractBuild<?, ?> currentBuild = null;
        if (this.getProject().getLastCompletedBuild() instanceof AbstractBuild) {
            currentBuild = (AbstractBuild<?, ?>) this.getProject()
                    .getLastCompletedBuild();
        }
        while (currentBuild != null) {
            if (currentBuild.getAction(BuildEnvironmentBuildAction.class) != null) {
                list.add(currentBuild);
            }
            currentBuild = currentBuild.getPreviousCompletedBuild();
        }
        return list;
    }

    public RunList<?> getBuilds() {
        return this.getProject().getBuilds();
    }

    public AbstractBuild<?, ?> getBuild() {
        return this.build;
    }

    public Job<?, ?> getProject() {
        return this.build.getProject();
    }

    public AbstractProject<?, ?> getAbstractProject() {
        return this.build.getParent();
    }

    public void doConfigSubmit(StaplerRequest req, StaplerResponse rsp)
            throws IOException, ServletException {
        final JSONObject form = req.getSubmittedForm();
        this.build1 = this.getAbstractProject().getBuildByNumber(
                Integer.parseInt(form.getString("build1")));
        this.build2 = this.getAbstractProject().getBuildByNumber(
                Integer.parseInt(form.getString("build2")));
        this.diffOption = form.getString("diffOnly");
        rsp.sendRedirect(this.getRedirectUrl());
    }

    public boolean isOnlyDifference() {
        if (this.diffOption != null && this.diffOption.equals("true")) {
            return true;
        }
        return false;
    }

    private String getRedirectUrl() {
        return this.getBuildUrl() + "/" + Constants.URL;
    }

    public AbstractBuild<?, ?> getBuild1() {
        return this.build1;
    }

    public AbstractBuild<?, ?> getBuild2() {
        return this.build2;
    }

    public String getBackgroundColor(boolean change) {
        if (change) {
            return Constants.getBackgroundDifferenceColorAsString();
        }
        return Constants.getBackgroundNoDifferenceColorAsString();
    }

    public int getDifferentCount(Data data) {
        if (this.dataDifference == null) {
            this.calculatePreviousBuildDifference();
        }
        try {
            return this.getDataDifferenceObjectByName(data.getName())
                    .getDifferentCount();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public boolean isDifferentFromPrevious(Data data, String key) {
        if (this.dataDifference == null) {
            this.calculatePreviousBuildDifference();
        }
        try {
            return this.getDataDifferenceObjectByName(data.getName()).getMap()
                    .get(key).areDifferent();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public List<DataDifferenceObject> getDifference() {
        if (build1 == null || build2 == null) {
            return null;
        }
        return getDifference(this.build1, this.build2);
    }

    private DataDifferenceObject getDataDifferenceObjectByName(String name) {
        for (DataDifferenceObject currentDataDiff : this.dataDifference) {
            if (currentDataDiff.getName().equals(name)) {
                return currentDataDiff;
            }
        }
        return null;
    }

    private void calculatePreviousBuildDifference() {
        AbstractBuild<?, ?> previousBuild = this.build
                .getPreviousCompletedBuild();
        while (previousBuild != null) {
            if (previousBuild.getAction(BuildEnvironmentBuildAction.class) != null) {
                // previous build with this action found, get out
                break;
            }
            previousBuild = previousBuild.getPreviousCompletedBuild();
        }
        if (previousBuild != null) {
            this.dataDifference = getDifference(this.build, previousBuild);
        } else {
            this.dataDifference = null;
        }
    }

    private List<DataDifferenceObject> getDifference(
            AbstractBuild<?, ?> build1, AbstractBuild<?, ?> build2) {
        BuildEnvironmentBuildAction action1 = build1
                .getAction(BuildEnvironmentBuildAction.class);
        BuildEnvironmentBuildAction action2 = build2
                .getAction(BuildEnvironmentBuildAction.class);

        List<DataDifferenceObject> diffList = new ArrayList<DataDifferenceObject>();
        for (Data data1 : action1.getDataHoldersList()) {
            boolean flag = false;
            for (Data data2 : action2.getDataHoldersList()) {
                if (data1.getClass().getName()
                        .equals(data2.getClass().getName())) {
                    // Data class listed in both builds, calculate difference
                    // and add it to the mapList
                    diffList.add(new DataDifferenceObject(data1, data2));
                    flag = true;
                    break;
                }
            }
            // if (!flag) {
            // @TODO
            // Data instance is listed in action1, but no in action2
            // Maybe show this also????
            // }
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

        return diffList;
    }

    /**
     * Returns the build url.
     * 
     * @return build url as string
     */
    @SuppressWarnings("deprecation")
    private String getBuildUrl() {
        if (this.getProject() != null) {
            return this.getProject().getAbsoluteUrl() + this.getBuildNumber();
        }
        return this.build.getAbsoluteUrl();
    }

    /**
     * Returns the build number.
     * 
     * @return build number as string
     */
    private String getBuildNumber() {
        return Integer.toString(this.build.getNumber());
    }

    private void addDataHolders() {
        this.dataHolders = new ArrayList<Data>();
        this.dataHolders.add(new EnvVarsData(this.getAbstractProject(),
                this.build, "Environment Variables", "envVar"));
        this.dataHolders.add(new SlaveData(this.getAbstractProject(),
                this.build, "Slave Information", "slaveInfo"));
        this.dataHolders.add(new ProjectData(this.getAbstractProject(),
                this.build, "Project Information", "projectInfo"));
        for (Data data : this.dataHolders) {
            Utils.filterMap(data.getData(),
                    Utils.getPasswordRestrictionPatterns());
        }
    }
}
