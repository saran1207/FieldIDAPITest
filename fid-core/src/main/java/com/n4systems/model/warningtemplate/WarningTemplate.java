package com.n4systems.model.warningtemplate;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This entity provides access to the warning_templates table, which contains Template Warning values for Procedure
 * Definitions.  Tenants may provide pre-set Warnings values which are then able to be selected when editing a Procedure
 * Definition.
 *
 * Extending <b>EntityWithTenant</b> gives us access to an "id" field, along with "created" and "modified" User and
 * Date fields (which are managed automagically by Hibernate.
 *
 * Created by Jordan Heath on 14-11-19.
 */
@Entity
@Table(name="warning_templates")
public class WarningTemplate extends EntityWithTenant implements Listable<Long>, NamedEntity {
    @Column(nullable=false, length=50)
    private String name;

    @Column(nullable=false, length=255)
    private String warning;

    public WarningTemplate() {
    }

    public WarningTemplate(String name, String warning) {
        this.name = name;
        this.warning = warning;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    @Override
    public String getDisplayName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
