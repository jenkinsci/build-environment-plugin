//package org.jenkinsci.plugins.buildenvtest;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.logging.Logger;
//
//import hudson.EnvVars;
//import hudson.Extension;
//import hudson.Launcher;
//import hudson.model.BuildListener;
//import hudson.model.Environment;
//import hudson.model.AbstractBuild;
//import hudson.model.Run;
//import hudson.model.TaskListener;
//import hudson.model.listeners.RunListener;
//
//@Extension
//public class AddTestEnvironment extends RunListener<AbstractBuild<?, ?>> {
//
//    private static final Logger LOGGER = Logger
//            .getLogger(AddTestEnvironment.class.getName());
//
//    @Override
//    public Environment setUpEnvironment(AbstractBuild build, Launcher launcher,
//            BuildListener listener) throws IOException, InterruptedException,
//            Run.RunnerAbortedException {
//        LOGGER.info("=================setUp==================");
//    }
//
//    @Override
//    public void onStarted(AbstractBuild<?, ?> build, TaskListener listener) {
//        LOGGER.info("=================onStarted==================");
//    }
//}
