<div class="lightBoxView">
	
	<h2 class="modalHeader"><@s.text name="label.messagetitle" /></h2>
	<p id="modalBox_message" style="text-align:center">
		<#if actionErrors.isEmpty() >
			<#list actionMessages as message >
				${message}<br/>
			</#list>
		<#else>
			<#list actionErrors as error >
				${error}<br/>
			</#list>
		</#if>
	</p>
	<p style="text-align:center">
		<button onclick="Lightview.hide();"><@s.text name="label.ok"/></button>
	</p>
	
	
</div>

 