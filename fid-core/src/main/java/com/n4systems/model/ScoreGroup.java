package com.n4systems.model;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.persistence.localization.Localized;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@Entity
@Table(name="score_groups")
@PrimaryKeyJoinColumn(name="id")
public class ScoreGroup extends ArchivableEntityWithTenant implements Listable, NamedEntity {

    @Column(nullable=false)
    private @Localized String name;

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
        this.scores.clear();
        this.scores.addAll(scores);
    }

    @Override
    public String getDisplayName() {
        return name;
    }

	public Score getScore(String scoreName) {
		for (Score score: getScores()) {
			if (score.getName().equalsIgnoreCase(scoreName)) {
				return score;
			}
		}
		return null;
	}
}
