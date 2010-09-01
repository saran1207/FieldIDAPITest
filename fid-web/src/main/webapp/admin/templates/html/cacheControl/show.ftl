<h2>Cache Control</h2>

<@s.actionerror />
<@s.actionmessage />

<@s.url id="reloadTenantCacheUrl" namespace="/admin" value="reloadTenantCache.action" />
<@s.url id="reloadModDateCacheUrl" namespace="/admin" value="reloadSetupDataLastModDatesCache.action" />
<@s.url id="reloadSafetyNetworkCacheUrl" namespace="/admin" value="reloadSafetyNetworkConnectionCache.action" />

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
	<div>Tenant Cache: </div>
	<button type="button" onclick="window.location = '<@s.text name="%{reloadTenantCacheUrl}" />';">Reload</button>
</div>	

<div class="cacheReload">
	<div>Mod Date Cache: </div>
	<button type="button" onclick="window.location = '<@s.text name="%{reloadModDateCacheUrl}" />';">Reload</button>
</div>	

<div class="cacheReload">
	<div>Safety Network Conn Cache: </div>
	<button type="button" onclick="window.location = '<@s.text name="%{reloadSafetyNetworkCacheUrl}" />';">Reload</button>
</div>

<div class="cacheTable">
	<h2>Tenant Cache - Tenants</h2>
	<table class="list">
	<tr><th>id</th><th>Name</th></tr>
	<@s.iterator value="tenants">
		<tr>
			<td><@s.property value="id" /></td>
			<td><@s.property value="name" /></td>
		</tr>
	</@s.iterator>
	</table>
</div>

<div class="cacheTable">
	<h2>Tenant Cache - PrimaryOrgs</h2>
	<table class="list">
	<tr><th>id</th><th>Name</th></tr>
	<@s.iterator value="primaryOrgs">
		<tr>
			<td><@s.property value="id" /></td>
			<td><@s.property value="name" /></td>
		</tr>
	</@s.iterator>
	</table>
</div>