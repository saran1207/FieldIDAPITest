<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %> 
<%@ taglib prefix="s" uri="/struts-tags" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Field ID Administration</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="styles/style.css" />
<script src="javascripts/prototype.js" type="text/javascript"></script>
<script src="javascripts/scriptaculous.js" type="text/javascript"></script>
</head>
<body >
<div id="header">
<h1>Administration Console</h1>
</div>
<ul id="nav">
<li><a href="organizationList.action">Organizations</a></li>
<li><a href="instructionalVideos.action">Instructional Videos</a></li>
<li><a href="findProductOptionList.action">Find Product Option</a></li>
<li><a href="unitOfMeasureList.action">Unit of Measures</a></li>
<li><a href="orderMappingList.action">Order Mappings</a></li>
<li><a href="importerList.action">Importer</a></li>
<li><a href="mailTest.action">Mail</a></li>
<li><a href="configCrud.action">Configuration</a></li>
<li><a href="certSelection.action">Certs</a></li>
<li><a href="taskCrud.action">Tasks</a></li>
<li><a href="changeAdminPass.action">Change System Pass</a></li>
</ul>
<div id="container">
<div id="content">
<decorator:body />
</div>
<div id="footer">
<p>Copyright&copy; 2008 N4 Systems <br />
|&nbsp;XHTML template by <a href="http://www.karenblundell.com" target="blank">arwen54</a>&nbsp;|&nbsp;<a href="http://validator.w3.org/check?uri=referer" target="_blank">Valid XHTML</a>&nbsp;|&nbsp;<a href="http://jigsaw.w3.org/css-validator/" target="_blank">Valid CSS</a>&nbsp;|</p>
</div>
</div>
</body>
</html>