$(document).ready(function() {
    $('img').imagesLoaded(function() {
        //-----------------[ Calculate the growth of the caution container ]-----------
        var offset    = $('#caution-text').outerHeight();
        var minHeight = 67; // the min-height set in css

        if( offset != minHeight ){
            offset = offset * 1.2117;       // fix problem with outerHeight reporting the wrong height
            offset = offset - minHeight;    // the amount the container has grown
            offset = offset - 1;            // add padding
        } else {
            offset = 0;
        }

        //-----------------[ Create Page ]---------------------------------------------
        var page = $('<div class="print-form-page"></div>').appendTo('body');
        $('#source .data-container-header').clone().appendTo(page);
        var currentDataContainer = $('<ul class="data-container"></ul>').appendTo(page);


        $('.data-item').each( function( index, elem ){
            //---------------------[ add element to current page ]---------------------
            $(elem).appendTo(currentDataContainer);

            //---------------------[ if the element overflows the page ]---------------
            if( $(currentDataContainer).outerHeight(true) > (630 - offset)){
                //-----------------[ end current page - add footer to page ]-----------
                $('#source .data-container-footer').clone().appendTo(page);

                //-----------------[ Create page ]-------------------------------------
                page = $('<div class="print-form-page"></div>').appendTo('body');
                $('#source .data-container-header').clone().appendTo(page);
                currentDataContainer = $('<ul class="data-container"></ul>').appendTo(page);

                //-----------------[ re-append element to new page ]-------------------
                $(elem).appendTo(currentDataContainer);
            }
        });
        //-------------------------[ add footer to page ]------------------------------
        $('#source .data-container-footer').clone().appendTo(page);
        $('#source').hide();

        //-------------------------[ resize the container to fit the page ]------------
        $('.data-container').height(645 - offset);
    });
});