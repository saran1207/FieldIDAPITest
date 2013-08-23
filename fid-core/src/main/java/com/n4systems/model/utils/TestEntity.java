package com.n4systems.model.utils;

import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.persistence.localization.Localized;
import com.n4systems.persistence.localization.LocalizedText;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="test")
@Localized("test")
public class TestEntity extends EntityWithTenant {

    @Localized("text")
    private LocalizedText text;

    public LocalizedText getText() {
        return text;
    }

    public void setText(LocalizedText text) {
        this.text = text;
    }
}





