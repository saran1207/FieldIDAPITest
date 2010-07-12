<#if inspection.hasAssignToUpdate()>
	<div >
		<label class="label"><@s.text name="label.assigned_to"/></label>
		<span class="fieldHolder">${(inspection.assignedTo.assignedUser.userLabel)!action.getText('label.unassigned')}</span>
	</div>
</#if>