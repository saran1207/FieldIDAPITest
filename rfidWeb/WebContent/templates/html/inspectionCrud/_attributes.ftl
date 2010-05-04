	
	
	<#setting url_escaping_charset='UTF-8'>	
	<#list inspectionType.infoFieldNames as infoField >
		<p>	
			<label>${infoField}:</label>
			<span><@s.textfield name="encodedInfoOptionMap['${infoField?url}']"/></span>
		</p>
	 	
	</#list>