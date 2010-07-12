
<h2><@s.text name="label.posteventinformation"/></h2>
<div  class="infoSet">
	<label class="label"><@s.text name="label.comments"/></label>
	<span class="fieldHolder">
		<@s.select name="commentTemplate" list="commentTemplates" listKey="id" listValue="displayName" emptyOption="true" onchange="changeComments(this)" theme="fieldidSimple"/> 
		<@s.textarea name="comments" id="comments"  cols="50" rows="3" theme="fieldidSimple"/>
	</span>
	
</div>

<#if action.isParentProduct() >
	<div  class="infoSet">
		<label class="label"><@s.text name="label.printable"/></label>
		<span class="fieldHolder">
			<@s.checkbox name="printable" theme="fieldidSimple" /> <@s.text name="label.printableexplination"/>
		</span> 
	</div>
	
	<div  class="infoSet">
		<label class="label"><@s.text name="label.productstatus"/></label>
		<@s.select name="productStatus" list="productStatuses" listKey="uniqueID" listValue="name" headerKey="" headerValue="" />
	</div>
	
	<#if inspectionType.assignedToAvailable && form_action="ADD">
		<div  class="infoSet">
			<label class="label"><@s.text name="label.assign_asset_to"/></label>
			<@s.select name="assignedToId" list="employees" listKey="id" listValue="displayName" />
			<@s.hidden name="assignToSomeone" id="assignToSomeone" value="true"/>
		</div>
	<#elseif form_action="EDIT">
		<#include "_assigned_to.ftl"/>
	</#if>
	
	<#if inspection.new >
		<#include "_schedules.ftl"/>
	</#if>	
	
</#if>

