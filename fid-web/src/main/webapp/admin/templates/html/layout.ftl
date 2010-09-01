<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Field ID Administration</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	<@n4.includeScript src="prototype"/>
	<@n4.includeScript src="scriptaculous"/>
	<@n4.includeStyle href="admin/style"/>	
	${head!}
</head>
<body>
	<div id="header">
		<h1>Field ID Admin Console</h1>
	</div>
	
	<div id="nav">
		<label for="navSelect">Action: </label>
		<select id="navSelect" onchange="window.location = this.options[this.selectedIndex].value;">
			<option value="admin/organizations.action">Organizations</option>
			<option value="admin/eulas.action">EULA</option>
			<option value="admin/releaseNotes.action">Release Notes</option>
			<option value="admin/instructionalVideos.action">Instructional Videos</option>
			<option value="admin/findProductOptionList.action">Find Product Option</option>
			<option value="admin/unitOfMeasureList.action">Unit of Measures</option>
			<option value="admin/orderMappingList.action">Order Mappings</option>
			<option value="admin/importObservations.action">Observation Importer</option>
			<option value="admin/mailTest.action">Mail</option>
			<option value="admin/configCrud.action">Configuration</option>
			<option value="admin/certSelection.action">Certs</option>
			<option value="admin/taskCrud.action">Tasks</option>
			<option value="admin/changeAdminPass.action">Change System Pass</option>
			<option value="admin/promoCodes.action">Promo Codes</option>
			<option value="admin/cacheShow.action">Cache Control</option>
		</select>
	</div>
	
	<script type="text/javascript">
		var currentUrl = window.location.href;
		var navSelect = $('navSelect');
		for (var i = 0; i < navSelect.length; i++) {
			if (currentUrl.indexOf(navSelect.options[i].value) != -1) {
				navSelect.selectedIndex = i;
				break;
			}	
		}
	</script>
	
	<div id="container">
		<div id="content">
			${body}
		</div>
	</div>
	
</body>
</html>