<head>
	<script src="javascripts/json2.js" type="text/javascript"></script>
	<script type="text/javascript">
	function selectConfig() {
		var configKey = $("keySelect").getValue();
		var tenantId = $("tenantIdSelect").getValue();
	
		$('configValue').disabled = true;
		$('ajaxStatus').innerHTML = "working ... "
			
	   	var url = "<@s.url action="getConfigValue" namespace="/ajax" />?key=" + configKey + "&tenantId=" + tenantId;
	   	new Ajax.Request(url, {
			method: 'post',
			asynchronous: false,
			onSuccess: function(transport) {
				if(transport.statusText == "OK") {
					eval(transport.responseText);
					$('ajaxStatus').innerHTML += "done"
	   			} else {
	   				$('ajaxStatus').innerHTML = "Ajax request failed: status=" + transport.statusText + ", response code=" + transport.status;
	   			}
				$('configValue').disabled = false;
	   		}
		});
	}
	</script>
</head>

<h2>System Configuration</h2>

<@s.actionerror />
<@s.actionmessage />
<@s.form action="configCrud!save" method="post" theme="simple">
	<table>
		<tr>
			<th>Config Entry</th>
			<th>Tenant</th>
			<th>Value</th>
		</tr>
		<tr>
			<td><@s.select id="keySelect" name="key" list="configs" size="15" onchange="selectConfig();" /></td>
			<td><@s.select id="tenantIdSelect" name="tenantId" list="tenants" size="15" emptyOption="true" onchange="selectConfig();" /></td>
			<td><@s.textarea id="configValue" name="value" value="New Value" cols="60" rows="17" /></td>
		</tr>
		<tr>
			<td><@s.submit value="Save" /><@s.submit value="Delete" action="configCrud!remove"/></td>
		</tr>
	</table>
	<div id="ajaxStatus"></div>
</@s.form>
<br />

<script type="text/javascript">
	$('keySelect').selectedIndex = 0;
	$('tenantIdSelect').selectedIndex = 0;
	selectConfig();
</script>