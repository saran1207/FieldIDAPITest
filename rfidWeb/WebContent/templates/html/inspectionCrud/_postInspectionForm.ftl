
<h2><@s.text name="label.posteventinformation"/></h2>
<p>
	<label><@s.text name="label.comments"/></label>
	<span>
		<@s.select name="commentTemplate" list="commentTemplates" listKey="id" listValue="displayName" emptyOption="true" onchange="changeComments(this)"/> 
		
		<@s.textarea name="comments" id="comments"  cols="50" rows="3"/>
	</span>
	
</p>

<#if action.isParentProduct() >
	<p>
		<label><@s.text name="label.printable"/></label>
		<span><@s.checkbox name="printable" /> <@s.text name="label.printableexplination"/></span> 
	</p>
	
	<p>
		<label><@s.text name="label.productstatus"/></label>
		<span><@s.select name="productStatus" list="productStatuses" listKey="uniqueID" listValue="name" headerKey="" headerValue="" /></span>
	</p>
	
	<#if inspectionType.assignedToAvailable && form_action="ADD">
		<p>
			<label><@s.text name="label.assign_asset_to"/></label>
			<@s.select name="assignedToId" list="employees" listKey="id" listValue="displayName"/>
			<@s.hidden name="assignToSomeone" id="assignToSomeone" value="true"/>
		</p>
	<#elseif form_action="EDIT">
		<#include "_assigned_to.ftl"/>
	</#if>
	
	<#if inspection.new >
		<#include "_schedules.ftl"/>
	</#if>	
	
</#if>

