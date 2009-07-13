<div id="orderSearch">
	<@s.form method="get" action="searchOrder" theme="simple" cssClass="simple">
		
		<#if (tagOptions.size() > 1) >
			<@s.select id="tagOptionId" name="tagOptionId" list="tagOptions" listKey="id" listValue="text" />
		<#else>
			<label class="label"><@s.text name="${tagOption.text}"/></label>
			<@s.hidden id="tagOptionId" name="tagOptionId" value="${tagOption.id}" />
		</#if>

		<span><@s.textfield name="orderNumber" required="true" /></span>

		<@s.submit name="load" key="hbutton.load"/>
	</@s.form>
</div>
	
