<div id="${errorPrefix!""}formErrors" <#if action.fieldErrors.empty >style="display:none"</#if>>
	<#if action.fieldErrors?size != 0 >
		<div class="formErrors" >
			<h2><@s.text name="label.errors"/></h2>
			<@s.fielderror />
		</div>
	</#if>
</div>