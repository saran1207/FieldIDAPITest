$( document ).ready(function() {
    var page = $('<div class="print-form-page"></div>').appendTo('body');
    $('#source .data-container-header').clone().appendTo(page);
    var currentDataContainer = $('<ul class="data-container"></ul>').appendTo(page);


    $('.data-item').each( function( index, elem ){
        $(elem).appendTo(currentDataContainer);

        if( $(currentDataContainer).outerHeight(true) > 540 ){
            $('#source .data-container-footer').clone().appendTo(page);

            page = $('<div class="print-form-page"></div>').appendTo('body');
            $('#source .data-container-header').clone().appendTo(page);
            currentDataContainer = $('<ul class="data-container"></ul>').appendTo(page);

            $(elem).appendTo(currentDataContainer);
        }
    });
    $('#source .data-container-footer').clone().appendTo(page);
   // $('#source').hide();

    $('.data-container').height(595);
});