<#assign html >
	<div id="results">
		<div class="error">
			<@s.actionerror />
		</div>
	</div>
</#assign>

$('results').replace( '${html?js_string}' );
$('results').highlight();


${action.clearFlashScope()}