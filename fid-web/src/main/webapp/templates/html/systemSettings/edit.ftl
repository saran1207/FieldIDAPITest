<head>
	<style type="text/css">
		
		#currentPlan {
			margin-right:10px;
		}
	</style>
	
</head>

${action.setPageType('account_settings', 'list')!}

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
		<div class="sectionContent">
			<div class="infoSet">
				<label>
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
		
			<div class="infoSet">
				<label for="dateFormat">
					<@s.text name="label.preferred_date_format" />
				</label>
				<@s.select name="dateFormat" list="dateFormats" listKey="id" listValue="name"/>
			</div>
		<div class="formAction">
			<@s.submit key="label.save"/> <@s.text name="label.or"/> <a href="<@s.url action="setup"/>"><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
</div>