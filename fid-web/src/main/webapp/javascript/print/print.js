$( document ).ready(function() {
	var page = $('<div class="print-form-page"></div>').appendTo('.print-form');
	$('#source .data-container-header').clone().appendTo(page);
	var currentDataContainer = $('<ul class="data-container"></ul>').appendTo(page);

	var counter = 0;

	$('.data-item').each( function( index, elem ){
		var withElem = counter + $(elem).height();

		if( $(elem).hasClass('short-form-steps') ){
			withElem = withElem + 10;
		}

		if( withElem < 500){
			counter = withElem;
			$(elem).appendTo(currentDataContainer);

		} else {
			counter = $(elem).height();
			$('#source .data-container-footer').clone().appendTo(page);
			
			page = $('<div class="print-form-page"></div>').appendTo('.print-form');
			$('#source .data-container-header').clone().appendTo(page);
			currentDataContainer = $('<ul class="data-container"></ul>').appendTo(page);
			
			$(elem).appendTo(currentDataContainer);
		}
	});
	$('#source .data-container-footer').clone().appendTo(page);
	$('#source').hide();
});