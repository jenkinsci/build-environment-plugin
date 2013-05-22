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

    public Data(AbstractProject<?, ?> project, AbstractBuild<?, ?> build) {
        this.project = project;
        this.build = build;
    }

    public AbstractProject<?, ?> getProject() {
        return this.project;
    }

    public AbstractBuild<?, ?> getBuild() {
        return this.build;
    }

    public TreeMap<String, String> getData() throws IOException {
        TreeMap<String, String> env;
        try {
            XmlFile xml = new XmlFile(Hudson.XSTREAM, new File(
                    this.getFullFileName()));

            if (xml.exists()) {
                env = xmlToMap(xml);
                return env;
            }
        } catch (CannotResolveClassException e) {
            return null;
        }
        return null;
    }

    abstract protected String getFileName();
    
    protected String getFullFileName() {
        return Constants.FILE_BASE + this.getProject().getName() + "/" + this.getBuild().getId() + "/" + this.getFileName();
    }

    protected static TreeMap<String, String> xmlToMap(XmlFile file)
            throws IOException {
        return (TreeMap<String, String>) file
                .unmarshal(new TreeMap<String, String>());
    }
}
