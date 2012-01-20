var savedItemsDropBoxShown = false;

function showDropDown() {
    savedItemsDropBoxShown = true;
    jQuery('#mySavedItemsBox').load("/fieldid/w/savedItems", function() { jQuery(this).slideDown(80) } );
    var boxSelector = "#mySavedItemsBox";
    var linkSelector = "#mySavedItemsLink";
    if (typeof(Prototype) == 'object') {
        boxSelector = 'mySavedItemsBox';
        linkSelector = 'mySavedItemsLink';
    }
    translate($(boxSelector), $(linkSelector), 24, -221);
}

function hideDropDown() {
    savedItemsDropBoxShown = false;
    jQuery('#mySavedItemsBox').fadeOut();
}

function toggleDropBox() {
    if (savedItemsDropBoxShown) {
        hideDropDown();
    } else {
        showDropDown();
    }
}

function listenForSavedItemsClick() {
    jQuery('#mySavedItemsLink').click(toggleDropBox);
    jQuery('#mySavedItemsLink').parents('#pageActions').mouseleave(hideDropDown);
}

jQuery(document).ready(function() { listenForSavedItemsClick(); });