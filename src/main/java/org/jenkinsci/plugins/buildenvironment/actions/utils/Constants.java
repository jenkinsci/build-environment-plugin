package org.jenkinsci.plugins.buildenvironment.actions.utils;

import java.io.File;

/**
 * Global Constants.
 * 
 * @author yboev
 * 
 */
public final class Constants {
    /**
     * Url for this plugin.
     */
    public static final String URL = "build_environment";
    /**
     * Not Applicable, Not Available.
     */
    public static final String NA = "N/A";
    /**
     * Name.
     */
    public static final String NAME = "Build Environment";
    /**
     * Icon.
     */
    public static final String ICONFILENAME = "/plugin/build-environment/icons/internet-mail.png";
    
    public static final String FILE_BASE = Constants.getHome() + "userContent/build-environment-history/";

    private static String getHome() {
        String s = System.getProperty("HUDSON_HOME");
        if (s == null) {
            s = System.getProperty("JENKINS_HOME");
        }
        if (s == null) {
            s = (new File(System.getProperty("user.dir"))).getParent();
        }
        if (s != null && !s.endsWith("/"))
            s = s + "/";
        if (s != null && !s.startsWith("/"))
            s = "/" + s;
        return s;
    }
}
