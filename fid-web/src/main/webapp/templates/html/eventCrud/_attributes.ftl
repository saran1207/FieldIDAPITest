	
	
	<#setting url_escaping_charset='UTF-8'>	
	<#list eventType.infoFieldNames as infoField >
		<div class="infoSet">
			<label class="label">${infoField?html}:</label>
			<span class="fieldHolder">
				<@s.textfield name="encodedInfoOptionMap['${action.getBase64EncodedString(infoField)}']"/>
			</span>
		</div>
	 	
	</#list>