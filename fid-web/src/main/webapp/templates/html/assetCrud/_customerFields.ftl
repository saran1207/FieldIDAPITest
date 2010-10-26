<div class="formRowHolder">
	<@s.select key="label.customer"  id="customer" name="owner" list="customers" listKey="id" listValue="name"  labelposition="left" onchange="customerChanged(this)">
		<#if !sessionUser.anEndUser >
			<@s.param name="headerKey"></@s.param>
			<@s.param name="headerValue"></@s.param>
		</#if>
	</@s.select>
	
	<@s.select id="division" key="label.division" name="division" list="divisions" listKey="id" listValue="name" labelposition="left" >
		<#if !sessionUser.inDivision >
			<@s.param name="headerKey"></@s.param>
			<@s.param name="headerValue"></@s.param>
		</#if>
	</@s.select>
</div>