<head>
	<style>
		#eventJobSelect label, #assetJobSelect label {
			float:none;
			display:inline;
			padding:0 0 0 10px;
			width:auto;
		}
		#eventJobSelect, #assetJobSelect {
			padding: 5px 0;
		}
	</style>
</head>
${action.setPageType('job', 'add')!}
<div class="pageSection" >
	<h2 class="decoratedHeader"><@s.text name="label.projectdetails"/></h2>

	<@s.form action="jobCreate" theme="fieldid" cssClass="crudForm largeForm bigForm pageSection layout">
		<div class="infoSet">
			<label for="eventJob"><@s.text name="indicator.required"/> What type of job is this?</label>
			<div class="container">
				<div id="eventJobSelect"><@s.radio name="eventJob" list="%{getOn(getText('label.eventjob'))}" /><label class="description"> - <@s.text name="description.eventjob"/></label></div>
				<div id="assetJobSelect"><@s.radio name="eventJob" list="%{getOff(getText('label.assetjob'))}"/><label class="description"> - <@s.text name="description.assetjob"/></label></div>
			</div>
		</div>
		<#include "_form.ftl"/>
	
		<div class="formAction">
			<@s.submit key="label.save"/>
			<@s.text name="label.or"/>
			<@s.url id="cancelUrl" action="jobs"/>
			<a href="#" onclick="return redirect( '${cancelUrl}' );" ><@s.text name="label.cancel"/></a>
		</div>
	</@s.form>
</div>