	
	
	<#setting url_escaping_charset='UTF-8'>	
	<#list inspectionType.infoFieldNames as infoField >
		<div class="infoSet">	
			<label class="label">${infoField?html}:</label>
			<@s.textfield name="encodedInfoOptionMap['${infoField?url}']"/>
		</div>
	 	
	</#list>