package com.n4systems.model.saveditem;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("S")
public class SavedSearchItem extends SavedItem {

    @Override
    public String getTitleLabelKey() {
        return "label.search";
    }

}
