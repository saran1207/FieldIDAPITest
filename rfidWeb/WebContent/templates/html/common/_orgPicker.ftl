<@n4.includeScript src="orgPicker"/>
<#assign name>
	<div id="orgSearch" style="display:none">
		<@s.form action="orgs" namespace="/ajax" theme="fieldid" cssClass="ajaxSearch">
			<@s.hidden name="orgTypeFilter" id="orgFilter"/>
			<div class="infoSet">
				<@s.textfield name="searchName" /> <@s.submit key="label.search"/>
			</div>
		</@s.form>
		<div id="orgPickerResults" targetId="">
		</div>	
	</div>
</#assign>
<style>
	#orgSearch { 
		border: 1px solid black;
		background-color: white;
	}
</style>
<@n4.includeScript>
	document.observe("dom:loaded", function() {
		if ($('orgSearch') == null) {
			$('fieldidBody').insert("${name?js_string}");
			$$('.ajaxSearch').each(function(element) {element.observe("submit", ajaxFormEvent)});
		}
	});
</@n4.includeScript>
