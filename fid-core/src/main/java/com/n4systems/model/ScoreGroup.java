package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.parents.EntityWithTenant;
import org.hibernate.annotations.IndexColumn;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="score_groups")
@PrimaryKeyJoinColumn(name="id")
public class ScoreGroup extends EntityWithTenant implements Listable {

    @Column(nullable=false)
    private String name;

    @OneToMany(fetch= FetchType.EAGER, cascade= CascadeType.ALL)
    @JoinTable(name="score_groups_scores", joinColumns = @JoinColumn(name="score_group_id"), inverseJoinColumns = @JoinColumn(name="score_id"))
    @IndexColumn(name="orderIdx")
    private List<Score> scores = new ArrayList<Score>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    @Override
    public String getDisplayName() {
        return name;
    }
}
