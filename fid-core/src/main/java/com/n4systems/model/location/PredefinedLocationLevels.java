package com.n4systems.model.location;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

import com.n4systems.model.parents.EntityWithTenant;


@Entity
@Table(name="predefined_location_levels")
public class PredefinedLocationLevels extends EntityWithTenant {

	@CollectionOfElements(fetch=FetchType.EAGER)
	@IndexColumn(name="orderidx")
	private List<LevelName> levels = new ArrayList<LevelName>();

	public List<LevelName> getLevels() {
		return levels;
	}
	
	public LevelName getNameForLevel(TreeNode location) {
		int levelNumber = location.levelNumber();
		if (levels.size() >=  levelNumber) {
			if (levels.get(levelNumber - 1) != null) {
				return levels.get(levelNumber - 1);
			}
		}
		
		return new LevelName();
	}
}
