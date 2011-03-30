var editDownloadNameUrl="";
var cancelDownloadNameUrl="";

function editDownloadName( fileId ) {
	var params = new Object();
	params.fileId = fileId;
	getResponse(editDownloadNameUrl, "get", params);
}

function updateDownloadName( fileId ) {
	$( 'download_name_' + fileId ).request(getStandardCallbacks());
}

function cancelDownloadName( fileId ) {
	var params = new Object();
	params.fileId = fileId;
	getResponse(cancelDownloadNameUrl, "get", params);
}

function markDownloaded ( row ) {
	$(row).removeClassName('strong');
}