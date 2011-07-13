package com.n4systems.util.selection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiIdSelection implements Serializable {

    private Set<Long> ids = new HashSet<Long>();

    public void addId(Long id) {
        ids.add(id);
    }

    public void addAllIds(Collection<Long> idsToAdd) {
        ids.addAll(idsToAdd);
    }

    public void removeAllIds(Collection<Long> idsToRemove) {
        ids.removeAll(idsToRemove);
    }

    public void removeId(Long id) {
        ids.remove(id);
    }

    public boolean containsId(Long id) {
        return ids.contains(id);
    }

    public int getNumSelectedIds() {
        return ids.size();
    }

    public List<Long> getSelectedIds() {
        return new ArrayList<Long>(ids);
    }

    public void clear() {
        ids.clear();
    }

}
