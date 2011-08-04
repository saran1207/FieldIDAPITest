
var googleMap = (function() {
	 
	var name = "FieldIdGoogleMaps";
	
 
	var initialize = function(id, latitude, longitude) {
		var loc = new google.maps.LatLng(latitude, longitude);	
		var myOptions = {
	 		zoom: 14,
	  	  	mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		var map = new google.maps.Map(document.getElementById(id), myOptions);  
		map.setCenter(loc);
		var coordinates = '(' + latitude + ' , ' + longitude + ')';  /*default value just in case geocoder doesnt work */
					
		var marker = new google.maps.Marker({
			position: loc,
			map: map,
			title: coordinates
		});	
		
		var geocoder = new google.maps.Geocoder();
		geocoder.geocode( {'latLng': loc}, function(results, status) {
			if (status== google.maps.GeocoderStatus.OK) {				        
				marker.setTitle(formatAddressString(results[0]));				      
		 	}
		});
				
		return map;		
	};

	function formatAddressString(address) { 
		return address.address_components[0].short_name + ' '  + address.address_components[1].short_name + ', ' + address.address_components[2].short_name;   
	}
	
	return {
		initialize: initialize
	};
 
})();



		
