package org.jenkinsci.plugins.buildenvironment.actions.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * @param stringList
     *            a list of strings that represent restrictions.
     * @return the filtered map, not containing entries that match one of the
     *         restriction Strings.
     */
    public static Map<String, String> filterMap(Map<String, String> map,
            List<String> stringList) {
        final ArrayList<String> keysToBeRemoved = new ArrayList<String>();
        for (String currentString : stringList) {
            final Pattern pattern = Pattern.compile(currentString);
            for (String key : map.keySet()) {
                final Matcher matcher = pattern.matcher(key);
                if (matcher.find()) {
                    // hit => remove from map
                    keysToBeRemoved.add(key);
                }
            }
        }
        for (String key : keysToBeRemoved) {
            map.remove(key);
        }
        return map;
    }

    // public static String makePluralIfMany(int count) {
    // if(count == 1) {
    // return "";
    // }
    // return "s";
    // }

    /**
     * Returns a list of restricted properties such as passwords, so that they
     * are not shown with the rest of the information.
     * 
     * @return list with restricted Strings
     */
    public static List<String> getPasswordRestrictionPatterns() {
        final ArrayList<String> list = new ArrayList<String>(1 << 2);
        list.add("PASS");
        list.add("KEY");
        list.add("SECRET");
        list.add("ENCRYPTED");
        return list;
    }
}
