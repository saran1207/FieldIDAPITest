<head>
	
	<link type="text/css" rel="stylesheet" href="<@s.url value="/style/pageStyles/projectNotes.css"/>"/>
	<script type="text/javascript" src="<@s.url value="/javascript/fileUpload.js"/>"></script>
	<script type="text/javascript" src="<@s.url value="/javascript/projects.js"/>"></script>
	<script type="text/javascript">
		uploadUrl = '<@s.url action="uploadForm" namespace="/aHtml/iframe" />';
		removeText = '<@s.text name="label.remove"/>';
	</script>
	
</head>
${action.setPageType('job','notes')!}
<#include "_secondaryNav.ftl"/>
<@s.form id="addNote" action="jobNoteCreate" method="post" theme="fieldid" cssClass="crudForm largeForm layout" >
	<h2><@s.text name="label.add_a_note"/></h2>
	<#include "_form.ftl"/>
	
	<div class="formAction" >
		<@s.url id="cancelUrl" action="jobNotes" projectId="${project.id}" />
		<@s.reset key="label.cancel" onclick="return redirect('${cancelUrl}');"/>
		<@s.submit key="label.save" />
	</div>	
</@s.form>

<script type="text/javascript">
	addUploadFile(${limits.diskSpaceMaxed?string('true','false')}, '<span id="attachment" class="limitWarning"><@s.text name="warning.disk_space_maxed"/></span>');
</script>