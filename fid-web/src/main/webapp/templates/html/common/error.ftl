<title><@s.text name="title.notfound"/></title>

<div id="errorContainer">
	<h2><@s.text name="title.notfound"/></h2>
		<div id="errorList">
			<@s.actionerror/>
		</div>
	<p>
		<@s.text name="message.cannotloaddata"/>
	</p>
</div>

<script type="text/javascript" >

	document.observe("dom:loaded", function() {
		$('error').hide();
	});


</script> 