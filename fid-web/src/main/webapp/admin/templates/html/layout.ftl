<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Field ID Administration</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	<@n4.includeScript src="prototype"/>
	<@n4.includeScript src="scriptaculous"/>
    <@n4.includeScript src="common"/>
	<@n4.includeStyle href="admin/style"/>	
	${head!}
</head>
<body>
	<div id="header">
		 <img src="../images/admin/admin-logo.png"/>
	</div>
	
	<div id="nav">
		<label for="navSelect">Action: </label>
		<select id="navSelect" onchange="window.location = this.options[this.selectedIndex].value;">
			<option value="organizations.action">Organizations</option>
			<option value="eulas.action">EULA</option>
			<option value="releaseNotes.action">Release Notes</option>
			<option value="instructionalVideos.action">Instructional Videos</option>
			<option value="unitOfMeasureList.action">Unit of Measures</option>
			<option value="orderMappingList.action">Order Mappings</option>
			<option value="importObservations.action">Observation Importer</option>
			<option value="mailTest.action">Mail</option>
			<option value="configCrud.action">Configuration</option>
			<option value="certSelection.action">Certs</option>
			<option value="taskCrud.action">Tasks</option>
			<option value="changeAdminPass.action">Change System Pass</option>
			<option value="promoCodes.action">Promo Codes</option>
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