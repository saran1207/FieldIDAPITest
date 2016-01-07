package com.fieldid.data.service;

import com.n4systems.model.Score;
import com.n4systems.model.ScoreGroup;
import com.n4systems.model.Tenant;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static com.n4systems.util.FunctionalUtils.bind;
import static com.n4systems.util.FunctionalUtils.map;

@Component
public class ScoreGroupMigrator extends DataMigrator<ScoreGroup> {

	public ScoreGroupMigrator() {
		super(ScoreGroup.class);
	}

	@Override
	@Transactional
	protected ScoreGroup copy(ScoreGroup srcGroup, Tenant newTenant, String newName) {
		ScoreGroup dstGroup = new ScoreGroup();
		dstGroup.setTenant(newTenant);
		dstGroup.setName(newName);
		dstGroup.getScores().addAll(map(srcGroup.getScores(), bind(this::migrateScore, newTenant)));
		persistenceService.save(dstGroup);
		return dstGroup;
	}

	private Score migrateScore(Tenant newTenant, Score srcScore) {
		Score dstButton = new Score();
		dstButton.setTenant(newTenant);
		dstButton.setName(srcScore.getName());
		dstButton.setValue(srcScore.getValue());
		dstButton.setNa(srcScore.isNa());
		return dstButton;
	}
}
