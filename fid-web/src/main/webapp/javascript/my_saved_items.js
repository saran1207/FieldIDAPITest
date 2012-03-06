var savedItemsDropBoxShown = false;

function showDropDown() {
    savedItemsDropBoxShown = true;
    jQuery('#mySavedItemsBox').load("/fieldid/w/savedItems", function() { jQuery(this).slideDown(80) } );
    var boxSelector = "#mySavedItemsBox";
    var linkSelector = "#pageActions";
    var withinSelector = "#pageHeader";
    if (typeof(Prototype) == 'object') {
        boxSelector = 'mySavedItemsBox';
        linkSelector = 'pageActions';
        withinSelector = "pageHeader";
    }
    translateWithin($(boxSelector), $(linkSelector), $(withinSelector), 70, 673);
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