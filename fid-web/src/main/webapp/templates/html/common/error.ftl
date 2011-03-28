<title><@s.text name="title.notfound"/></title>

<div id="errorContainer">
	<h2><@s.text name="title.notfound"/></h2>
		<div id="errorList">
			<#if action.actionErrors.isEmpty()>
				<span class="errorMessage"><@s.text name="message.cannotloaddata"/></span>
			<#else>
				<@s.actionerror/>
			</#if>
		</div>
	<p>
		<@s.text name="message.cannotloaddata_support"/>
	</p>
</div>

<script type="text/javascript" >

	document.observe("dom:loaded", function() {
		$('error').hide();
	});


</script> 