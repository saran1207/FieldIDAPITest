<head>
	<@n4.includeScript src="swfobject"/>
	<@n4.includeScript>
		swfobject.registerObject("player","9.0.98","expressInstall.swf");
	</@n4.includeScript>
</head>
<object id="player" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" name="player" width="640" height="480">
	<param name="movie" value="<@s.url value="/flash/player-viral.swf"/>" />
	<param name="allowfullscreen" value="true" />
	<param name="allowscriptaccess" value="always" />
	<param name="flashvars" value="file=<@s.url value="/images/preview/fieldid.flv"/>&image=preview.jpg" />
	<object type="application/x-shockwave-flash" data="<@s.url value="/flash/player-viral.swf"/>" width="640" height="480">
		<param name="movie" value="player-viral.swf" />
		<param name="allowfullscreen" value="true" />

		<param name="allowscriptaccess" value="always" />
		<param name="flashvars" value="file=<@s.url value="/images/preview/fieldid.flv"/>&image=preview.jpg" />
		<p><a href="http://get.adobe.com/flashplayer">Get Flash</a> to see this player.</p>
	</object>
</object>	
	
