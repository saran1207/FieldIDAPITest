var savedItemsDropBoxShown = false;

function showDropDown() {
    savedItemsDropBoxShown = true;
    jQuery('#mySavedItemsBox').load("/fieldid/w/savedItems", function() { jQuery(this).slideDown(80) } );
    var boxSelector = "#mySavedItemsBox";
    var linkSelector = ".js-page-actions";
    var withinSelector = ".js-page-header";
    if (typeof(Prototype) == 'object') {
        boxSelector = 'mySavedItemsBox';
        linkSelector = 'pageActions';
        withinSelector = "pageHeader";
    }
    //translateWithin($(boxSelector), $(linkSelector), $(withinSelector), 70, 673);
    translateWithin($(boxSelector), $(linkSelector), $(withinSelector), 30);
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

function listenForSavedItemsHover() {
    jQuery("#mySavedItemsLink").hover(
        function() {
            showDropDown(); 
        }, 
        function() {
            jQuery(this).parents('#pageActions').mouseleave(hideDropDown);

            // new template/framework header
            jQuery(this).parents('.js-nav-user').mouseleave(hideDropDown);
        }
    );
}

//jQuery(document).ready(function() { listenForSavedItemsClick(); });
jQuery(document).ready(function() { listenForSavedItemsHover(); });



