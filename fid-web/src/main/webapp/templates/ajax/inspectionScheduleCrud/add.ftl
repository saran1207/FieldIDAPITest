<#assign form>
	<#include "/templates/html/inspectionScheduleCrud/_addForm.ftl" >
</#assign>
<#escape x as x?j_string >
	$('newSchedule').replace("${form}");
	
</#escape>