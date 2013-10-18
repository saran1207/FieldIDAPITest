var layoutListShown = false;

function showList() {
    if ($('#layoutCount').val() > 1) {
        jQuery('#layoutList').slideDown(100);
        layoutListShown = true;
    }
}

function hideList() {
    jQuery('#layoutList').fadeOut();
    layoutListShown = false;
}

function toggleList() {
    if(layoutListShown) {
        hideList();
    } else {
        showList();
    }
}

function listenForLayoutListClick() {
    jQuery('#layoutListLink').click(toggleList);
    jQuery('#layoutList').mouseleave(hideList);
    if ($('#layoutCount').val() <= 1) {
        $('#downArrow').hide();
    }
}

jQuery(document).ready(function() {
    listenForLayoutListClick();
});
