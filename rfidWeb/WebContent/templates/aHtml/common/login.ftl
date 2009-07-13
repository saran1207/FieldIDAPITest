<div class="ajaxView">
	<h2 class="modalHeader"><@s.text name="label.messagetitle" /></h2>
	<p class="instructions">
		<@s.text name="message.sessionexpired"/>
	</p>
	<p class="instructions">
		<@s.text name="message.clicklogintologbackin"/>
	</p>
	<p style="text-align:center">
		<button onclick="<@s.url action="login;" /><@s.text name="label.login"/></button>
	</p>
</div>