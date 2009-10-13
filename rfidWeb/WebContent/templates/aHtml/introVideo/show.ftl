
<#assign flashVars>file=<@s.url value="/images/preview/fieldid.flv"/>&image=<@s.url value="/images/preview/preview.jpg"/></#assign>
<object id="player" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" name="player" width="640" height="480">
	<param name="movie" value="<@s.url value="/flash/player-viral.swf"/>" />
	<param name="allowfullscreen" value="true" />
	<param name="allowscriptaccess" value="always" />
	<param name="flashvars" value="${flashVars}" />
	<object type="application/x-shockwave-flash" data="<@s.url value="/flash/player-viral.swf"/>" width="640" height="480">
		<param name="movie" value="player-viral.swf" />
		<param name="allowfullscreen" value="true" />

		<param name="allowscriptaccess" value="always" />
		<param name="flashvars" value="${flashVars}" />
		<p><a href="http://get.adobe.com/flashplayer">Get Flash</a> to see this player.</p>
	</object>
</object>	
	
