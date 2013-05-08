package org.jenkinsci.plugins.environmentinformator;

import org.jenkinsci.plugins.environmentinformator.utils.Constants;
import org.kohsuke.stapler.StaplerProxy;

import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.model.Actionable;
import hudson.model.Run;

public class EnvironmentInformatorBuildAction extends Actionable implements Action, StaplerProxy {

    /**
     * The current build.
     */
    private AbstractBuild<?, ?> build;

    /**
     * Constructor method.
     * 
     * @param build
     *            build for which the action is constructed.
     */
    public EnvironmentInformatorBuildAction(Run<?, ?> build) {
        this.build = (AbstractBuild<?, ?>) build;
    }

    public String getSearchUrl() {
        return Constants.URL;
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

    public Object getTarget() {
        // TODO Auto-generated method stub
        return "TARGET !!!";
    }
}
