package com.n4systems.model;

import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.AllowSafetyNetworkAccess;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventforms")
public class EventForm extends ArchivableEntityWithTenant {

    @OneToMany(fetch= FetchType.LAZY, cascade= CascadeType.ALL, orphanRemoval = false)
    @OrderColumn(name="orderidx")
    @JoinTable(name="eventforms_criteriasections", joinColumns = @JoinColumn(name="eventform_id"), inverseJoinColumns = @JoinColumn(name="sections_id"))
    private List<CriteriaSection> sections = new ArrayList<CriteriaSection>();

    @AllowSafetyNetworkAccess
    public List<CriteriaSection> getSections() {
        return sections;
    }

    public void setSections(List<CriteriaSection> sections) {
        this.sections.clear();
        this.sections.addAll(sections);
    }

    @Column(nullable=false, name="use_score_for_result")
    private boolean useScoreForResult;

    @Column(nullable=false, name="use_observation_count_for_result")
    private boolean useObservationCountForResult;

    @Enumerated(EnumType.STRING)
	@Column(nullable=false, name="score_calculation_type")
    private ScoreCalculationType scoreCalculationType = ScoreCalculationType.SUM;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="comparator", column = @Column(name="pass_comparator")),
		@AttributeOverride(name="value1", column = @Column(name="pass_value1")),
        @AttributeOverride(name="value2", column = @Column(name="pass_value2"))
	})
    private ResultRange passRange = new ResultRange();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="comparator", column = @Column(name="fail_comparator")),
        @AttributeOverride(name="value1", column = @Column(name="fail_value1")),
        @AttributeOverride(name="value2", column = @Column(name="fail_value2"))
    })
    private ResultRange failRange = new ResultRange();

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, name="observationcount_pass_calculation_type")
    private ScoreCalculationType observationcountPassCalculationType = ScoreCalculationType.SUM;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="comparator", column = @Column(name="observationcount_pass_comparator")),
            @AttributeOverride(name="value1", column = @Column(name="observationcount_pass_value1")),
            @AttributeOverride(name="value2", column = @Column(name="observationcount_pass_value2"))
    })
    private ResultRange observationcountPassRange = new ResultRange();

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, name="observationcount_fail_calculation_type")
    private ScoreCalculationType observationcountFailCalculationType = ScoreCalculationType.SUM;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="comparator", column = @Column(name="observationcount_fail_comparator")),
            @AttributeOverride(name="value1", column = @Column(name="observationcount_fail_value1")),
            @AttributeOverride(name="value2", column = @Column(name="observationcount_fail_value2"))
    })
    private ResultRange observationcountFailRange = new ResultRange();

    @ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=true)
    @JoinColumn(name="observationcount_group_id")
    private ObservationCountGroup observationCountGroup;

    @ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=true)
    @JoinColumn(name="observationcount_pass")
    private ObservationCount observationCountPass;

    @ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=true)
    @JoinColumn(name="observationcount_fail")
    private ObservationCount observationCountFail;

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

    public ResultRange getPassRange() {
        return passRange;
    }

    public void setPassRange(ResultRange passRange) {
        this.passRange = passRange;
    }

    public ResultRange getFailRange() {
        return failRange;
    }

    public void setFailRange(ResultRange failRange) {
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

    public boolean isUseObservationCountForResult() {
        return useObservationCountForResult;
    }

    public void setUseObservationCountForResult(boolean useObservationCountForResult) {
        this.useObservationCountForResult = useObservationCountForResult;
    }

    public ScoreCalculationType getObservationcountPassCalculationType() {
        return observationcountPassCalculationType;
    }

    public void setObservationcountPassCalculationType(ScoreCalculationType observationcountPassCalculationType) {
        this.observationcountPassCalculationType = observationcountPassCalculationType;
    }

    public ScoreCalculationType getObservationcountFailCalculationType() {
        return observationcountFailCalculationType;
    }

    public void setObservationcountFailCalculationType(ScoreCalculationType observationcountFailCalculationType) {
        this.observationcountFailCalculationType = observationcountFailCalculationType;
    }

    public ObservationCountGroup getObservationCountGroup() {
        return observationCountGroup;
    }

    public void setObservationCountGroup(ObservationCountGroup observationCountGroup) {
        this.observationCountGroup = observationCountGroup;
    }

    public ObservationCount getObservationCountPass() {
        return observationCountPass;
    }

    public void setObservationCountPass(ObservationCount observationCountPass) {
        this.observationCountPass = observationCountPass;
    }

    public ObservationCount getObservationCountFail() {
        return observationCountFail;
    }

    public void setObservationCountFail(ObservationCount observationCountFail) {
        this.observationCountFail = observationCountFail;
    }

    public ResultRange getObservationcountPassRange() {
        return observationcountPassRange;
    }

    public void setObservationcountPassRange(ResultRange observationcountPassRange) {
        this.observationcountPassRange = observationcountPassRange;
    }

    public ResultRange getObservationcountFailRange() {
        return observationcountFailRange;
    }

    public void setObservationcountFailRange(ResultRange observationcountFailRange) {
        this.observationcountFailRange = observationcountFailRange;
    }
}
