<head>
	<@n4.includeStyle href="listFilter" type="page"/>
</head>

<div class="listFilter quickForm" >
	<div id="listFilterHeader">
		<h2><@s.text name="label.filter"/></h2>
		&nbsp;
		<span class="egColor"><@s.text name="message.filter_customers"/></span>
	</div>
	<@s.form id="listFilterForm" action="${filterAction}" method="get">
		<@s.textfield key="label.name" name="nameFilter" id="nameFilter" labelposition="left" />
		<@s.textfield key="label.id" name="idFilter" id="idFilter" labelposition="left" />
		<@s.select key="label.organization" name="orgFilter" id="orgFilter" list="parentOrgs" listKey="id" listValue="name" headerKey="" headerValue="All" labelposition="left"/>
		<div class="formAction">
			<@s.submit key="hbutton.filter" />
			<span><@s.text name="label.or" /></span>
			<a href="javascript:void(0);" onClick="$('nameFilter').value = '';$('idFilter').value = '';$('orgFilter').selectedIndex = 0;$('listFilterForm').submit();"> <@s.text name="hbutton.clear"/></a>
		</div>
	</@s.form>
</div>