<head>
	<style>
		.formAction div{
			padding: 0;
		}
		
		.formRowHolder div{
			padding-left: 12px;
		}
		
		.formRowHolder{
			background-image: none;
		}
	</style>
	<title>
		<#if uniqueID?exists  >
			${action.setPageType('asset_status', 'edit')!}
		<#else>
			${action.setPageType('asset_status', 'add')!}
		</#if>
	</title>
</head>

<@s.form action="assetStatusEdit!save" cssClass="crudForm" theme="css_xhtml">
	<@s.hidden name="uniqueID" />
	<div class="formRowHolder">
		<@s.textfield key="label.name" name="name" size="30" labelposition="left"/>
	</div>
	<div class="formAction">
		<@s.url id="cancelUrl" action="assetStatusList"/>
		<@s.submit key="hbutton.save" />
		<@s.text name="label.or"/>
		<a href="#" onclick="return redirect( '${cancelUrl}' );" />	<@s.text name="label.cancel"/></a>
	</div>
</@s.form> 