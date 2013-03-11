var newProcedureList = false;

function showList() {
    jQuery('#addNewContainer').slideDown(100);
    newProcedureList = true;
}

function hideList() {
    jQuery('#addNewContainer').fadeOut();
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
    jQuery('#newProcedureLink').click(toggleList);
    jQuery('#addNewContainer').mouseleave(hideList);
}

jQuery(document).ready(function() { listenForNewProcedureListClick(); });
