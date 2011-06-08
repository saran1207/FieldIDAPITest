<h3>${(action.currentPackageFilter().packageName?html)!} Plan</h3>
<@s.form id="planForm" action="savePlan" namespace="/adminAjax" theme="fieldidSimple" >
	<@s.hidden name="id"/>
	<div class="infoSet">
		<label><@s.text name="label.disk_space"/></label><span><@s.textfield name="diskSpace"/></span>
	</div>
	
	<div class="infoSet">
		<label><@s.text name="label.employee_accounts"/></label><span><@s.textfield name="users"/></span>
	</div>
	
	<div class="infoSet">
		<label><@s.text name="label.lite_user_accounts"/></label><span><@s.textfield name="liteUsers"/></span>
	</div>
	
	<div class="infoSet">
		<label><@s.text name="label.readonly_user_accounts"/></label><span><@s.textfield name="readonlyUsers"/></span>
	</div>

	<div class="infoSet">
		<label><@s.text name="label.assets"/></label><span><@s.textfield name="assets"/></span>
	</div>
		
	<div class="infoSet">
		<label><@s.text name="label.secondaryOrgs"/></label><span><@s.textfield name="secondaryOrgs"/></span>
	</div>

	<div class="planFormActions">
		<input id="updatePlanButton" type="button" onClick="updatePlan();" value="<@s.text name='label.save'/>" />
		<@s.text name="label.or"/> 
		<a href="javascript:void(0);" onClick="cancelPlan(${id});"><@s.text name="label.cancel"/></a>
		<img id="planFormLoading" class="loading" src="<@s.url value="../images/loading-small.gif"/>" />
	</div>
</@s.form>