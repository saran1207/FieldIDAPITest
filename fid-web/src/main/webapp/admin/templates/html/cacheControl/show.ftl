<h2>Cache Control</h2>

<@s.actionerror />
<@s.actionmessage />

<@s.url id="reloadModDateCacheUrl" namespace="/admin" value="reloadSetupDataLastModDatesCache.action" />

<style>
	.cacheReload {
		padding-top: 20px;
	}
	
	.cacheReload div {
		float: left;
		width: 250px;
	}
	
	.cacheTable {
		padding-top: 40px;
	}
</style>

<div class="cacheReload">
	<div>Mod Date Cache: </div>
	<button type="button" onclick="window.location = '<@s.text name="%{reloadModDateCacheUrl}" />';">Reload</button>
</div>	
