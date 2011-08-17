/**
 * TODO : 
 * 
 * in order to make this code more portable i should....
 * prototype the map class and give it an "addMarker()" method. 
 * .: API would look like this...
 * 
 *  var map = googleMap.initialize(id); 
 *  map.addMarker(latitude, longitude);    etc...
 *  onload = map.show();
 *  
 *  OR 
 *  
 *  var map = googleMap.showWithMarker(id, latitude, longitude);   <-- default map for a single point.
 *  
 *  as well, each map instance would have its own "locations[]" property and "createMarker()" method. 
 *  the mapOptions property should be exposed
 * 
 * the googleMap interface should only expose...
 * googleMap.initialize() methods  (prolly be a few).
 * googleMap.enableGeoCodingForMarkers()
 * googleMap.getMarkerForStatus();   // fieldId specific code
 * 
 * 
 * the big problem with the code now is that it is sort of a singleton.  displaying multiple maps on same page would break.  
 * 
 */

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
	
	var initializeWithMarker = function(id, latitude, longitude, content, marker) { 
		addMarker(latitude,longitude,content,marker);		
		return initialize(id);
	};
	
	var addMarker = function(latitude,longitude,content,marker) {
		var location = new google.maps.LatLng(latitude,longitude);
		location.content = content;
		location.marker = marker;
		locations.push(location);
		
		/** TODO DD : why not just create marker here and deal with setting of map/image/shadow stuff later? */
	};
	
	function addMarkers() {
		var bounds = new google.maps.LatLngBounds();				
		var count = locations.length;
		for (var i=0; i<count; i++) {
			var loc = locations[i];
			var marker = getMarkerForLocation(loc);
			
			bounds.extend(loc);					
			marker.content = loc.content;
			marker.loc = loc;
			/** TODO DD : refactor this update address feature so it is an option.  note that it can affect performance */
			google.maps.event.addListener(marker, 'click', function() {
				updateInfoWindowWithAddress(this, map);
			});		
		
		}
		if (count>1) { 
			map.fitBounds(bounds);
		} else if (count==1) { 
			map.setCenter(locations[0]);
		}			
		
	};
	
	function updateInfoWindowWithAddress(marker, map) {
		if (marker.address) {
			infowindow.setContent(marker.content);	
			infowindow.open(map,marker);	
			return;
		}
		
		var geocoder = new google.maps.Geocoder();
		geocoder.geocode( {'latLng': marker.loc}, function(results, status) {
			if (status== google.maps.GeocoderStatus.OK) {
				marker.address = formatAddressString(results[0]);
				marker.content = marker.content+'<br>(' + marker.address+')'; 
			}
			infowindow.setContent(marker.content);
			infowindow.open(map,marker);
		});		
	} 
	
	function prefixed(prefix, url) { 
		return prefix ? prefix+url : url;
	}
	
	function getMarkerForLocation(loc) { 
		if(loc.marker) {
			loc.marker.setPosition(loc);
			loc.marker.setMap(map);
			return loc.marker;
		} else {
			return new google.maps.Marker({
				position: loc,
				map: map				
			});					
		}
	} 	
	
	/** TODO DD : refactor this.  events status stuff doesn't really belong with google maps stuff */
	var markerForStatus = function(status, prefix) { 
		if (!status || status.toLowerCase()=='fail') {
			return null;  /* note that red is default icon which is what we currently use for fail */
		}
		
		var icon = '';
		if (status.toLowerCase()=='pass') {
			icon = prefixed(prefix, 'images/marker-images/greenMapIcon.png');
		} else if (status.toLowerCase()=='na') {
			icon =  prefixed(prefix,'images/marker-images/grayMapIcon.png');
		}
		var image = new google.maps.MarkerImage(
				icon,
				new google.maps.Size(32,32),
				new google.maps.Point(0,0),
				new google.maps.Point(16,32)
		);		
		
		var shadow = new google.maps.MarkerImage(
				  prefixed(prefix,'images/marker-images/mapIconShadow.png'),
				  new google.maps.Size(52,32),
				  new google.maps.Point(0,0),
				  new google.maps.Point(16,32)
				);
		
		var shape = {
				  coord: [19,0,20,1,21,2,22,3,23,4,24,5,24,6,24,7,24,8,24,9,24,10,24,11,24,12,23,13,23,14,22,15,21,16,20,17,20,18,19,19,19,20,18,21,18,22,17,23,17,24,17,25,17,26,16,27,16,28,16,29,16,30,16,31,14,31,14,30,14,29,14,28,14,27,14,26,13,25,13,24,13,23,12,22,12,21,12,20,11,19,10,18,10,17,9,16,8,15,7,14,7,13,6,12,6,11,6,10,6,9,6,8,6,7,6,6,7,5,7,4,8,3,9,2,10,1,11,0,19,0],
				  type: 'poly'
				};
		
		return new google.maps.Marker({
			  draggable: false,
			  icon: image,
			  shadow: shadow,
			  shape: shape
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
		addMarker: addMarker, 
		markerForStatus: markerForStatus
	};
 
})();



		
