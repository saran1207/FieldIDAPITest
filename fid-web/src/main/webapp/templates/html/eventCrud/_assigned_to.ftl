<#if event.hasAssignToUpdate()>
	<div class="infoSet">
		<label class="label"><@s.text name="label.assigned_to"/></label>
		<span class="fieldHolder">${(event.assignedTo.assignedUser.userLabel)!action.getText('label.unassigned')}</span>
	</div>
</#if>