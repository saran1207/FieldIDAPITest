<h3>${(action.currentPackageFilter().packageName?html)!} Plan</h3>
<@s.form id="planForm" action="savePlan" namespace="/adminAjax" theme="fieldidSimple" >
	<@s.hidden name="id"/>
	<div class="infoSet">
		<label><@s.text name="label.employee_accounts"/></label><span><@s.textfield name="userLimits.maxEmployeeUsers"/></span>
	</div>
	
	<div class="infoSet">
		<label><@s.text name="label.lite_user_accounts"/></label><span><@s.textfield name="userLimits.maxLiteUsers"/></span>
	</div>
	
	<div class="infoSet">
		<label><@s.text name="label.readonly_user_accounts"/></label><span><@s.textfield name="userLimits.maxReadOnlyUsers"/></span>
	</div>

	<div class="infoSet">
		<label><@s.text name="label.usage_based_user_accounts"/></label>
        <span>
            <@s.checkbox id="usageBasedUsersEnabled" name="userLimits.usageBasedUsersEnabled" onclick="toggleUsageBasedUserEvents(this.checked);"/>
            <@s.textfield id="usageBasedUserEvents" name="userLimits.usageBasedUserEvents" cssStyle="width:130px"/>
        </span>
	</div>

	<div class="planFormActions">
		<input id="updatePlanButton" type="button" onClick="updatePlan();" value="<@s.text name='label.save'/>" />
		<@s.text name="label.or"/> 
		<a href="javascript:void(0);" onClick="cancelPlan(${id});"><@s.text name="label.cancel"/></a>
		<img id="planFormLoading" class="loading" src="<@s.url value="../images/loading-small.gif"/>" />
	</div>
</@s.form>