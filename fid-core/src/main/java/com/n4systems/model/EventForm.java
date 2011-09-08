package com.n4systems.model;

import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import org.hibernate.annotations.IndexColumn;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @Column(nullable=false, name="use_score_for_result")
    private boolean useScoreForResult;

    @Enumerated(EnumType.STRING)
	@Column(nullable=false, name="score_calculation_type")
    private ScoreCalculationType scoreCalculationType = ScoreCalculationType.SUM;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="comparator", column = @Column(name="pass_comparator")),
		@AttributeOverride(name="value1", column = @Column(name="pass_value1")),
        @AttributeOverride(name="value2", column = @Column(name="pass_value2"))
	})
    private ScoreResultRange passRange = new ScoreResultRange();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="comparator", column = @Column(name="fail_comparator")),
        @AttributeOverride(name="value1", column = @Column(name="fail_value1")),
        @AttributeOverride(name="value2", column = @Column(name="fail_value2"))
    })
    private ScoreResultRange failRange = new ScoreResultRange();

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

    public ScoreResultRange getPassRange() {
        return passRange;
    }

    public void setPassRange(ScoreResultRange passRange) {
        this.passRange = passRange;
    }

    public ScoreResultRange getFailRange() {
        return failRange;
    }

    public void setFailRange(ScoreResultRange failRange) {
        this.failRange = failRange;
    }

    public ScoreCalculationType getScoreCalculationType() {
        return scoreCalculationType;
    }

    public void setScoreCalculationType(ScoreCalculationType scoreCalculationType) {
        this.scoreCalculationType = scoreCalculationType;
    }

    public boolean isUseScoreForResult() {
        return useScoreForResult;
    }

    public void setUseScoreForResult(boolean useScoreForResult) {
        this.useScoreForResult = useScoreForResult;
    }
}
