<head>
	<@n4.includeStyle type="page" href="signUp"/>
	<@n4.includeScript>
		var staffLimit = 5;
		function updateTotal() {
			var additionalStaff = $('additionalStaff');
			var totalStaffContainer = $('totalStaff');
			var limitError = $('limitError');
			
			additionalStaff.removeClassName('inputError');
			totalStaffContainer.removeClassName('inputError');
			limitError.hide();
			
			var currentStaff = parseInt($('currentStaff').innerHTML);
			var increment = additionalStaff.getValue();
			
			var total = currentStaff;
			
			if (increment == null || isNaN(parseInt(increment))  || increment < 1) {
				additionalStaff.addClassName('inputError');
			} else {
				total = currentStaff + parseInt(increment);
			} 
			
			if (staffLimit && total > staffLimit) {
				totalStaffContainer.addClassName("inputError");
				limitError.show();
			}
			
			totalStaffContainer.update(total);
		}
		
		
		
		function updatePrice() {
			var form = $('upgradeAccount').serialize(true);
			var options = new Object();
			$$(".changesPrice").each(function(element) {
					options[element.name] = form[element.name];
				}); 
			getResponse("<@s.url action="increaseStaffPriceCheck" namespace="/ajax"/>", "get", options);
		}
		
		
		
		document.observe("dom:loaded", function() {
				$('additionalStaff').observe('keyup', updateTotal);
				$$(".changesPrice").each(function(element) {
						element.observe('change', updatePrice);
					});
				$('upgradeAccount').observe('submit', function(event) { 
						var form = Event.element(event);
						$$("#" + form.id + " input[type='submit']").invoke('disable');
					});
			});
	</@n4.includeScript>
</head>

${action.setPageType('account_settings', 'increase_staff')!}

<@s.form action="increaseStaffComplete" id="upgradeAccount" cssClass="fullForm fluidSets" theme="fieldid">
	<#include "/templates/html/common/_formErrors.ftl"/>
	
	<h3 class="clean">
		<@s.text name="label.increasing_your_accounts"/>
	</h3>
	
	<p>
		<@s.text name="label.increase_staff_information"/>
	</p>
	
	<table id="incrementResource">
		<tr>
			<th><@s.text name="label.current_staff_limit"/></th>
			<td id="currentStaff">${limits.employeeUsersMax}</td>	
		</tr>
		<tr>
			<th><@s.text name="label.additional_staff"/></th>
			<td>+<@s.textfield name="additionalStaff" theme="fieldidSimple" id="additionalStaff" cssClass="changesPrice"/></td>
			<td class="messageCol">(<@s.text name="label.per_user_per_month"><@s.param>${upgradeFilter.currentCostPerUserPerMonth?string.currency}</@s.param></@s.text>)</td>	
		</tr>
		<tr>
			<th><@s.text name="label.staff_limit_after_purchase"/></td>
			<td id="totalStaff">${limits.employeeUsersMax + additionalStaff?default(1)}</td>
			<td class="messageCol errorMessage" id="limitError"  style="display:none">(<@s.text name="error.limited_user_accounts"><@s.param>${staffLimit!}</@s.param></@s.text>)</td>	
		</tr>
	</table>	
	<#assign charge_label="label.you_will_be_charged_this_immediately"/>
	<#include "../accountUpgrade/_charges.ftl"/>
		
	
	<div class="actions">
		<p id="purchaseWarning"><strong><@s.text name="label.purchase_warning"/></strong></p>
		<@s.submit key="label.please_upgrade_my_account"/>
		<@s.text name="label.or"/>
		<a href="<@s.url action="accountUpgrades"/>"><@s.text name="label.do_not_upgrade_my_account"/></a>
	</div>
</@s.form>
	