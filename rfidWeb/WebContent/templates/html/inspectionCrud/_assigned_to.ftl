<#if inspection.hasAssignToUpdate()>
	<p>
		<label><@s.text name="label.asset_assignment_changed_to"/></label>
		<span>${(inspection.assignedTo.assignedUser.userLabel)!action.getText('label.no_one')}</span>
	</p>
</#if>