package com.n4systems.util.selection;

import java.io.Serializable;
import java.util.*;

public class MultiIdSelection implements Serializable {

    private Set<Long> ids = new HashSet<Long>();
    private Map<Integer,Long> indexIdSelectionMap = new TreeMap<Integer, Long>();
    public boolean isSorted = false;


    public void addId(int index, Long id) {
        indexIdSelectionMap.put(index, id);
    }

    public void removeIndex(int index) {
        indexIdSelectionMap.remove(index);
    }

    public boolean containsIndex(int index) {
        return indexIdSelectionMap.keySet().contains(index);
    }

    public int getNumSelectedIds() {
        return indexIdSelectionMap.size();
    }

    public List<Long> getSelectedIds() {
        return new ArrayList<Long>(indexIdSelectionMap.values());
    }

    public void clear() {
        ids.clear();
        indexIdSelectionMap.clear();
    }

    public void clearIndexes() {

        isSorted = true;

        // create a new Map
        Map<Integer,Long> newIndexIdSelectionMap = new TreeMap<Integer, Long>();

        // loop through old map
        // set variable to new Map
        Iterator it = indexIdSelectionMap.entrySet().iterator();
        int i = 0;

        while(it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            newIndexIdSelectionMap.put(new Integer(--i), (Long)pairs.getValue());
            it.remove();
        }

        indexIdSelectionMap.clear();
        indexIdSelectionMap = newIndexIdSelectionMap;
    }

    public void validateIndexes(int currentPage, int itemsPerPage, List<Long> currentPageIdList) {

        Map<Integer, Long> addValues = new TreeMap<Integer, Long>();
        List<Integer> removeValues = new ArrayList<Integer>();
        Set<Integer> keySet = indexIdSelectionMap.keySet();

        for (Integer keyVal: keySet) {
            if (keyVal >= 0) {
                continue;
            }

            Long id = indexIdSelectionMap.get(keyVal);
            int currindex = currentPageIdList.indexOf(id) ;
            if (currindex >=0) {
                addValues.put(currindex  + (currentPage*itemsPerPage), id);
                removeValues.add(keyVal);
            }
        }

        // add new changed values and remove old changed values
        if (addValues.size() > 0) {
            indexIdSelectionMap.putAll(addValues);

            for (Integer rVal: removeValues) {
                indexIdSelectionMap.remove(rVal);
            }

        }

    }
}
