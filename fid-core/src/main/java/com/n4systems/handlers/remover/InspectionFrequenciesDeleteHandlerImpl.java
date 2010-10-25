package com.n4systems.handlers.remover;

import java.util.List;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.handlers.remover.summary.InspectionFrequencyDeleteSummary;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionType;
import com.n4systems.model.inspectiontype.InspectionFrequencySaver;
import com.n4systems.model.producttype.InspectionFrequencyListLoader;
import com.n4systems.persistence.Transaction;

public class InspectionFrequenciesDeleteHandlerImpl implements InspectionFrequenciesDeleteHandler {

	
	private final InspectionFrequencyListLoader listLoader;
	private final InspectionFrequencySaver saver;
	
	private InspectionType inspectionType;
	private AssociatedInspectionType associatedInspectionType;
	private Transaction transaction;

	
	public InspectionFrequenciesDeleteHandlerImpl(InspectionFrequencyListLoader listLoader, InspectionFrequencySaver saver) {
		super();
		this.listLoader = listLoader;
		this.saver = saver;
	}


	public void remove(Transaction transaction) {
		this.transaction = transaction;
		List<AssetTypeSchedule> frequencies = getInspectionFrequencies();
		deleteFrequencies(frequencies);
	}


	private List<AssetTypeSchedule> getInspectionFrequencies() {
		
		if (inspectionType != null) {
			return  getFrequenciesForInspectionType();
		} else if (associatedInspectionType != null) {
			return getFrequenciesForAssociatedInspectionType(associatedInspectionType);
		}
		
		throw new InvalidArgumentException("you must give the inspection type or the associated inspection type.");
	}

	
	private List<AssetTypeSchedule> getFrequenciesForInspectionType() {
		return listLoader.setInspectionTypeId(inspectionType.getId()).load(transaction);
	}

	
	private List<AssetTypeSchedule> getFrequenciesForAssociatedInspectionType(AssociatedInspectionType entity) {
		return listLoader.setInspectionTypeId(entity.getInspectionType().getId()).setAssetTypeId(entity.getAssetType().getId()).load(transaction);
	}
	
	
	private int deleteFrequencies(List<AssetTypeSchedule> frequencies ) {
		for (AssetTypeSchedule assetTypeSchedule : frequencies) {
			saver.remove(transaction, assetTypeSchedule);
		}
		return frequencies.size();
	}
	
	public InspectionFrequencyDeleteSummary summary(Transaction transaction) {
		this.transaction = transaction;
		InspectionFrequencyDeleteSummary inspectionFrequencyDeleteSummary = new InspectionFrequencyDeleteSummary();
		inspectionFrequencyDeleteSummary.setElementsToRemove(getInspectionFrequencies().size());
		return inspectionFrequencyDeleteSummary;
	}


	public InspectionFrequenciesDeleteHandler forAssociatedInspectionType(AssociatedInspectionType associatedInspectionType) {
		this.associatedInspectionType = associatedInspectionType;
		return this;
	}


	public InspectionFrequenciesDeleteHandler forInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}


	
		
}
