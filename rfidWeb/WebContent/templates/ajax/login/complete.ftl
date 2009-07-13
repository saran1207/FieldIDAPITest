<div>
	<div class="message">
		<@s.actionmessage />
	</div>
	<div class="error">
		<@s.actionerror />
	</div>
</div>
<div>
	<p class="instructions">
		<@s.text name="message.loggedbackin"/>
	<p>
	<p class="easyForm" id="closeButtonHolder">
		<input id="sessionTimeOutClose" type="submit" onclick="Lightview.hide()" value="<@s.text name="hbutton.close"/>" />
	</p>
</div>
<style>
	#closeButtonHolder {
		text-align: center;
		margin-top:10px
	}
</style>
<script type="text/javascript">
	new PeriodicalExecuter( 
		function(pe) {
 			pe.stop();
 			Lightview.hide();
 		}, 3 );
</script>