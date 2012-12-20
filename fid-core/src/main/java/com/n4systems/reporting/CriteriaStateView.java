package com.n4systems.reporting;

import com.n4systems.model.*;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CriteriaStateView implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String section;
	private String criteria;
	private String state;
	private File stateImage;
    private Double sectionScoreTotal;
    private Double sectionScorePercentage;
	private Integer recommendations;
	private Integer deficiencies;
	private String compressedRecommendations;
	private String compressedDeficiencies;
    private String type;
	private List<CriteriaResultImageView> criteriaImages = new ArrayList<CriteriaResultImageView>();
    private String label;
	
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

	public List<CriteriaResultImageView> getCriteriaImages() {
		return criteriaImages;
	}

	public void setCriteriaImages(List<CriteriaResultImageView> criteriaImages) {
		this.criteriaImages = criteriaImages;
	}

    public Double getSectionScorePercentage() {
        return sectionScorePercentage;
    }

    public void setSectionScorePercentage(Double sectionScorePercentage) {
        this.sectionScorePercentage = sectionScorePercentage;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}