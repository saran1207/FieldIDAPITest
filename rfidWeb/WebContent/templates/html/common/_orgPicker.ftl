<@n4.includeScript src="orgPicker"/>
<#assign name>
	<div id="orgSelector" style="display:none" targetId="">
		<div class="selections">
			<a href="#" id="switchOrgBrowser"><@s.text name="label.browse"/></a>
			<a href="#" id="switchOrgSearch"><@s.text name="label.search"/></a>
			<a href="#" id="closeOrgPicker">x</a>
		</div>
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
			<@s.form action="orgList" id="orgBrowserForm" name="orgBrowserForm" namespace="/ajax" theme="fieldid" cssClass="fullForm" >
				<@s.hidden name="orgId" value="-1" id="orgPickerCurrentOrg"/>
				<div class="infoSet">
					<label><@s.text name="label.org"/></label>
					<@s.select name="org" id="orgList"/>
				</div>	
				<div class="infoSet">
					<label><@s.text name="label.customer"/></label>
					<@s.select name="customer" id="customerList"/>
				</div>
				<div class="infoSet">
					<label><@s.text name="label.division"/></label>
					<@s.select name="division" id="divisionList"/>
				</div>
				<div class="actions">
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
	
	#orgSelector .selections {
		
		overflow:auto;
		border-bottom: 1px solid #000;
		margin-bottom:5px;
	}
	#orgSelector .selections .selected { 
		position:relative;
		top:1px;
	}
	#orgSelector .selections a {
		display:block;
		float:left;
		padding:5px;
		border:1px solid #000;
		border-bottom:none;
	}
	#orgSelector .selections a#closeOrgPicker {
		float:right;
		clear:right;
	}
</style>
<@n4.includeScript>
	orgListUrl = "<@s.url action="orgList" namespace="/ajax/"/>";
	document.observe("dom:loaded", function() {
		if ($('orgSelector') == null) {
			$('fieldidBody').insert("${name?js_string}");
			$$('.ajaxSearch').each(function(element) {element.observe("submit", ajaxFormEvent)});
			$('selectOrg').observe("click", selectOrg);
			$('cancelOrgSelect').observe("click", cancelOrgBrowse);
			$('closeOrgPicker').observe("click", cancelOrgBrowse);
			$$('#orgBrowser select').each(function(element) { element.observe('change', changeOrgList) });
			$('switchOrgBrowser').observe("click", function(event) { event.stop(); openOrgBrowser(); } );
			$('switchOrgSearch').observe("click", function(event) { event.stop(); openOrgSearch(); } );
		}
	});
</@n4.includeScript>
