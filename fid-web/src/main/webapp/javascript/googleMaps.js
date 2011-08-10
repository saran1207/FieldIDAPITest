
var googleMap = (function() {
	 
	var name = "FieldIdGoogleMaps";	
	var map = '';  
	var locations = [];
	var infowindow =  new google.maps.InfoWindow();	
	
	var initialize = function(id) {
		var myOptions = {
	 		zoom: 15,
	  	  	mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		map = new google.maps.Map(document.getElementById(id), myOptions);		
		addMarkers();
		return map;		
	};
	
	var initializeWithMarker = function(id, latitude, longitude, content) { 
		addMarker(latitude,longitude, content);		
		initialize(id);
	};
	
	var addMarker = function(latitude,longitude,content) {
		var location = new google.maps.LatLng(latitude, longitude);
		location.content = content;
		locations.push(location);
	};
	
	function addMarkers() {
		var bounds = new google.maps.LatLngBounds();				
		var count = locations.length;
		for (var i=0; i<count; i++) {
			var loc = locations[i];
			var marker = new google.maps.Marker({
				position: loc,
				map: map,
				title: loc.toString()
			});		
			
			bounds.extend(loc);					
			marker.content = loc.content;
			google.maps.event.addListener(marker, 'click', function() {
				infowindow.setContent(this.content);
				infowindow.open(map,this);
			});		
		
		}
		if (count>1) { 
			map.fitBounds(bounds);
		} else if (count==1) { 
			map.setCenter(locations[0]);
		}			
		
	};
	
	function infoWindow(marker, content) { 
		
	}
	
	var addGeoCoder = function(marker) { 
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
			result += ' ' + address.address_components[1].short_name;
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



		
