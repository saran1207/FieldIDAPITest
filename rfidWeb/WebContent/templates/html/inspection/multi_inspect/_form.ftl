<head>
	<@n4.includeScript src="inspection" />
	<@n4.includeStyle type="page" href="inspection" />
	<#include "/templates/html/common/_calendar.ftl"/>
	<#include "/templates/html/common/_orgPicker.ftl"/>
	
	<@n4.includeScript src="commentTemplates"/>
		
	<@n4.includeScript>
		changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="/ajax"   />';
	</@n4.includeScript>
</head>

<@s.form action="inspectionCreate" namespace="/multiInspect/ajax" id="inspectionCreate" cssClass="crudForm" theme="simple">
	<#include "/templates/html/common/_formErrors.ftl"/>
	<@s.hidden name="type" value="${eventTypeId}"/>
	<@s.hidden name="scheduleId" value="0"/>

	<@s.hidden name="productId" id="productId"/>
	
	
	<h2><@s.text name="label.customerinformation"/></h2>
	
	<p class="infoSet">
		<label><@s.text name="label.owner"/></label>
		<span><@n4.orgPicker name="modifiableInspection.owner" required="true" id="ownerId" /></span>
	</p>	
	
	<p class="infoSet">
		<label><@s.text name="label.location"/></label>
		<span><@s.textfield name="modifiableInspection.location" /></span>
	</p>
	
	<h2><@s.text name="label.inspectiondetails"/></h2>
	<p class="infoSet">
		<label class="label"><@s.text name="label.inspector"/></label>
		<@s.select name="inspector" list="inspectors" listKey="id" listValue="name"  />
	</p>
	<p class="infoSet">
		<label class="label"><@s.text name="label.inspectiondate"/></label>
		<@s.datetimepicker id="inspectionDate" name="modifiableInspection.inspectionDate" theme="fieldidSimple"  type="dateTime"/>
	</p>
	
	
	<#include "/templates/html/inspectionCrud/_attributes.ftl"/>
		
	<#assign formInspection=inspection>
	<#assign form_action="ADD">
	<#assign identifier="inspectionForm">
	<#include "/templates/html/inspectionCrud/_inspection.ftl" />
	
	

	<h2><@s.text name="label.postinspectioninformation"/></h2>
	<p class="infoSet">
		<label><@s.text name="label.comments"/></label>
		<span>
			<@s.select name="commentTemplate" list="commentTemplates" listKey="id" listValue="displayName" emptyOption="true" onchange="changeComments(this)"/> 
			<@s.textarea name="comments" id="comments"  cols="50" rows="3"/>
		</span>
		
	</p>
	
	<p class="infoSet">
		<label class="label"><@s.text name="label.printable"/></label>
		<@s.checkbox name="printable" /> <@s.text name="label.printableexplination"/>
	</p>
	
	<p class="infoSet">
		<label><@s.text name="label.productstatus"/></label>
		<span>
			<@s.select name="productStatus" list="productStatuses" listKey="uniqueID" listValue="name" headerKey="" headerValue="" />
		</span>
	</p>
	
	<#assign noAutoSuggest=true/>
	<#include "/templates/html/inspectionCrud/_schedules.ftl" />
	
</@s.form>
