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
		<input id="sessionTimeOutClose" type="submit" onclick="closeLoginLightbox()" value="<@s.text name="hbutton.close"/>" />
	</p>
</div>
<style>
	#closeButtonHolder {
		text-align: center;
		margin-top:10px
	}
</style>
<script type="text/javascript">
    var interval = self.setInterval("onQuickLogin()", 750);
    function onQuickLogin() {
        interval = window.clearInterval(interval);
        closeLoginLightbox();
        if (typeof (window.onSuccessfulSessionRefresh) == 'function') {
            onSuccessfulSessionRefresh();
        }
    }
</script>