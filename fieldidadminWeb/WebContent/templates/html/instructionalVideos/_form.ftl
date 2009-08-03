
<@s.hidden name="uniqueID" />
<p>
	<label>Title</label>
	<span><@s.textfield name="video.name" size="100"/></span>
</p>
<p>
	<label>URL</label>
	<span><@s.textfield name="video.url" size="100"/></span>
</p>
<@s.reset name="cancel" value="cancel" onclick="window.location='instructionalVideos.action'; return false;"/> <@s.submit value="save" name="save"/>