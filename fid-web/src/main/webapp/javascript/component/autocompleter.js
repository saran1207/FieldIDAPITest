/**
 * Created by rrana on 2015-05-22.
 */
jQuery("#searchText").autocomplete({
    source: function( request, response ) {
        $.ajax({
            url: "THIS NEEDS A URL",
            dataType: "jsonp",
            data: {
                q: request.term
            },
            success: function( data ) {
                response( data );
            }
        });
    },
    minLength: 3,
    select: function( event, ui ) {
        log( ui.item ?
        "Selected: " + ui.item.label :
        "Nothing selected, input was " + this.value);
    }
});