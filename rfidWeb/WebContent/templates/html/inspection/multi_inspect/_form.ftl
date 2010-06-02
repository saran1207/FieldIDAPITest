

<@s.form action="inspectionCreate" namespace="/multiInspect/ajax" id="inspectionCreate" cssClass="fullForm fluidSets" theme="fieldid">
	<#include "/templates/html/common/_formErrors.ftl"/>
	<@s.hidden name="type"/>
	<@s.hidden name="scheduleId" value="0"/>

	<@s.hidden name="productId" id="productId"/>
	
	
	
	<h2><@s.text name="label.customerinformation"/></h2>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.owner"/></label>
		<@n4.orgPicker name="modifiableInspection.owner" required="true" id="ownerId" />
	</div>	
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.location"/></label>
		<@s.textfield name="modifiableInspection.location" />
	</div>
	
	<h2>${inspection.type.name?html} <@s.text name="label.details"/></h2>
	<div class="infoSet">
		<label class="label"><@s.text name="label.performed_by"/></label>
		<@s.select name="performedBy" list="examiners" listKey="id" listValue="name"  />
	</div>
	<div class="infoSet">
		<label class="label"><@s.text name="label.date_performed"/></label>
		<@s.datetimepicker id="datePerformed" name="modifiableInspection.datePerformed"  type="dateTime"/>
	</div>
	
	
	<#setting url_escaping_charset='UTF-8'>	
	<#list inspectionType.infoFieldNames as infoField >
		<div class="infoSet">
			<label class="label">${infoField?html}:</label>
			<@s.textfield name="encodedInfoOptionMap['${infoField?url}']"/>
		</div>
	</#list>
		
	<#assign formInspection=inspection>
	<#assign form_action="ADD">
	<#assign identifier="inspectionForm">
	<#include "/templates/html/inspectionCrud/_inspection.ftl" />
	
	

	<h2><@s.text name="label.posteventinformation"/></h2>
	<div class="infoSet">
		<label class="label"><@s.text name="label.comments"/></label>
		<div class="fieldHolder">
			<@s.select name="commentTemplate" list="commentTemplates" listKey="id" listValue="displayName" emptyOption="true" onchange="changeComments(this)" theme="fieldidSimple"/> 
			<@s.textarea name="comments" id="comments"  cols="50" rows="3" theme="fieldidSimple"/>
		</div>
	</div>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.printable"/></label>
		<span class="fieldHolder">	
			<@s.checkbox name="printable" theme="simple"/> <@s.text name="label.printableexplination"/>
		</span>
	</div>
	
	<div class="infoSet">
		<label class="label"><@s.text name="label.productstatus"/></label>
			<@s.select name="productStatus" list="productStatuses" listKey="uniqueID" listValue="name" headerKey="" headerValue="" />
	</div>
	
	<#include "/templates/html/inspectionCrud/_schedules.ftl" />
	
</@s.form>
