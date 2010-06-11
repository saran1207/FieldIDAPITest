<head>
	<style type="text/css">
		
		#currentPlan {
			margin-right:10px;
		}
	</style>
	
</head>

${action.setPageType('account_settings', 'list')!}

<div class="pageSection crudForm largeForm">
	<h2><@s.text name="label.about_your_system"/></h2>
	<div class="sectionContent">
		<div class="infoSet">
			<label  for="companyID"><@s.text name="label.companyid"/></label>
			<span class="fieldHolder">${sessionUser.tenant.name?html}</span>			
		</div>
		
		<div class="infoSet">
			<label><@s.text name="label.your_current_plan"/></label>
			<span class="fieldHolder">
				<strong id="currentPlan">${(action.currentPackageFilter().packageName?html)!}</strong>   
				<#if userSecurityGuard.allowedAccessWebStore && action.currentPackageFilter().upgradable>
					 <a href="<@s.url action="upgradePlans"/>"><@s.text name="label.upgrade_my_plan"/></a>
				</#if>
			</span>
		</div>
		<div class="infoSet">
			<label><@s.text name="label.disk_space"/></label>
			<div class="fieldHolder" style="float:left; padding: 5px 0;">
				
				<div style="width:300px; float:left;">
					<@n4.percentbar progress="${limits.diskSpaceUsed}" total="${limits.diskSpaceMax}"/>
				</div>
				<div style="float:left; margin:5px;">${action.getHumanReadableFileSize(limits.diskSpaceUsed)} <@s.text name="label.of"/> <#if limits.diskSpaceUnlimited><@s.text name="label.unlimited"/><#else>${action.getHumanReadableFileSize(limits.diskSpaceMax)}</#if></div>
			</div>
		</div>
		<div class="infoSet">
			<label><@s.text name="label.employee_accounts"/></label>
			<div class="fieldHolder" style="float:left; padding: 5px 0;">
				
				<div style="width:300px; float:left;">
					<@n4.percentbar progress="${limits.employeeUsersUsed}" total="${limits.employeeUsersMax}"/>
				</div>
				<div style="float:left; margin:5px;">${limits.employeeUsersUsed} <@s.text name="label.of"/> <#if limits.employeeUsersUnlimited><@s.text name="label.unlimited"/><#else>${limits.employeeUsersMax}</#if></div>
				<#if userSecurityGuard.allowedAccessWebStore && !action.currentPackageFilter().legacy && !limits.employeeUsersUnlimited>
					<div style="float:left; margin:5px;"><a href="<@s.url action="increaseEmployeeLimit"/>"><@s.text name="label.i_want_more_employee_accounts"/></a></div>
				</#if>
				
			</div>
		</div>
		<div class="infoSet">
			<label><@s.text name="label.assets"/></label>
			<div class="fieldHolder" style="float:left; padding: 5px 0;">
				
				<div style="width:300px; float:left;">
					<@n4.percentbar progress="${limits.assetsUsed}" total="${limits.assetsMax}"/>
				</div>
				<div style="float:left; margin:5px;">${limits.assetsUsed} <@s.text name="label.of"/> <#if limits.assetsUnlimited><@s.text name="label.unlimited"/><#else>${limits.assetsMax}</#if></div>
			</div>
		</div>
	</div>
</div>

<div class="pageSection crudForm largeForm">
	<h2><@s.text name="label.login_options"/></h2>
	<div class="sectionContent">
		<div class="infoSet">
			<label><@s.text name="label.login_url"/></label>
			<span class="fieldHolder">${loginUrl?html}</span>
		</div>
		
		<div class="infoSet">
			<label>
				<@s.text name="label.embedded_login_snipit"/>
				<a href="javascript:void(0);" id="whatsThis_reportTitle_button" >?</a>
				<div id="whatsThis_embededLoginCode" class="hidden" style="border :1px solid black"><@s.text name="whatsthis.embedded_login_code"/></div>
				<script type="text/javascript">
					$("whatsThis_reportTitle_button").observe( 'click', function(event) { showQuickView('whatsThis_embededLoginCode', event); } );
				</script>
			</label>
			<span class="fieldHolder">
				<#assign snipit><iframe src="${embeddedLoginUrl}" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="300" ></iframe></#assign>
				${snipit?html}
			</span>
		</div>
	</div>
</div>
	<@s.form action="systemSettingsUpdate" cssClass="crudForm pageSection largeForm" theme="fieldid">
		<#include "../common/_formErrors.ftl"/>
		<h2><@s.text name="label.system_settings" /></h2>
	
			<div class="infoSet">
				<label class="label">
					<@s.text name="label.enable_asset_assignment"/>
					<a href="javascript:void(0);" id="whatsThis_assignedTo_button" >?</a>
					<div id="whatsThis_embeddedCode" class="hidden" style="border :1px solid black">
						<h4><@s.text name="label.asset_assignment"/></h4>
						<p>
							<@s.text name="label.asset_assignment_tooltip_1"/>
							<br/>
							<@s.text name="label.asset_assignment_tooltip_2"/>
							<b><@s.text name="label.asset_assignment_tooltip_3"/></b> 
							<@s.text name="label.asset_assignment_tooltip_4"/>
						</p>
					</div>
					<script type="text/javascript">
						$("whatsThis_assignedTo_button").observe( 'click', function(event) { showQuickView('whatsThis_embeddedCode', event); } );
					</script>
				</label>
				<span class="fieldHolder">
					<@s.checkbox name="assignedTo" /> 
				</span>
			</div>
		
		<#include "_settings.ftl"/>
		<div class="formAction">
			<@s.submit key="label.save"/> <@s.text name="label.or"/> <a href="<@s.url action="setup"/>"><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
