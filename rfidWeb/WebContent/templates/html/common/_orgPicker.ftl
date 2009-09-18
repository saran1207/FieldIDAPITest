<@n4.includeScript src="orgPicker"/>
<#assign name>
	<div id="orgSelector" style="display:none" targetId="">
		<div id="orgSearch" style="display:none">
			<@s.form action="orgs" namespace="/ajax" theme="fieldid" cssClass="ajaxSearch">
				<@s.hidden name="orgTypeFilter" id="orgFilter"/>
				<div class="infoSet">
					<@s.textfield name="searchName" /> <@s.submit key="label.search"/>
				</div>
			</@s.form>
			<div id="orgPickerResults">
			</div>	
		</div>
		<div id="orgBrowser" >
			<@s.form action="orgs" namespace="/ajax" theme="fieldid" cssClass="fullForm" >
				<@s.hidden name="orgId" value="-1" id="orgPickerCurrentOrg"/>
				<div class="infoSet">
					<label><@s.text name="label.org"/></label>
					<@s.select name="org" id="orgList"/>
				</div>	
				<div class="infoSet">
					<label><@s.text name="label.org"/></label>
					<@s.select name="customer" id="customerList"/>
				</div>
				<div class="infoSet">
					<label><@s.text name="label.org"/></label>
					<@s.select name="division" id="divisionList"/>
				</div>
				<div>
					<@s.submit key="label.select" id="selectOrg"/> <@s.submit key="label.cancel" id="cancelOrgSelect"/>
				</div>	
			</@s.form>
				
		</div>
	</div>
</#assign>
<style>
	#orgSelector { 
		border: 1px solid black;
		background-color: white;
	}
</style>
<@n4.includeScript>
	orgListUrl = "<@s.url action="orgList" namespace="/ajax/"/>";
	document.observe("dom:loaded", function() {
		if ($('orgSearch') == null) {
			$('fieldidBody').insert("${name?js_string}");
			$$('.ajaxSearch').each(function(element) {element.observe("submit", ajaxFormEvent)});
			$('selectOrg').observe("click", selectOrg);
			$('cancelOrgSelect').observe("click", cancelOrgBrowse);
			$$('#orgBrowser select').each(function(element) { element.observe('change', changeOrgList) });
		}
	});
</@n4.includeScript>
