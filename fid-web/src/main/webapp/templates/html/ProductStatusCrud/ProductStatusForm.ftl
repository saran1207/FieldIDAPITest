<title>
	<#if uniqueID?exists  >
		${action.setPageType('product_status', 'edit')!}
	<#else>
		${action.setPageType('product_status', 'add')!}
	</#if>
</title>

<@s.form action="productStatusEdit!save" cssClass="inputForm oneColumn" theme="css_xhtml">
	<@s.hidden name="uniqueID" />
	<div class="formRowHolder">
		<@s.textfield key="label.name" name="name" size="30" labelposition="left"/>
	</div>
	<div class="formAction">
		<@s.submit key="hbutton.cancel" name="redirect-action:productStatusList" />
		<@s.submit key="hbutton.save" />
	</div>
</@s.form>  