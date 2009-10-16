<@n4.includeScript src="orgPicker"/>
<@n4.includeStyle href="orgPicker" type="feature"/>
<#assign name>
	<div id="orgSelector" style="display:none" targetId="">
		<div class="selections">
			<a href="#" id="switchOrgBrowser"><@s.text name="label.browse"/></a>
			<a href="#" id="switchOrgSearch"><@s.text name="label.search"/></a>
			<a href="#" id="closeOrgPicker"><img src="<@s.url value="/images/x.gif"/>" alt="x"/></a>
		</div>
		<div id="orgSearch" style="display:none">
			<@s.form action="orgs" namespace="/ajax" theme="fieldid" >
				<@s.hidden name="orgTypeFilter" cssClass="orgFilter"/>
				<div class="infoSet"><@s.textfield name="searchName" class="searchName" id="orgSearchName"/> <@s.submit key="label.search"/></div>
			</@s.form>
			<div id="orgPickerResults">
			</div>
			<div id="orgPickerLoading" style="display:none">	
				<#include "/templates/html/common/_loadingImage.ftl"/>	<@s.text name="label.loading"/>
			</div>
		</div>
		<div id="orgBrowser" >
			<@s.form action="orgList" id="orgBrowserForm" name="orgBrowserForm" namespace="/ajax" theme="fieldid" cssClass="fullForm" >
				<@s.hidden name="orgId" value="-1" id="orgPickerCurrentOrg"/>
				<@s.hidden name="orgTypeFilter" id="orgFilter" cssClass="orgFilter"/>
				<div class="infoSet">
					<label class="label" for="org"><@s.text name="label.org"/></label>
					<@s.select name="org" id="orgList"/>
				</div>	
				<div class="infoSet">
					<label class="label" for="customer"><@s.text name="label.customer"/></label>
					<@s.select name="customer" id="customerList"/>
				</div>
				<div class="infoSet">
					<label class="label" for="division"><@s.text name="label.division"/></label>
					<@s.select name="division" id="divisionList"/>
				</div>
				<div class="actions">
					<@s.submit key="label.select" id="selectOrg"/> <@s.submit key="label.cancel" id="cancelOrgSelect"/>
				</div>	
			</@s.form>
			<div id="orgBrowserLoading" style="display:none">
				<#include "/templates/html/common/_loadingImage.ftl"/>	<@s.text name="label.loading"/>
			</div>
		</div>
	</div>
</#assign>

<@n4.includeScript>
	orgListUrl = "<@s.url action="orgList" namespace="/ajax/"/>";
	document.observe("dom:loaded", function() {
		if ($('orgSelector') == null) {
			$('fieldidBody').insert("${name?js_string}");
			$('orgs').observe("submit", searchForOrgs);
			$('selectOrg').observe("click", selectOrg);
			$('cancelOrgSelect').observe("click", cancelOrgBrowse);
			$('closeOrgPicker').observe("click", cancelOrgBrowse);
			$$('#orgBrowser select').each(function(element) { element.observe('change', changeOrgList) });
			$('switchOrgBrowser').observe("click", function(event) { event.stop(); openOrgBrowser(); } );
			$('switchOrgSearch').observe("click", function(event) { event.stop(); openOrgSearch(); } );
			
		}
	});
</@n4.includeScript>
