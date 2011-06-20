<title><@s.text name="label.setup" /></title>
<head>
	<#include "/templates/html/common/_datetimepicker.ftl"/>
</head>


<@s.form action="dataLogSearch" id="searchForm" cssClass="crudForm twoColumns pageSection" theme="fieldid" method="get" >
	<#include "../common/_formErrors.ftl"/>
	<h2><@s.text name="label.search_settings"/></h2>
	
	<div class="sectionContent" >
		<div class="infoSet">
			<label for="logType"><@s.text name="label.type"/></label>
			<@s.select  name="logType" list="logTypes" listKey="name" listValue="name" emptyOption="true"  />
		</div>
		<div class="infoSet">
			<label for="logStatus"><@s.text name="label.status"/></label>
			<@s.select name="logStatus" list="logStatuses" listKey="name" listValue="name" emptyOption="true" />
		</div>
		
		
		<div class="container">
			<div class="infoSet">
				<label for="fromDate"><@s.text name="label.fdate"/></label>
				<@s.textfield  name="fromDate" cssClass="datepicker"/>
			</div>
			<div class="infoSet">
				<label for="toDate"><@s.text name="label.tdate"/></label>
				<@s.textfield  name="toDate" cssClass="datepicker"/>
			</div>
		</div>
	</div>
	
	<div class="formAction">
		<@s.submit key="label.Run"/>
	</div>
</@s.form >