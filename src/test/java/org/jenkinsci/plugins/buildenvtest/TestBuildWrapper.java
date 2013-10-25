//package org.jenkinsci.plugins.buildenvtest;
//
//import java.util.Set;
//
//import org.kohsuke.stapler.DataBoundConstructor;
//
//import hudson.Extension;
//import hudson.Launcher;
//import hudson.model.BuildListener;
//import hudson.model.AbstractBuild;
//import hudson.model.AbstractProject;
//import hudson.tasks.BuildWrapper;
//import hudson.tasks.BuildWrapperDescriptor;
//
//public class TestBuildWrapper extends BuildWrapper {
//    
//    @DataBoundConstructor
//    public TestBuildWrapper() {
//        //noop
//    }
//    
//    @Override
//    public Environment setUp(AbstractBuild build, Launcher launcher,
//            BuildListener listener) {
//        return new Environment() {
//        };
//    }
//    
//    @Override
//    public void makeBuildVariables(AbstractBuild build, java.util.Map<String, String> variables) {
//        variables.put("asdf", "123456");
//    }
//    
//    @Override
//    public void makeSensitiveBuildVariables(AbstractBuild build, Set<String> sensitiveVariables) {
//        sensitiveVariables.add("asdf");
//    }
//
//    @Extension
//    public static final class DescriptorImpl extends BuildWrapperDescriptor {
//
//        @Override
//        public boolean isApplicable(AbstractProject<?, ?> item) {
//            return true;
//        }
//
//        @Override
//        public String getDisplayName() {
//            return "Test Build Descriptor";
//        }
//    }
//}