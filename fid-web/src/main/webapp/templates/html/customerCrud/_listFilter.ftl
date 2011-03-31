<div class="listFilter quickForm" >
	<div id="listFilterHeader">
		<h2><@s.text name="hbutton.filter"/></h2>
		<span class="egColor"><@s.text name="message.filter_customers"/></span>
	</div>
	<@s.form action="${filterAction}" method="get">
		<@s.textfield key="label.name" name="nameFilter" id="nameFilter" labelposition="left" />
		<@s.textfield key="label.id" name="idFilter" id="idFilter" labelposition="left" />
		<@s.select key="label.organization" name="orgFilter" id="orgFilter" list="parentOrgs" listKey="id" listValue="name" emptyOption="true" labelposition="left"/>
		<div class="formAction">
			<@s.submit key="hbutton.filter" />
		</div>
	</@s.form>
</div>