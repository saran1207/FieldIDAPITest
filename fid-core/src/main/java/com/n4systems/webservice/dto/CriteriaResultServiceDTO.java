package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CriteriaResultServiceDTO extends AbstractBaseServiceDTO {
	public static final String TYPE_ONE_CLICK = "ONE_CLICK";
	public static final String TYPE_TEXT_FIELD = "TEXT_FIELD";
	public static final String TYPE_SELECT_FIELD = "SELECT_FIELD";
	public static final String TYPE_COMBO_BOX = "COMBO_BOX";
	public static final String TYPE_UNIT_OF_MEASURE = "UNIT_OF_MEASURE";
	public static final String SIGNATURE = "SIGNATURE";
	public static final String TYPE_DATE_FIELD = "DATE_FIELD";
	public static final String TYPE_NUMBER_FIELD = "NUMBER_FIELD";
	public static final String TYPE_SCORE = "SCORE";
	
	private long criteriaId;
	private long stateId;
	private long inspectionId;
	private List<ObservationResultServiceDTO> recommendations = new ArrayList<ObservationResultServiceDTO>();
	private List<ObservationResultServiceDTO> deficiencies = new ArrayList<ObservationResultServiceDTO>();
	private String type;
	private String textFieldValue;
	private String selectFieldValue;
	private String comboBoxFieldValue;
	private String unitOfMeasurePrimaryFieldValue;
	private String unitOfMeasureSecondaryFieldValue;
	private byte[] signatureImage;
	private Date dateFieldValue;
	private Double numberFieldValue;
	private long scoreId;
	
	public long getCriteriaId() {
		return criteriaId;
	}

	public void setCriteriaId(long criteriaId) {
		this.criteriaId = criteriaId;
	}

	public long getStateId() {
		return stateId;
	}

	public void setStateId(long stateId) {
		this.stateId = stateId;
	}

	public long getInspectionId() {
		return inspectionId;
	}

	public void setInspectionId(long inspectionId) {
		this.inspectionId = inspectionId;
	}

	public List<ObservationResultServiceDTO> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<ObservationResultServiceDTO> recommendations) {
		this.recommendations = recommendations;
	}

	public List<ObservationResultServiceDTO> getDeficiencies() {
		return deficiencies;
	}

	public void setDeficiencies(List<ObservationResultServiceDTO> deficiencies) {
		this.deficiencies = deficiencies;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTextFieldValue() {
		return textFieldValue;
	}

	public void setTextFieldValue(String textFieldValue) {
		this.textFieldValue = textFieldValue;
	}

	public String getSelectFieldValue() {
		return selectFieldValue;
	}

	public void setSelectFieldValue(String selectFieldValue) {
		this.selectFieldValue = selectFieldValue;
	}

	public String getComboBoxFieldValue() {
		return comboBoxFieldValue;
	}

	public void setComboBoxFieldValue(String comboBoxFieldValue) {
		this.comboBoxFieldValue = comboBoxFieldValue;
	}

	public String getUnitOfMeasurePrimaryFieldValue() {
		return unitOfMeasurePrimaryFieldValue;
	}

	public void setUnitOfMeasurePrimaryFieldValue(String unitOfMeasurePrimaryFieldValue) {
		this.unitOfMeasurePrimaryFieldValue = unitOfMeasurePrimaryFieldValue;
	}

	public String getUnitOfMeasureSecondaryFieldValue() {
		return unitOfMeasureSecondaryFieldValue;
	}

	public void setUnitOfMeasureSecondaryFieldValue(String unitOfMeasureSecondaryFieldValue) {
		this.unitOfMeasureSecondaryFieldValue = unitOfMeasureSecondaryFieldValue;
	}

	public byte[] getSignatureImage() {
		return signatureImage;
	}

	public void setSignatureImage(byte[] signatureImage) {
		this.signatureImage = signatureImage;
	}

	public void setDateFieldValue(Date dateFieldValue) {
		this.dateFieldValue = dateFieldValue;
	}

	public Date getDateFieldValue() {
		return dateFieldValue;
	}

	public void setNumberFieldValue(Double numberFieldValue) {
		this.numberFieldValue = numberFieldValue;
	}

	public Double getNumberFieldValue() {
		return numberFieldValue;
	}

	public void setScoreId(long scoreId) {
		this.scoreId = scoreId;
	}

	public long getScoreId() {
		return scoreId;
	}	
}
