<head>
	<#include "/templates/html/common/_datetimepicker.ftl"/>
	<@n4.includeStyle href="projects" type="page"/>
	<#include "/templates/html/common/_orgPicker.ftl"/>
	
</head>
<#include "/templates/html/common/_formErrors.ftl" />

<div class="twoColumn" >
	<div class="infoSet">
		<label for="projectID"><@s.text name="indicator.required"/> <@s.text name="label.projectid"/></label>
		<@s.textfield name="projectID" required="true"/>
	</div>
	
	<div class="infoSet">
		<label for="name"><@s.text name="indicator.required"/> <@s.text name="label.title"/></label>
		<@s.textfield name="name" required="true"/>
	</div>
	
	<div class="infoSet">
		<label for="name"><@s.text name="indicator.required"/> <@s.text name="label.owner"/></label>
		<@n4.orgPicker name="owner" required="true"/>
	</div>
		
	
	<div class="infoSet">
		<label for="status"><@s.text name="label.status"/></label>
		<@s.textfield name="status"/>
	</div>
	
	<div class="infoSet">
		<label for="open"><@s.text name="label.open" /></label>
		<@s.checkbox name="open" />
	</div>		

	<div class="infoSet">
		<label for="description"><@s.text name="label.description" /></label>
		<@s.textarea name="description" />
	</div>
	
	
</div>
<div class="twoColumn" >
	<div class="infoSet">
		<label for="started"><@s.text name="label.datecreated"/></label>
		<span>${action.formatDateTime(project.created)}</span>
	</div>
	<div class="infoSet">
		<label for="started"><@s.text name="label.datestarted"/></label>
		<@s.textfield name="started" cssClass="datetimepicker"/>
	</div>
	
	<div class="infoSet">
		<label for="estimatedCompletion"><@s.text name="label.estimatedcompletion"/> </label>
		<@s.textfield name="estimatedCompletion" cssClass="datetimepicker"/>
	</div>
	
	<div class="infoSet">
		<label for="acutalCompletion"><@s.text name="label.actualcompletion"/> </label>
		<@s.textfield name="actualCompletion" cssClass="datetimepicker"/>
	</div>
		
	<div class="infoSet">
		<label for="duration"><@s.text name="label.duration"/></label>
		<@s.textfield name="duration"/>
	</div>

	<div class="infoSet">
		<label for="poNumber"><@s.text name="label.ponumber"/></label>
		<@s.textfield name="poNumber" />
	</div>

	<div class="infoSet">
		<label for="workPerformed"><@s.text name="label.workperformed" /></label>
		<@s.textarea name="workPerformed" />
	</div>	
</div>