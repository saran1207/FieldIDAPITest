<#if inspection.hasAssignToUpdate()>
	<p>
		<label><@s.text name="label.assigned_to"/></label>
		<span>${(inspection.assignedTo.assignedUser.userLabel)!action.getText('label.not_assigned')}</span>
	</p>
</#if>