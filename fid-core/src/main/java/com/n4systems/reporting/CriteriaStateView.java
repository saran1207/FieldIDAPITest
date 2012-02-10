package com.n4systems.reporting;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Deficiency;
import com.n4systems.model.Recommendation;
import com.n4systems.model.State;

public class CriteriaStateView implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String section;
	private String criteria;
	private String state;
	private File stateImage;
    private Double sectionScoreTotal;
	private Integer recommendations;
	private Integer deficiencies;
	private String compressedRecommendations;
	private String compressedDeficiencies;
    private String type;
	
	public CriteriaStateView() {}
	
	public CriteriaStateView(CriteriaSection section, Criteria criteria, List<Recommendation> recommendations, List<Deficiency> deficiencies) {
		this.section = section.getTitle();
		this.criteria = criteria.getDisplayText();
		this.recommendations = recommendations.size();
		this.deficiencies = deficiencies.size();
		this.compressedDeficiencies = "";
		this.compressedRecommendations = "";
		for (Deficiency deficiency : deficiencies) {
			this.compressedDeficiencies += "-" + deficiency.getText() + "\n";
		}
		
		for (Recommendation recommendation : recommendations) {
			this.compressedRecommendations += "-" + recommendation.getText() + "\n";
		}
	}

    public void setStateButtonGroup(State state) {
        this.state = state.getDisplayText();
        this.stateImage = PathHandler.getButtonImageFile(state);
    }
	
	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String buttonName) {
		this.criteria = buttonName;
	}

	public String getState() {
		return state;
	}

	public void setState(String buttonStateValue) {
		this.state = buttonStateValue;
	}

	public File getStateImage() {
		return stateImage;
	}

	public void setStateImage(File buttonStateImage) {
		this.stateImage = buttonStateImage;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String buttonGroupName) {
		this.section = buttonGroupName;
	}

	public Integer getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(Integer recommendations) {
		this.recommendations = recommendations;
	}

	public Integer getDeficiencies() {
		return deficiencies;
	}

	public void setDeficiencies(Integer deficiencies) {
		this.deficiencies = deficiencies;
	}

	public String getCompressedRecommendations() {
		return compressedRecommendations;
	}

	public void setCompressedRecommendations(String compressedRecommendations) {
		this.compressedRecommendations = compressedRecommendations;
	}

	public String getCompressedDeficiencies() {
		return compressedDeficiencies;
	}

	public void setCompressedDeficiencies(String compressedDeficiencies) {
		this.compressedDeficiencies = compressedDeficiencies;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getSectionScoreTotal() {
        return sectionScoreTotal;
    }

    public void setSectionScoreTotal(Double sectionScoreTotal) {
        this.sectionScoreTotal = sectionScoreTotal;
    }
}