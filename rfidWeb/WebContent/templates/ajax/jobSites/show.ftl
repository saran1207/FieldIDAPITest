<#escape x as x?js_string >
	$('customerName').innerHTML = '${(jobSite.customer.name)!}';
	$('divisionName').innerHTML = '${(jobSite.division.name)!}';

	if( $('customer') != null ) { 
		$('customer').value = '${(jobSite.customer.id)!}';
		$('division').value = '${(jobSite.division.id)!}';
	}
	if( typeof( updateInspectionBooks ) != "undefined" ) {
		updateInspectionBooks( 'customer', ( $('uniqueID').value != "" ) ? true : false );
	}
	
</#escape>
