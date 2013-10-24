package com.n4systems.util.selection;

import java.io.Serializable;
import java.util.*;

public class MultiIdSelection implements Serializable {

    private Set<Long> ids = new HashSet<Long>();
    private Map<Integer,Long> indexIdSelectionMap = new TreeMap<Integer, Long>();

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

}
