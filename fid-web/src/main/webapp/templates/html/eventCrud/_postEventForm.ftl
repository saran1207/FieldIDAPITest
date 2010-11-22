
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
		<div class="fieldHolder">
			<label class="checkBoxLabel">
				<@s.checkbox name="printable" theme="fieldidSimple" /> <@s.text name="label.printableexplination"/>
			</label>
		</div> 
	</div>
	
	<div  class="infoSet">
		<label class="label"><@s.text name="label.assetstatus"/></label>
		<span class="fieldHolder">
			<@s.select name="assetStatus" list="assetStatuses" listKey="uniqueID" listValue="name" headerKey="" headerValue="" />
		</span>
	</div>
	
	<#if event.new >
		<#include "_schedules.ftl"/>
	</#if>	
	
</#if>

