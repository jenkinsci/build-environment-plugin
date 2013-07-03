package org.jenkinsci.plugins.buildenvironment.actions.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {
    public static Map<String, String> filterMap(
            Map<String, String> map, List<String> stringList) {
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
        for(String key : keysToBeRemoved) {
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
    
    public static List<String> getPasswordRestrictionPatterns() {
        final ArrayList<String> list = new ArrayList<String>(1<<2);
        list.add("PASS");
        list.add("KEY");
        list.add("SECRET");
        list.add("ENCRYPTED");
        return list;
    }
}
