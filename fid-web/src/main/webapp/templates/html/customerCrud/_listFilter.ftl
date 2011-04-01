<div class="listFilter quickForm" >
	<div id="listFilterHeader">
		<h2><@s.text name="hbutton.filter"/></h2>
		&nbsp;
		<span class="egColor"><@s.text name="message.filter_customers"/></span>
	</div>
	<@s.form id="listFilterForm" action="${filterAction}" method="get">
		<@s.textfield key="label.name" name="nameFilter" id="nameFilter" labelposition="left" />
		<@s.textfield key="label.id" name="idFilter" id="idFilter" labelposition="left" />
		<@s.select key="label.organization" name="orgFilter" id="orgFilter" list="parentOrgs" listKey="id" listValue="name" emptyOption="true" labelposition="left"/>
		<div class="formAction">
			<@s.submit key="hbutton.filter" />
			<span><@s.text name="label.or" /></span>
			<a href="javascript:void(0);" onClick="$('listFilterForm').reset();"> <@s.text name="hbutton.clear"/></a>
		</div>
	</@s.form>
</div>