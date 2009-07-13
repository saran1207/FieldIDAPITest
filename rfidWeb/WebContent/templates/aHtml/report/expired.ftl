<div class="ajaxView">
	
	<h2 class="modalHeader"><@s.text name="label.messagetitle" /></h2>
	<p id="modalBox_message" style="text-align:center">
		
		<#list actionErrors as error >
			${error}<br/>
		</#list>
		
	</p>
	<p style="text-align:center">
		
		<button onclick="<@s.url action="report" /><@s.text name="label.ok"/></button>
	</p>
	
	
</div>