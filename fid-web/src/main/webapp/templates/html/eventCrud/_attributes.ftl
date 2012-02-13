	
	
	<#setting url_escaping_charset='UTF-8'>	
	<#list eventType.infoFieldNames as infoField >
		<div class="infoSet">
			<label class="label">${infoField?html}:</label>
			<span class="fieldHolder">
				<@s.textfield name="encodedInfoOptionMap['${infoField?url?js_string}']"/>
			</span>
		</div>
	 	
	</#list>