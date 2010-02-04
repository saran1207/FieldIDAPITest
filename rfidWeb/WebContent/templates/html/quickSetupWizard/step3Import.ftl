<title><@s.text name="label.quick_setup_wizard"/> - <@s.text name="label.step_x_of_y"><@s.param>3</@s.param><@s.param>3</@s.param></@s.text> - <@s.text name="label.importing"/></title>


<#include "../publishedCatalog/_show.ftl"/>

<head>
	<@n4.includeScript>
		var yourDoneUrl = '<@s.url action="step3" idImported="${uniqueID}"/>';
		var yourCancelUrl = '<@s.url action="step3" />';
		var importDoneName = '<@s.text name="label.next"/>';
	</@n4.includeScript>
	<@n4.includeStyle type="page" href="quick_setup_wizard"/>
	<style>
		#steps {
			width:680px;
			float:right;
			margin:20px 0px;
			padding-left: 70px;
		}
	</style>
</head>