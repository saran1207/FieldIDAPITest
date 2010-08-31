<div style="width: 500px; text-align: center;">
	<h2 class="modalHeader"><@s.text name="label.messagetitle" /></h2>
	<div id="modalBox_message">
		<#if actionErrors.isEmpty() >
			<p><@s.text name="message.downloadbeinggenerated" /></p>
			<p><@s.text name="message.emailpending" /></p>
			<p><@s.text name="message.seedownloadstatus" /></p>
		<#else>
			<#list actionErrors as error >
				${error}<br/>
			</#list>
		</#if>
	</div>
	<div style="text-align:center">
		<button style="padding: 2px 5px;" onclick="window.location.href='<@s.url namespace="/" action="showDownloads"/>'"><@s.text name="hbutton.tomydownloads" /></button>
		&nbsp;<@s.text name="label.or" />&nbsp;
		<button style="padding: 2px 5px;" onclick="Lightview.hide();"><@s.text name="hbutton.closemessage" /></button>
	</div>
</div>