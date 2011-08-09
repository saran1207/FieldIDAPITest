
var googleMap = (function() {
	 
	var name = "FieldIdGoogleMaps";	
	var map = '';  
	var bounds = new google.maps.LatLngBounds();
	var markerCount = 0;
	
	var initialize = function(id) {
		var myOptions = {
	 		zoom: 14,
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
		bounds.extend(loc);
		if (markerCount!=0) { 
			map.fitBounds(bounds);   /*TODO DD : optimize??? leave this up to caller to do just once? */
		}			
		markerCount++;		
		
		
		var geocoder = new google.maps.Geocoder();
		geocoder.geocode( {'latLng': loc}, function(results, status) {
			if (status== google.maps.GeocoderStatus.OK) {				        
				marker.setTitle(formatAddressString(results[0]));				      
		 	}
		});					
	}

	
	function formatAddressString(address) {
		var result = '';
		if (address.address_components[0]) {
			result += address.address_components[0].short_name;
		}
		if (address.address_components[1]) {
			result += ',' + address.address_components[1].short_name;
		}
		if (address.address_components[2]) {
			result += ',' + address.address_components[2].short_name;
		}
		return result;   
	}
	
	return {
		initialize: initialize, 
		initializeWithMarker : initializeWithMarker,
		addMarker: addMarker
	};
 
})();



		
