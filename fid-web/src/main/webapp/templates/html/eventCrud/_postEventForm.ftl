
<h2><@s.text name="label.posteventinformation"/></h2>
<div  class="infoSet">
	<label class="label"><@s.text name="label.comments"/></label>
	<span class="fieldHolder">
		<@s.select name="commentTemplate" list="commentTemplates" listKey="id" listValue="displayName" emptyOption="true" onchange="changeComments(this)" theme="fieldidSimple"/> 
		<@s.textarea name="comments" id="comments"  cols="50" rows="3" theme="fieldidSimple"/>
	</span>
	
</div>

<#if action.isParentAsset() >
	<div  class="infoSet">
		<label class="label"><@s.text name="label.printable"/></label>
		<span class="fieldHolder">
			<@s.checkbox name="printable" theme="fieldidSimple" /> <@s.text name="label.printableexplination"/>
		</span> 
	</div>
	
	<div  class="infoSet">
		<label class="label"><@s.text name="label.assetstatus"/></label>
		<@s.select name="assetStatus" list="assetStatuses" listKey="uniqueID" listValue="name" headerKey="" headerValue="" />
	</div>
	
	<#if inspection.new >
		<#include "_schedules.ftl"/>
	</#if>	
	
</#if>

