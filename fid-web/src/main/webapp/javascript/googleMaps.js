/**
 * requires.... 
 *  <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script> 
 *  <@n4.includeScript src="googleMaps.js"/>
 *  
 * e.g. 
 * 
 * simplest usage for a map with a single marker on it =
 *  googleMapFactory.createAndShowWithLocation('mapCanvas',44, -79)
 * ...given <div id='mapCanvas'/> exists and... 
 * 44/-79 is the latitude/longitude of the marker.   
 * 
 * to add more markers and/or customize them...
 * 
 *  var map = googleMapFactory.create('mapCanvas');
 *	map.makeMarker = googleMapFactory.makeMarkerForStatus;     // factory to create custom markers based on status  (PASS in this case).
 *  map.addLocation(44,-79, "hello show this in infowindow", "pass");   // content = "hello..." which pops up when user clicks on marker.  PASS = status
 *  map.show();
 * 
 * make sure you don't call map.show() unless you know the element ('mapCanvas' in this case) has been parsed into the document.  
 * if not, delay the show call until onLoad().
 */

var googleMapFactory = (function() { 
	
	var create = function(id) { 
		return googleMap(id);
	};
	
	var createAndShowWithLocation = function(id, latitude,longitude) { 
		var map = create(id);
		map.addLocation(latitude,longitude);
		map.show();
	};
			
	
	/**
	 * map object returned by factory 
	 */	
	function googleMap(mapId) {
		
		/* private methods and properties */
		var id = mapId;
		var map = '';
		var locations = [];		
		var options = {
				zoom: 14,
				mapTypeId: google.maps.MapTypeId.ROADMAP
			};
		var updateContentWithAddress = true; 
		
		function showInfoWindow(marker, map) {
			if (updateContentWithAddress && !marker.address) {
				new google.maps.Geocoder().geocode( {'latLng': marker.position}, function(results, status) {
					if (status== google.maps.GeocoderStatus.OK) {
						marker.address = formatAddressString(results[0]);						
						marker.content = marker.content ? marker.content+'<br>' + marker.address : marker.address; 
					}
					showInfoWindowImpl(marker,map);
				});
			}
			else if (marker.content) {
				showInfoWindowImpl(marker,map);
			}			
		}

		function showInfoWindowImpl(marker, map) {			
			var infowindow =  new google.maps.InfoWindow();					
			infowindow.setContent(marker.content);	
			infowindow.open(map,marker);
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
		
		/* public methods exposed */
		return { 		
			
			makeMarker : function(loc) {
				return new google.maps.Marker({
					draggable : false,
					position: loc,
					map: map
				});
			},
			
			addLocation : function(latitude,longitude) {
				var loc = new google.maps.LatLng(latitude,longitude);
				locations.push(loc);  				
				loc.args =  [].slice.call(arguments, 2); /* attach any additional args possibly passed to location. may be used by makeMarker() */
				return loc;
			},
			
			show : function() {
				if (map) {
					return;
				}
				var element = document.getElementById(id);
				if (!element) { 
					throw "can't find element " + id + " for google map.";
				}
				map = new google.maps.Map(element, options);
				
				var bounds = new google.maps.LatLngBounds();				
				var count = locations.length;

				for (var i=0; i<count; i++) {
					var loc = locations[i];
					var marker = this.makeMarker(loc);
					marker.setMap(map);
					if (marker.content || updateContentWithAddress) { 
						google.maps.event.addListener(marker, 'click', function() {
							showInfoWindow(this, map);
						});						
					}
					bounds.extend(loc);							
				}
				if (count>1) { 
					map.fitBounds(bounds);
				} else if (count==1) { 
					map.setCenter(locations[0]);
				}			
			}		
			
		};
		
	};
	
	function prefixed(prefix,url) {
		return prefix ? prefix+url : url;
	}
			
	var makeMarkerForStatus = function(loc) {
		var content = loc.args[0];   // assumes that these *might* be passed to addLocation in this order.  [content,status,prefix]
		var status = loc.args[1];
		var prefix = loc.args[2];
		if (status.toLowerCase()=='fail') {
			return new google.maps.Marker({     /* note that red is default icon which is what we currently use for fail */
				draggable : false,
				position: loc
			});
		}
		
		var icon = '';
		if (status.toLowerCase()=='pass') {
			icon = prefixed(prefix,'images/marker-images/greenMapIcon.png');
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
		
		var marker =  new google.maps.Marker({
			  draggable: false,
			  icon: image,
			  shadow: shadow,
			  shape: shape,
			  position: loc
			});
		
		marker.content = content;
		return marker;		
	};

		
	return { 
		create : create,
		createAndShowWithLocation : createAndShowWithLocation,
		makeMarkerForStatus : makeMarkerForStatus	
	};

	
})();



		
