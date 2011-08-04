
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
		var coordinates = '(' + latitude + ' , ' + longitude + ')';
		
		var marker = new google.maps.Marker({
		    position: loc,
		    map: map,
		    title : coordinates
		});
		return map;		
	};
 
	return {
		initialize: initialize
	};
 
})();



		
