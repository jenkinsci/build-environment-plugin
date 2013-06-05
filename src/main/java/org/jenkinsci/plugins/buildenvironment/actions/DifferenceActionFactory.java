package org.jenkinsci.plugins.buildenvironment.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import hudson.Extension;
import hudson.model.Action;
import hudson.model.TransientBuildActionFactory;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Run;

/**
 * Action Factory for the build view. Adds the "Send Mail" action to every
 * build.
 * 
 * @author yboev
 * 
 */
@Extension
public class DifferenceActionFactory extends TransientBuildActionFactory {

    /** Our logger. */
    // private static final Logger LOG =
    // Logger.getLogger(MailProjectActionFactory.class.getName());

    /**
     * @param build
     *            the current build
     * @return Collection of actions for this build with the new one added.
     */
    @Override
    public Collection<? extends Action> createFor(
            @SuppressWarnings("rawtypes") Run build) {
        final List<BuildEnvironmentDifferenceAction> buildActions = build
                .getActions(BuildEnvironmentDifferenceAction.class);
        final ArrayList<Action> actions = new ArrayList<Action>();
        if (buildActions.isEmpty()) {
            // the if makes sure that only builds having their Jobs instances of
            // AbstractProject get this action.
            if (build instanceof AbstractBuild<?, ?>
                    && build.getParent() instanceof AbstractProject<?, ?>) {
                final BuildEnvironmentDifferenceAction newAction = new BuildEnvironmentDifferenceAction(
                        build);
                actions.add(newAction);
            }
            return actions;
        } else {
            return buildActions;
        }
    }

}
