<p>
	<@s.text name="label.import_started"><@s.param>${action.formatDateTime(now)}</@s.param><@s.param>${estimatedImportTime}</@s.param></@s.text>
</p>
<p>
	<strong><@s.text name="label.please_note"/>:</strong> <@s.text name="warning.catalog_import"/>
</p>
<div class="stepAction"/>
	<@s.url id="doneUrl" action="safetyNetworkList" namespace="/" />
	<@s.submit theme="fieldid" key="label.return_to_safety_network" onclick="return redirect('${doneUrl}');"/>
</div>