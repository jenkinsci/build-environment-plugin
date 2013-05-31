package org.jenkinsci.plugins.buildenvironment.actions.utils;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import java.util.ArrayList;
import java.util.List;

public class StringPair {
    
    String first;
    String second;
    
    public StringPair(String first, String second) {
        this.first = first;
        this.second = second;
    }
    
    public String getFirst() {
        return this.first;
    }
    
    public String getSecond() {
        return this.second;
    }
    
}
