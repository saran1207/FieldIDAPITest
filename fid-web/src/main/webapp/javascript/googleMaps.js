
var googleMap = (function() {
	 
	var name = "FieldIdGoogleMaps";	
	var map = '';
	
	var initialize = function(id) {
		var myOptions = {
	 		zoom: 3,
	  	  	mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		map = new google.maps.Map(document.getElementById(id), myOptions);
		var loc = new google.maps.LatLng(42.130821,-97.998047);  /*default value is centered on north america */		
		map.setCenter(loc);		
		return map;		
	};
	
	var initializeWithMarker = function(id, latitude, longitude) { 
		initialize(id);
		addMarker(latitude,longitude);		
	}
	
	var addMarker = function(latitude,longitude) {	
		var loc = new google.maps.LatLng(latitude, longitude);	
		map.setCenter(loc);
		var coordinates = '(' + latitude + ' , ' + longitude + ')';  /*default value just in case geocoder doesnt work */
					
		var marker = new google.maps.Marker({
			position: loc,
			map: map,
			title: coordinates
		});
		map.setZoom(14);
		
		var geocoder = new google.maps.Geocoder();
		geocoder.geocode( {'latLng': loc}, function(results, status) {
			if (status== google.maps.GeocoderStatus.OK) {				        
				marker.setTitle(formatAddressString(results[0]));				      
		 	}
		});					
	}

	function formatAddressString(address) { 
		return address.address_components[0].short_name + ' '  + address.address_components[1].short_name + ', ' + address.address_components[2].short_name;   
	}
	
	return {
		initialize: initialize, 
		initializeWithMarker : initializeWithMarker,
		addMarker: addMarker
	};
 
})();



		
