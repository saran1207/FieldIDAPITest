package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadWriteModel;

public class ApiCriteriaResult extends ApiReadWriteModel {
	private Long criteriaId;
	private List<ApiObservation> recommendations = new ArrayList<ApiObservation>();
	private List<ApiObservation> deficiencies = new ArrayList<ApiObservation>();
	private Long oneClickValue;
	private String textValue;
	private String selectValue;
	private String comboBoxValue;
	private String unitOfMeasurePrimaryValue;
	private String unitOfMeasureSecondaryValue;
	private byte[] signatureValue;
	private Date dateValue;
	private Double numberValue;
	private Long scoreValue;

	public Long getCriteriaId() {
		return criteriaId;
	}

	public void setCriteriaId(Long criteriaId) {
		this.criteriaId = criteriaId;
	}

	public List<ApiObservation> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<ApiObservation> recommendations) {
		this.recommendations = recommendations;
	}

	public List<ApiObservation> getDeficiencies() {
		return deficiencies;
	}

	public void setDeficiencies(List<ApiObservation> deficiencies) {
		this.deficiencies = deficiencies;
	}

	public Long getOneClickValue() {
		return oneClickValue;
	}

	public void setOneClickValue(Long oneClickValue) {
		this.oneClickValue = oneClickValue;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	public String getSelectValue() {
		return selectValue;
	}

	public void setSelectValue(String selectValue) {
		this.selectValue = selectValue;
	}

	public String getComboBoxValue() {
		return comboBoxValue;
	}

	public void setComboBoxValue(String comboBoxValue) {
		this.comboBoxValue = comboBoxValue;
	}

	public String getUnitOfMeasurePrimaryValue() {
		return unitOfMeasurePrimaryValue;
	}

	public void setUnitOfMeasurePrimaryValue(String unitOfMeasurePrimaryValue) {
		this.unitOfMeasurePrimaryValue = unitOfMeasurePrimaryValue;
	}

	public String getUnitOfMeasureSecondaryValue() {
		return unitOfMeasureSecondaryValue;
	}

	public void setUnitOfMeasureSecondaryValue(String unitOfMeasureSecondaryValue) {
		this.unitOfMeasureSecondaryValue = unitOfMeasureSecondaryValue;
	}

	public byte[] getSignatureValue() {
		return signatureValue;
	}

	public void setSignatureValue(byte[] signatureValue) {
		this.signatureValue = signatureValue;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public Double getNumberValue() {
		return numberValue;
	}

	public void setNumberValue(Double numberValue) {
		this.numberValue = numberValue;
	}

	public Long getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(Long scoreValue) {
		this.scoreValue = scoreValue;
	}

}
