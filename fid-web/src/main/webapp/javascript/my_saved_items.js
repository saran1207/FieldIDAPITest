function showDropBox() {
    jQuery('#mySavedItemsBox').load("/fieldid/w/savedItems", function() { jQuery(this).slideDown() } );
    var boxSelector = "#mySavedItemsBox";
    var linkSelector = "#mySavedItemsLink";
    if (typeof(Prototype) == 'object') {
        boxSelector = 'mySavedItemsBox';
        linkSelector = 'mySavedItemsLink';
    }
    translate($(boxSelector), $(linkSelector), 15, -110);
    jQuery("#mySavedItemsLink").parents("li").addClass("openedSavedItemsLink");
}

function hideDropBox() {
    jQuery('#mySavedItemsBox').fadeOut();
    jQuery("#mySavedItemsLink").parents("li").removeClass("openedSavedItemsLink");
}

function listenForSavedItemsHover() {
    jQuery('#mySavedItemsLink').mouseenter(showDropBox);
    jQuery('#mySavedItemsLink').parents('#pageActions').mouseleave(hideDropBox);
}

jQuery(document).ready(function() { listenForSavedItemsHover(); });