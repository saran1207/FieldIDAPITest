package com.n4systems.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class ScoreResultRange implements Serializable {

    @Enumerated(EnumType.STRING)
	@Column(nullable=false, name="comparator")
    private ScoreComparator comparator = ScoreComparator.BETWEEN;

    @Column(nullable=false, name="value1")
    private Double value1 = 0.0;

    @Column(name="value2")
    private Double value2;

    public ScoreComparator getComparator() {
        return comparator;
    }

    public void setComparator(ScoreComparator comparator) {
        this.comparator = comparator;
    }

    public Double getValue1() {
        return value1;
    }

    public void setValue1(Double value1) {
        this.value1 = value1;
    }

    public Double getValue2() {
        return value2;
    }

    public void setValue2(Double value2) {
        this.value2 = value2;
    }
}
