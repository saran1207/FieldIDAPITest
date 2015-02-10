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
 * note that you MUST style the div with the desired height/width.  a map will not affect its containers size - it merely fills it up.
 * 
 * to add more markers and/or customize them...
 * 
 *  var map = googleMapFactory.create('mapCanvas');
 *	map.makeMarker = googleMapFactory.makeMarkerForStatus;     // factory to create custom markers based on status  (PASS status in this case).
 *  map.addLocation(44,-79, "hello show this in infowindow", "pass");   // content = "hello..." which pops up when user clicks on marker.  PASS = status
 *  map.show();
 * 
 * make sure you don't call map.show() unless you know the element ('mapCanvas' in this case) has been parsed into the document.  
 * if not, delay the show call until onLoad().
 */

var googleMapFactory = (function() {

	 var defaultOptions = {
		 zoom: 8,
		 mapTypeId:google.maps.MapTypeId.ROADMAP,
	 };

	 var create = function(options, lat, lng) {
		 // for legacy callers, needs to be fixed in next release
		 if (typeof options === "string") {
			options = {
				id: options,
				latitude: lat,
				longitude: lng
			}
		 }
		
		 if (!options.id) {
		 	throw "you must specify id for GoogleMap element";
		 }
		 if (options.latitude && options.longitude) {
			 options.center = new google.maps.LatLng(options.latitude,options.longitude);
         }
		
		 if (typeof options.zoom === "undefined") {
			options.zoom = defaultOptions.zoom;
		 }
		 if (typeof options.mapTypeId === "undefined") {
			options.mapTypeId = defaultOptions.mapTypeId;
		  }
		 
		 
		 return googleMap(options.id, options);
	 };

	 var createAndShow = function(opts) {
		var map = create(opts);
		if (opts.data) {
			var results = opts.data.results;
        	for (var i=0;i<results.length;i++) {
			    map.addLocation(results[i].latitude, results[i].longitude, results[i].desc, results[i].colour);
	        }
		}
		map.show();
		return map;
	};

	 var createAutoCompleteAddress = function(options) {
		 var widget = autoCompleteAddress(options);
		 widget.updateContentWithAddress = false;
		 return widget;
	 }


	 /**
	  * autocomplete text field with built in map returned by factory.
	  */
	 function autoCompleteAddress(opts) {
		 var defaults = {
			lat : 44,
			lng : -77
		 };
		 var options = $.extend(defaults, opts);
		 var $address = $(options.id);
		 var $text = $address.find('.txt');
		 var $lat = $address.children('.lat');
		 var $lng = $address.children('.lng');
		 var $street_address = $address.children('.street-address');
		 var $city = $address.children('.city');
		 var $country = $address.children('.country');
		 var $postalCode = $address.children('.postal-code');
		 var $state = $address.children('.state');
		 var $map;
		 var currentInput;

		 if (!options.noMap) {
			 $map = (options.mapVar) ? window[options.mapVar] : createAndShowWithLocation($address.children('.map').attr('id'), options.lat, options.lng);
		 }

		 function getAddresses(request,response) {
			 new google.maps.Geocoder().geocode( {'address': request.term}, function(results, status) {
				 if (status== google.maps.GeocoderStatus.OK) {
					 if (results.length==0) {
						 response($.map(results,function(value,i) {return value.formatted_address;}));
					 } else if (results.length>1) {
						 //alert('multiple address found - just taking the first one for now.');   // TODO : create a menu with list of all possibilities.   "did you mean....A/B/C...?"
						 response(results);
					 }
					 else {
						 response(results);
					 }
				 } else {
					 response([]);
				 }
			 });

		 };

		 function extractFromGeoCode(components, type) {
			 for (var i=0; i<components.length; i++) {
				 for (var j=0; j<components[i].types.length; j++) {
					 if (components[i].types[j] == type) return components[i].long_name;
				 }
			 }
			 return "";
		 }

		 function updateNonAddress(textInput) {
		 	update(textInput);
		 }

		 function updateAddress(item) {
		 	update(item.formatted_address, item.geometry.location, item.address_components);
		 }

		 function update(formattedAddress, latLng, addressInfo) {
			$text.val(formattedAddress);
			if (latLng) {
				$lat.val(latLng.lat());
				$lng.val(latLng.lng());
				if ($map) {
					$map.setLocation(latLng.lat(), latLng.lng(), formattedAddress);
				}
			}

			if (addressInfo) {
				$street_address.val(extractFromGeoCode(addressInfo,'street_number') + ' ' + extractFromGeoCode(addressInfo,'route'));
				$city.val(extractFromGeoCode(addressInfo,'locality'));
				$country.val(extractFromGeoCode(addressInfo,'country'));
				$postalCode.val(extractFromGeoCode(addressInfo,'postal_code'));
				$state.val(extractFromGeoCode(addressInfo,'administrative_area_level_1'));
			} else {
				$street_address.val(null);
				$city.val(null);
				$country.val(null);
				$postalCode.val(null);
				$state.val(null);
			}

			currentInput = $text.val();
		}

		 var textOptions = {
			 delay:500,
			 minLength:1,
			 source: function(request,response) { getAddresses(request,response); },
			 select: function(event,ui) {
				 updateAddress(ui.item);
				 $country.change();
				 return false;
			 },
			 change : function(a,b) {
				 		// address is NOT a selected value, just some random input so we'll remove any gps, postal code, country etc... values.
				 		if (currentInput!=$text.val()) {
						 	updateNonAddress($text.val());
							 $country.change();
						 }
				 		return false;
					 }
		 };

		 if ($text.size()>0) {
			 $text.autocomplete(textOptions).data('autocomplete')._renderItem =
				 function(ul,item) {
					 return $("<li></li>")
						 .data("item.autocomplete", item)
						 .append("<a>" + item.formatted_address + "</a>")
						 .appendTo(ul);
				 };
	 	}


		 return {

		 };

	 }


	/**
	 * map object returned by factory 
	 */	
	function googleMap(mapId, opt) {
		
		/* private methods and properties */
		var id = mapId;
		var map = '';
		var locations = [];
		var markers = [];
		var options = opt;
		var infowindow;
		var currBounds;
		var currZoom;
		var timeout;
		
		function showInfoWindow(marker, map) {
			if (this.updateContentWithAddress && !marker.address) {
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
			if (infowindow) infowindow.close();
			infowindow =  new google.maps.InfoWindow();
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

		function mapChanged() {
			google.maps.event.addListenerOnce(map,'idle',mapChanged);
			if (!currBounds ||!currZoom) {
				return;
			}
			var bounds = asBounds(map.getBounds());
			var zoom = map.getZoom();
			var centre = map.getCenter();
			if (needsRefresh(bounds,zoom)) {
				currBounds = bounds;
				currZoom = zoom;
				var url =  options.callbackUrl + "&s="+bounds.s+"&w="+bounds.w+"&n="+bounds.n+"&e="+bounds.e+"&zoom="+zoom+"&lat="+centre.lat()+"&lng="+centre.lng();
				if (timeout) {
					window.clearTimeout(timeout);
				}
				timeout = window.setTimeout(function() {
												wicketAjaxGet(url, function() {}, function() {});
											}, 1000);
			}
		}

		function needsRefresh(bounds,zoom) {
			var changed = bounds.s<currBounds.s || bounds.w<currBounds.w || bounds.e>currBounds.e || bounds.n>currBounds.n;
			if (zoom>currZoom && !changed) {
				return options.data.grouped;   // if you zoom in then refresh results when current results are grouped.
			}
			return changed;
		}

		function asBounds(bounds) {
			return {
				w: bounds.getSouthWest().lng(),
				s: bounds.getSouthWest().lat(),
				e: bounds.getNorthEast().lng(),
				n: bounds.getNorthEast().lat()
				}
		}

		/* public methods exposed */
		function removeMarkers() {
			for (var i = 0; i < markers.length; i++) {
				markers[i].setMap(null);
			}
			markers = [];
		}

		return {
			
			makeMarker : function(loc) {
				var desc = loc.desc;
				var marker = new google.maps.Marker({
					draggable : false,
					position: loc,
					map: map
				});
				marker.content = desc;
				return marker;
			},

			addLocation : function(latitude, longitude, desc, colour) {
				var loc = new google.maps.LatLng(latitude,longitude);
				if (desc) { loc.desc = desc; }
				locations.push(loc);  				
				loc.args =  [].slice.call(arguments, 2); /* attach any additional args possibly passed to location. may be used by makeMarker() */
				if (infowindow) infowindow.close();
				return loc;
			},

			setLocation : function(latitude, longitude, title, address) {
				locations = [];
				var loc = new google.maps.LatLng(latitude,longitude);
				locations.push(loc);
				loc.args = [].slice.call(arguments, 2);
				removeMarkers();
				var marker = this.makeMarker(loc);
				marker.setMap(map);
				map.setCenter(loc);
				markers.push(marker);
				marker.setPosition(loc);
				marker.address = address ? address : null;
				marker.content = title ? title : null;
				if (marker.content || this.updateContentWithAddress) {
					google.maps.event.addListener(marker, 'click', function() {
						showInfoWindow(this, map);
					});
				}
				if (infowindow) infowindow.close();
				return loc;
			},

			updateContentWithAddress : true,

			show : function() {
				if (map) {
					return;
				}
				var element = document.getElementById(id);
				if (!element) { 
					throw "can't find element '" + id + "' for google map.";
				}
				map = new google.maps.Map(element, options);

				var bounds = new google.maps.LatLngBounds();
				var count = locations.length;

				for (var i=0; i<count; i++) {
					var loc = locations[i];
					var marker = makeColouredMarker(loc);
					markers.push(marker);
					marker.setMap(map);
					if (marker.content || this.updateContentWithAddress) {
						google.maps.event.addListener(marker, 'click', function() {
							showInfoWindow(this, map);
						});
					}
					bounds.extend(loc);
				}

				if (options.zoom) {
					map.setZoom(options.zoom);
				}
				if (options.center) {
					map.setCenter(options.center);
				} else if (count>=1) {
					map.fitBounds(bounds);
				}

				if (options.callbackUrl) {
					google.maps.event.addListenerOnce(map,'idle',function() {
						currBounds = asBounds(map.getBounds());
						currZoom = map.getZoom();
						google.maps.event.addListenerOnce(map,'idle',mapChanged);
					});
				}

			}

		};
		
	}

	function prefixed(prefix,url) {
		return prefix ? prefix+url : url;
	}

     //Not being used
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

	 var makeColouredMarker = function(loc) {
         var colour = loc.args[1];
         var desc = loc.desc;
         var prefix= window.location.protocol+ "//" + window.location.hostname + "/fieldid/";

         //Use default marker if no colour is provided
		 if (!colour) {
             var marker = new google.maps.Marker({
                 draggable : false,
                 position: loc
             });
             marker.content = desc;
             return marker;
		 }

		 var icon = '';
		 if (colour.toUpperCase()=='GREEN') {
			 icon = prefixed(prefix,'images/marker-images/greenMapIcon.png');
		 } else if (colour.toUpperCase()=='GREEN_A') {
             icon = prefixed(prefix,'images/marker-images/greenMapIconA.png');
         } else if (colour.toUpperCase()=='GRAY') {
			 icon =  prefixed(prefix,'images/marker-images/grayMapIcon.png');
		 } else if (colour.toUpperCase()=='YELLOW') {
			 icon =  prefixed(prefix,'images/marker-images/yellowMapIcon.png');
         } else if (colour.toUpperCase()=='YELLOW_A') {
             icon =  prefixed(prefix,'images/marker-images/yellowMapIconA.png');
		 } else if (colour.toUpperCase()=='RED') {
             icon =  prefixed(prefix,'images/marker-images/redMapIcon.png');
         } else if (colour.toUpperCase()=='RED_A') {
             icon =  prefixed(prefix,'images/marker-images/redMapIconA.png');
         } else if (colour.toUpperCase()=='BLUE') {
			 icon =  prefixed(prefix,'images/marker-images/blueMapIcon.png');
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

		 marker.content = desc;
		 return marker;
	 };

	return { 
		create : create,
		createAndShow : createAndShow,
		makeMarkerForStatus : makeMarkerForStatus,
		createAutoCompleteAddress : createAutoCompleteAddress
	};

	
})();



		
