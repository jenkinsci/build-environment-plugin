package org.jenkinsci.plugins.buildenvironment.data;


import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.jenkinsci.plugins.buildenvironment.actions.utils.StringPair;

public class DataDifferenceObject {

    private String name;
    private Map<String, StringPair> difMap;
    
    public DataDifferenceObject(Data data1, Data data2) {
        this.name = data1.getName();
        this.calculateDifference(data1, data2);
    }
    
    public String getName() {
        return this.name;
    }
    
    public Map<String, StringPair> getMap() {
        return this.difMap;
    }
    
    public int getDifferentCount() {
        int count = 0;
        for(String key : this.difMap.keySet()) {
            if(this.difMap.get(key).areDifferent()) {
                count++;
            }
        }
        return count;
    }
    
    public Map<String, StringPair> getDifferenceOnly() {
        Map<String, StringPair> difOnlyMap = new TreeMap<String, StringPair>();
        for(String key : this.difMap.keySet()) {
            if(this.difMap.get(key).areDifferent()) {
                difOnlyMap.put(key, difMap.get(key));
            }
        }
        return difOnlyMap;
    }
    
    private void calculateDifference(Data data1, Data data2) {
        this.difMap = new TreeMap<String, StringPair>();
        Set<String> keySet = new TreeSet<String>();
        try {
            if (canMapBeRetrieved(data1)) {
                keySet.addAll(data1.getData().keySet());
            }
            if (canMapBeRetrieved(data2)) {
                keySet.addAll(data2.getData().keySet());
            }
            for (String key : keySet) {
                this.difMap.put(key, new StringPair(
                        checkMapAttribute(data1, key) ? data1.getData()
                                .get(key) : "",
                        checkMapAttribute(data2, key) ? data2.getData()
                                .get(key) : ""));
            }
        } catch (IOException e) {
            this.difMap = null;
        }
    }
    
    private boolean canMapBeRetrieved(Data data) throws IOException {
        return data != null && data.getData() != null;
    }

    private boolean checkMapAttribute(Data data, String key) throws IOException {
        return canMapBeRetrieved(data) && data.getData().containsKey(key);
    }
}
