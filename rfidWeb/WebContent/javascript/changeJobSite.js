var jobSiteChangeUrl = '';
function jobSiteChange( jobSite ) {
	var url = jobSiteChangeUrl + "?uniqueID=" + jobSite.value;
	getResponse( url, "get" );
}