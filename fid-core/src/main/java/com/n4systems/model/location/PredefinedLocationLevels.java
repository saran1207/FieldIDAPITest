package com.n4systems.model.location;

import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="predefined_location_levels")
public class PredefinedLocationLevels extends EntityWithTenant {

	@ElementCollection(fetch=FetchType.EAGER)
    @JoinTable(name="predefined_location_levels_levels", joinColumns = @JoinColumn(name="predefined_location_levels_id"))
	@OrderColumn(name="orderidx")
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
