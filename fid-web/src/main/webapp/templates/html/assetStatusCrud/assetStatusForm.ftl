<title>
	<#if uniqueID?exists  >
		${action.setPageType('asset_status', 'edit')!}
	<#else>
		${action.setPageType('asset_status', 'add')!}
	</#if>
</title>

<@s.form action="assetStatusEdit!save" cssClass="inputForm oneColumn" theme="css_xhtml">
	<@s.hidden name="uniqueID" />
	<div class="formRowHolder">
		<@s.textfield key="label.name" name="name" size="30" labelposition="left"/>
	</div>
	<div class="formAction">
		<@s.submit key="hbutton.cancel" name="redirect-action:assetStatusList" />
		<@s.submit key="hbutton.save" />
	</div>
</@s.form>  