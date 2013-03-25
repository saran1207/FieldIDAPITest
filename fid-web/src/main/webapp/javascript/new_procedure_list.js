var newProcedureList = false;

function showList() {
    jQuery('.new-procedure-types').slideDown(100);
    newProcedureList = true;
}

function hideList() {
    jQuery('.new-procedure-types').fadeOut();
    newProcedureList = false;
}

function toggleList() {
    if(newProcedureList) {
        hideList();
    } else {
        showList();
    }
}

function listenForNewProcedureListClick() {
    jQuery('.new-procedure').click(toggleList);
    jQuery('.new-procedure-types').mouseleave(hideList);
}

jQuery(document).ready(function() { listenForNewProcedureListClick(); });
