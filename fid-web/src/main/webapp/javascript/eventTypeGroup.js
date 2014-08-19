/**
 * Created by rrana on 2014-08-12.
 */
Event.observe(window, 'load', function() {

    $$('.printOutDetails input').each( function(elt) {
        elt.observe('click', function() {
            clearAndSetBorders(elt);
        });
    });

    $$('.printOutThumbnail', '.noImageContainer').each( function(elt) {
        elt.observe('click', function() {
            clearAndSetBorders(elt);
            elt.adjacent('.printOutDetails').first().down('input').click();
        });
    });
});

function clearAndSetBorders(selectedElement){
    $$('.printOutSelection li').each(function(elt) {
        elt.setStyle({'padding': '5px', 'border' : '1px solid #D0DAFD'});
    });

    selectedElement.up('li').setStyle({'padding': '4px','border' : '2px solid #D0DAFD'});
};
