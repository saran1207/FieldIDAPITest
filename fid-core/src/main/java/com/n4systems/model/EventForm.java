package com.n4systems.model;

import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import org.hibernate.annotations.IndexColumn;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "eventforms")
public class EventForm extends ArchivableEntityWithTenant {

    @OneToMany(fetch= FetchType.LAZY, cascade= CascadeType.ALL, orphanRemoval = false)
    @IndexColumn(name="orderidx")
    @JoinTable(name="eventforms_criteriasections", joinColumns = {@JoinColumn(name="eventform_id")})
    private List<CriteriaSection> sections = new ArrayList<CriteriaSection>();

    @AllowSafetyNetworkAccess
    public List<CriteriaSection> getSections() {
        return sections;
    }

    public void setSections(List<CriteriaSection> sections) {
        this.sections = sections;
    }

    @Transient
    public List<CriteriaSection> getAvailableSections() {
        List<CriteriaSection> availableSections = new ArrayList<CriteriaSection>();
        for (CriteriaSection section : getSections()) {
            if (!section.isRetired()) {
                availableSections.add(section);
            }
        }
        return availableSections;
    }

}
