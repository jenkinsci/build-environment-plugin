package org.jenkinsci.plugins.buildenvironment.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
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

/**
 * Main class for this plugin. Contains most of the functionality. Here also
 * reside all method used for visualizing information(used in index.jelly,
 * summary.jelly, environment_export.jelly).
 * 
 * The class implements an Action that is added to every build on its
 * completion. The action is added by build.addAction() (see
 * EnvironmentDataWriter class), meaning it is persistent.
 * 
 * @author yboev
 * 
 */
public class BuildEnvironmentBuildAction extends Actionable implements Action {

    private static final Logger LOGGER = Logger
            .getLogger(BuildEnvironmentBuildAction.class.getName());

    /**
     * The current build.
     */
    final private AbstractBuild<?, ?> build;

    /**
     * First build when comparing builds. Equals the current build at the start.
     */
    private AbstractBuild<?, ?> build1;

    /**
     * Second build when comparing builds. Equals the current build at the
     * start.
     */
    private AbstractBuild<?, ?> build2;

    /**
     * Tells if every entry should be shown in the comaprasion view or only the
     * different ones.
     */
    private String diffOption;

    /**
     * List with every Data object for this build. Initialized in the {@code}
     * addDataHolders method.
     */
    private List<Data> dataHolders;

    /**
     * List with DataDifferenceObjects. There should be one DataDifferenceObject
     * for every Data objects that the two build being compared have in common.
     */
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

    /**
     * Display name for this Action.
     */
    public String getDisplayName() {
        return Constants.NAME;
    }

    /**
     * Icon file for this Action.
     */
    public String getIconFileName() {
        return Constants.MENUICONFILENAME;
    }

    /**
     * Summary icon file for this Action.
     * 
     * @return the file as String.
     */
    public String getSummaryIconFilename() {
        return Constants.SUMMARYICONFILENAME;
    }

    /**
     * Url for this Action.
     */
    public String getUrlName() {
        return Constants.URL;
    }

    /**
     * Search url for this Acion.
     */
    public String getSearchUrl() {
        return Constants.URL;
    }

    /**
     * Returns the list with Data objects.
     * 
     * @return list with every Data object for this build.
     */
    public List<Data> getDataHoldersList() {
        return this.dataHolders;
    }

    /**
     * Converts true/false -> yes/no.
     * 
     * @param value
     *            boolean to be converted.
     * @return if value was true then "yes", otherwise "no".
     */
    public String trueFalseToYesNo(boolean value) {
        if (value) {
            return "yes";
        }
        return "no";
    }

    /**
     * Returns all build for the current project that have this Action listed.
     * 
     * @return list of AbstractBuild objects.
     */
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

    /**
     * Returns all environment variables defined in this build as a list, ready
     * to be exportedin bash for example. There are 2 entries for every
     * variable, the first assings a value and the second exports the variable.
     * example: FOO=123 export FOO
     * 
     * @return a list containing the two lines for every variable.
     */
    public ArrayList<String> getEnvironmentVariablesForExport() {
        Data envVars = null;
        for (Data data : this.getDataHoldersList()) {
            if (data.getId().equals("envVars")) {
                envVars = data;
                break;
            }
        }
        TreeMap<String, String> envVarsMap;
        if (envVars != null) {
            envVarsMap = envVars.getData();
        } else {
            envVarsMap = new TreeMap<String, String>(this.getBuild()
                    .getEnvVars());
        }
        ArrayList<String> exportVars = new ArrayList<String>();
        for (String key : envVarsMap.keySet()) {

            exportVars.add(key + "=" + envVarsMap.get(key) + "\n");
            exportVars.add("export " + key + "\n");
        }
        LOGGER.info(exportVars.size() + "");
        return exportVars;
    }

    /**
     * Returns all build for the current project.
     * 
     * @return all builds as list.
     */
    public RunList<?> getBuilds() {
        return this.getProject().getBuilds();
    }

    /**
     * Return the current build.
     * 
     * @return the current build as AbstractBuild object.
     */
    public AbstractBuild<?, ?> getBuild() {
        return this.build;
    }

    /**
     * Returns the parent of this build.
     * 
     * @return the current project as Job object.
     */
    public Job<?, ?> getProject() {
        return this.build.getProject();
    }

    /**
     * Returns the parent of this build.
     * 
     * @return the current project as Abstractproject
     */
    public AbstractProject<?, ?> getAbstractProject() {
        return this.build.getParent();
    }

    /**
     * Method to populate values and implement functionality of a HTML form. The
     * form can be found in index.jelly.
     * 
     * @param req
     *            the Request
     * @param rsp
     *            the Response
     * @throws IOException
     * @throws ServletException
     */
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

    /**
     * Passes the value of diffOption.
     * 
     * @return same as diffOption, false if diffOption is null.
     */
    public boolean isOnlyDifference() {
        if (this.diffOption != null && this.diffOption.equals("true")) {
            return true;
        }
        return false;
    }

    /**
     * Returns the redirect url to which we jump when the HTML form from
     * doConfigSubmit() is submitted.
     * 
     * @return the url as String.
     */
    private String getRedirectUrl() {
        return this.getBuildUrl() + "/" + Constants.URL;
    }

    /**
     * Passes the build1 Attribute.
     * 
     * @return the build1 object.
     */
    public AbstractBuild<?, ?> getBuild1() {
        return this.build1;
    }

    /**
     * Passes the build2 Attribute
     * 
     * @return the build2 object.
     */
    public AbstractBuild<?, ?> getBuild2() {
        return this.build2;
    }

    /**
     * Return the background color according to a boolean value.
     * 
     * @param change
     *            the boolean value indicating if there was a change is an
     *            entry.
     * @return the corresponding color as String.
     */
    public String getBackgroundColor(boolean change) {
        if (change) {
            return Constants.getBackgroundDifferenceColorAsString();
        }
        return Constants.getBackgroundNoDifferenceColorAsString();
    }

    /**
     * Returns the number of different entries between Data object for this
     * build and a given Data object. Note that the two Data objects should have
     * the same name. If no such 2 objects exist then zero is returned as
     * difference count.
     * 
     * @param data
     *            the Data object.
     * @return number of difference entries as int.
     */
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

    /**
     * Checks if a given entry in a given Data object is different from what has
     * been recorded in the previous build.
     * 
     * @param data
     *            the Data object.
     * @param key
     *            the key Object.
     * @return true if they are different, false otherwise.
     */
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

    /**
     * Returns a list with DataDifferenceObjects representing the difference
     * between build1 nad build2 set for this Action.
     * 
     * @return the list with difference objects.
     */
    public List<DataDifferenceObject> getDifference() {
        if (build1 == null || build2 == null) {
            return null;
        }
        return getDifference(this.build1, this.build2);
    }

    /**
     * Returns a DataDifferenceObject by its name.
     * 
     * @param name
     *            the name of the difference object.
     * @return the difference object corresponding to the given name, null if no
     *         such.
     */
    private DataDifferenceObject getDataDifferenceObjectByName(String name) {
        for (DataDifferenceObject currentDataDiff : this.dataDifference) {
            if (currentDataDiff.getName().equals(name)) {
                return currentDataDiff;
            }
        }
        return null;
    }

    /**
     * Calculated the difference between this build and the previous one. If
     * there was no previous, then dataDifference will be null. For example when
     * a project is being built for the first time.
     */
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

    /**
     * Calculated the list with DataDifferenceObjects that represents the
     * difference between build1 and build2 given as parameters.
     * 
     * @param build1
     *            the first build.
     * @param build2
     *            the second build.
     * @return list with difference objects.
     */
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

    /**
     * Adds every Data object to the list of Data holders. Called once when this
     * Action is constructed.
     */
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
