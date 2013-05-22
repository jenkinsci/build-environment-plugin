//package org.jenkinsci.plugins.buildenvironment.actions;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import hudson.Extension;
//import hudson.model.Action;
//import hudson.model.TransientBuildActionFactory;
//import hudson.model.AbstractBuild;
//import hudson.model.AbstractProject;
//import hudson.model.Run;
//
///**
// * Action Factory for the build view. 
// * 
// * @author yboev
// * 
// */
//@Extension
//public class BuildEnvironmentBuildActionFactory extends BuildActionFactory {
//
//    /**
//     * @param build
//     *            the current build
//     * @return Collection of actions for this build with the new one added.
//     */
//    @Override
//    public Collection<? extends Action> createFor(
//            @SuppressWarnings("rawtypes") Run build) {
//        final List<BuildEnvironmentBuildAction> buildActions = build
//                .getActions(BuildEnvironmentBuildAction.class);
//        final ArrayList<Action> actions = new ArrayList<Action>();
//        if (buildActions.isEmpty()) {
//            // the if makes sure that only builds having their Jobs instances of
//            // AbstractProject get this action.
//            if (build instanceof AbstractBuild<?, ?>
//                    && build.getParent() instanceof AbstractProject<?, ?>) {
//                final BuildEnvironmentBuildAction newAction = new BuildEnvironmentBuildAction(
//                        build);
//                actions.add(newAction);
//            }
//            return actions;
//        } else {
//            return buildActions;
//        }
//    }
//
//}