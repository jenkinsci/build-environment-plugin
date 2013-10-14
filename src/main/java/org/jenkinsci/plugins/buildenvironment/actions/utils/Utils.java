package org.jenkinsci.plugins.buildenvironment.actions.utils;

import hudson.model.AbstractBuild;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Utility class, contains some help methods and additional functionality.
 * 
 * @author yboev
 * 
 */
public final class Utils {

    /**
     * Filters the map according to what getPasswordRestrictionPatterns returns.
     * Made in order to hide passwords or secret phrases used during the build.
     * 
     * @param map
     *            the map being filtered.
     * @param restrictedStrings
     *            a list of strings that represent restrictions.
     * @return the filtered map, not containing entries that match one of the
     *         restriction Strings.
     */
    public static Map<String, String> filterMap(Map<String, String> map,
            Set<String> restrictedStrings) {
        final ArrayList<String> keysToBeRemoved = new ArrayList<String>();
        for (String currentRestriction : restrictedStrings) {
            for (String key : map.keySet()) {
                if (currentRestriction != null
                        && currentRestriction.equals(key)) {
                    keysToBeRemoved.add(key);
                }
            }
        }
        for (String key : keysToBeRemoved) {
            map.remove(key);
            map.put(key, "********");
        }
        return map;
    }

    /**
     * Returns a list of restricted properties such as passwords, so that they
     * are not shown with the rest of the information.
     * 
     * @return list with restricted Strings
     */
    public static Set<String> getPasswordRestrictionPatterns(
            AbstractBuild<?, ?> build) {
        return build.getSensitiveBuildVariables();
    }
}
