package org.jenkinsci.plugins.buildenvironment.actions.utils;

import hudson.model.Hudson;

import java.awt.Color;
import java.io.File;

import jenkins.model.Jenkins;

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
    public static final String URL = "compare_environment";
    /**
     * Not Applicable, Not Available.
     */
    public static final String NA = "N/A";
    /**
     * Name.
     */
    public static final String NAME = "Compare environment";
    /**
     * Icon.
     */
    public static final String MENUICONFILENAME = "/plugin/build-environment/icons/menu_icon.png";
    /**
     * Summary icon.
     */
    public static final String SUMMARYICONFILENAME = "/plugin/build-environment/icons/summary_icon.png";
    /**
     * Background color when entries are different.
     */
    public static final Color BGDIFFERENCECOLOR = new Color(255, 68, 68);
    /**
     * Background color when entries are the same.
     */
    public static final Color BGNODIFFERENCECOLOR = new Color(187, 187, 187);

    /**
     * Returns the String of the color for entries that are not different.
     * @return the color as String
     */
    public static String getBackgroundNoDifferenceColorAsString() {
        return Integer.toHexString(Constants.BGNODIFFERENCECOLOR.getRGB())
                .substring(2, 7);
    }
    
    /**
     * Returns the String of the color for entries that are different.
     * @return the color as String
     */
    public static String getBackgroundDifferenceColorAsString() {
        return Integer.toHexString(Constants.BGDIFFERENCECOLOR.getRGB())
                .substring(2, 7);
    }

}
