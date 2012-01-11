var savedItemsDropBoxDown = false;

function showDropBox() {
    savedItemsDropBoxDown = true;
    jQuery('#mySavedItemsBox').load("/fieldid/w/savedItems", function() { jQuery(this).slideDown(80) } );
    var boxSelector = "#mySavedItemsBox";
    var linkSelector = "#mySavedItemsLink";
    if (typeof(Prototype) == 'object') {
        boxSelector = 'mySavedItemsBox';
        linkSelector = 'mySavedItemsLink';
    }
    translate($(boxSelector), $(linkSelector), 14, -212);
    jQuery("#mySavedItemsLink").parents("li").addClass("openedSavedItemsLink");
}

function hideDropBox() {
    savedItemsDropBoxDown = false;
    jQuery('#mySavedItemsBox').fadeOut();
    jQuery("#mySavedItemsLink").parents("li").removeClass("openedSavedItemsLink");
}

function toggleDropBox() {
    if (savedItemsDropBoxDown) {
        hideDropBox();
    } else {
        showDropBox();
    }
}

function listenForSavedItemsClick() {
    jQuery('#mySavedItemsLink').click(toggleDropBox);
    jQuery('#mySavedItemsLink').parents('#pageActions').mouseleave(hideDropBox);
}

jQuery(document).ready(function() { listenForSavedItemsClick(); });