<head>
	<@n4.includeScript src="inspection" />
	<@n4.includeStyle type="page" href="inspection" />
	<#include "/templates/html/common/_calendar.ftl"/>
</head>

<@s.form action="inspectionCreate" namespace="/multiInspect/ajax" id="createInspection" cssClass="crudForm" theme="simple">
	<@s.hidden name="type" value="${eventTypeId}"/>
	<@s.hidden name="scheduleId" value="0"/>

	<@s.hidden name="productId" id="productId"/>
	<@s.hidden name="location" id="location"/>
	<@s.hidden name="ownerId" id="ownerId"/>
	<@s.hidden name="productStatus" id="productStatusId"/>
	

	<h2><@s.text name="label.inspectiondetails"/></h2>
	<p class="infoSet">
		<label class="label"><@s.text name="label.inspector"/></label>
		<@s.select name="inspector" list="inspectors" listKey="id" listValue="name"  />
	</p>
	<p class="infoSet">
		<label class="label"><@s.text name="label.inspectiondate"/></label>
		<@s.datetimepicker id="inspectionDate" name="inspectionDate" theme="fieldidSimple"  type="dateTime"/>
	</p>
	
	
	<#include "/templates/html/inspectionCrud/_attributes.ftl"/>
		
	<#assign formInspection=inspection>
	<#assign form_action="ADD">
	<#assign identifier="inspectionForm">
	<#include "/templates/html/inspectionCrud/_inspection.ftl" />
	
	
	<h2><@s.text name="label.postinspectioninformation"/></h2>
	<p class="infoSet">
		<label class="label"><@s.text name="label.printable"/></label>
		<@s.checkbox name="printable" /> <@s.text name="label.printableexplination"/>
	</p>
</@s.form>
