package com.n4systems.taskscheduling.task;

import java.util.List;
import java.util.Map;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.Asset;
import com.n4systems.model.user.User;

public class MassUpdateAssetsTask extends MassUpdateTask {

	private Map<String, Boolean> values;
	private Asset asset;
	private User modifiedBy;
	private List<Long> ids;
	private String orderNumber;
	
	public MassUpdateAssetsTask(MassUpdateManager massUpdateManager, List<Long> ids, Asset asset, Map<String, Boolean> values, User modifiedBy, String orderNumber){
		super(massUpdateManager);
		this.ids = ids;
		this.asset = asset;
		this.values = values;
		this.modifiedBy = modifiedBy;
		this.orderNumber = orderNumber;
	}

	private MassUpdateAssetsTask(MassUpdateManager massUpdateManager) {
		super(massUpdateManager);
	}

    @Override
    protected String getExecutionDetails() {
        return " User: " + modifiedBy;
    }

    @Override
	protected void executeMassUpdate() throws UpdateFailureException, UpdateConatraintViolationException {
		massUpdateManager.updateAssets(ids, asset, values, modifiedBy, orderNumber);
	}

	@Override
	protected void sendFailureEmailResponse() {
		String subject="Mass update of assets failed";
		String body="Failed to update " + ids.size() + " assets";
		sendEmailResponse(subject, body, modifiedBy);		
	}

	@Override
	protected void sendSuccessEmailResponse() {
		String subject="Mass update of assets completed";
		String body="Updated " + ids.size() + " assets successfully";
		sendEmailResponse(subject, body, modifiedBy);		
	}

}
