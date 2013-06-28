package org.jenkinsci.plugins.buildenvironment.data;

import hudson.XmlFile;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Hudson;
import hudson.model.Node;

import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.jenkinsci.plugins.buildenvironment.actions.utils.Constants;

import com.thoughtworks.xstream.mapper.CannotResolveClassException;

abstract public class Data {
    // private static final Logger LOGGER = Logger.getLogger(Data.class
    // .getName());

    private AbstractProject<?, ?> project;
    private AbstractBuild<?, ?> build;
    String name;
    String id;

    protected TreeMap<String, String> data;

    public Data(AbstractProject<?, ?> project, AbstractBuild<?, ?> build, String name, String id) {
        this.project = project;
        this.build = build;
        this.name = name;
        this.id = id;
    }

    public AbstractProject<?, ?> getProject() {
        return this.project;
    }

    public AbstractBuild<?, ?> getBuild() {
        return this.build;
    }
    
    public String getName() {
        return this.name;
    }

    public TreeMap<String, String> getData() {
        return this.data;
    }
    
    public String getId() {
        return this.id;
    }
}
