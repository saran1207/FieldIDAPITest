<head>
	<@n4.includeStyle type="page" href="signUp"/>
	<@n4.includeScript src="signUp"/>
	<@n4.includeScript>
		pricingUrl = '<@s.url action="increaseEmployeeLimitPriceCheck" namespace="/ajax"/>';
		pricingFormId = 'upgradeAccount';
		updatingMessage = '<@s.text name="label.updating_cost"/>';
	</@n4.includeScript>
	<@n4.includeScript>
		var employeeLimit = ${employeeLimit!"0"};
		function updateTotal() {
			var additionalEmployee = $('additionalEmployee');
			var additionalEmployeeError = $('additionalEmployeeError');
			var totalEmployeeContainer = $('totalEmployee');
			var limitError = $('limitError');
			
			
			additionalEmployee.removeClassName('inputError');
			additionalEmployeeError.hide();
			totalEmployeeContainer.removeClassName('inputError');
			limitError.hide();
			
			var currentEmployee = parseInt($('currentEmployee').innerHTML);
			var increment = additionalEmployee.getValue();
			
			var total = currentEmployee;
			
			if (increment == null || isNaN(parseInt(increment))  || increment < 1) {
				additionalEmployee.addClassName('inputError');
				additionalEmployeeError.show();
			} else {
				total = currentEmployee + parseInt(increment);
			} 
			
			if (employeeLimit && total > employeeLimit) {
				totalEmployeeContainer.addClassName("inputError");
				limitError.show();
			}
			
			totalEmployeeContainer.update(total);
		}
		
		document.observe("dom:loaded", function() {
				$('additionalEmployee').observe('keyup', updateTotal);
				
			});
	</@n4.includeScript>
</head>

${action.setPageType('account_settings', 'increase_employee')!}

<@s.form action="increaseEmployeeLimitComplete" id="upgradeAccount" cssClass="fullForm" theme="fieldid">
	<#include "/templates/html/common/_formErrors.ftl"/>
	
	<h3 class="clean">
		<@s.text name="label.increase_your_employee_limit"/>
	</h3>
	
	<p>
		<@s.text name="label.increase_employee_information"/>
	</p>
	
	<table id="incrementResource">
		<tr>
			<th><@s.text name="label.current_employee_limit"/></th>
			<td id="currentEmployee">${limits.employeeUsersMax}</td>
			<td class="messageCol"></td>	
		</tr>
		<tr>
			<th><@s.text name="label.additional_employee"/></th>
			<td>+<@s.textfield name="additionalEmployee" theme="fieldidSimple" id="additionalEmployee" cssClass="changesPrice"/></td>
			<td class="messageCol errorMessage" id="additionalEmployeeError" style="display:none">(<@s.text name="error.must_be_a_number_greater_than_0"/>)</td>	
		</tr>
		<tr>
			<th><@s.text name="label.employee_limit_after_purchase"/></td>
			<td id="totalEmployee">${limits.employeeUsersMax + additionalEmployee?default(1)}</td>
			<td class="messageCol errorMessage" id="limitError"  style="display:none">(<@s.text name="error.limited_employee_accounts"><@s.param>${employeeLimit!}</@s.param></@s.text>)</td>	
		</tr>
	</table>	
	<#assign charge_label="label.you_will_be_charged_this_immediately"/>
	<#include "../common/_charges.ftl"/>
	<#include "../common/_billing_information.ftl"/>
	
	<div class="actions">
		<p id="purchaseWarning"><strong><@s.text name="label.purchase_warning"/></strong></p>
		<@s.submit key="label.please_increase_my_employee_accounts"/>
		<@s.text name="label.or"/>
		<a href="<@s.url action="systemSettingsEdit"/>"><@s.text name="label.do_not_increase_my_employee_accounts"/></a>
	</div>
</@s.form>
	