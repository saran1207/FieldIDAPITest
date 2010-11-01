<title><@s.text name="title.quickevent" /></title>
<head>
	<style>
		.crudForm p span {
			padding-left: 10px;
		}
	</style>
</head>
<div class="crudForm">
	
	<span><@s.text name="label.chooseeventtype"/></span>
		
		

	<#list 	asset.type.inspectionTypes as type >
		<p>
			<span><a href="<@s.url action="selectEventAdd" assetId="${assetId}" type="${type.id}"/>">${type.name}</a></span>
		</p>
		
	</#list>
	<div class="formAction">
		<a href="<@s.url action="assetAdd" />"><@s.text name="label.returntoaddasset"/></a>
		
	</div>

</div>
