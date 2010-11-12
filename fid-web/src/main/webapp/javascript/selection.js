var addItemsToSelectionUrl = '';
var deleteItemsFromSelectionUrl = '';
var addEntireSearchToSelectionUrl = '';
var clearSelectionUrl = '';

var itemIdsOnCurrentPage = [];

function toggleEntirePage(searchContainerKey, searchId, isItChecked) {
    if (isItChecked) {
        callUrlWithIds(addItemsToSelectionUrl, itemIdsOnCurrentPage, searchContainerKey, searchId);
        showRowsSelected(itemIdsOnCurrentPage);
    } else {
        callUrlWithIds(deleteItemsFromSelectionUrl, itemIdsOnCurrentPage, searchContainerKey, searchId);
        showRowsUnselected(itemIdsOnCurrentPage);
    }
}

function resultSelectionChanged(id, searchContainerKey, searchId, isItChecked) {
    if (isItChecked) {
        callUrlWithIds(addItemsToSelectionUrl, [id], searchContainerKey, searchId);
        showRowsSelected([id]);
    } else {
        callUrlWithIds(deleteItemsFromSelectionUrl, [id], searchContainerKey, searchId);
        showRowsUnselected([id]);
    }
    checkHeaderBoxIfAllItemsAreSelected();
}

function showRowsSelected(ids) {
    ids.each(function(id){
        checkboxForEntityId(id).checked = true;
        rowForEntityId(id).addClassName('multiSelected');
    });
}

function showRowsUnselected(ids) {
    ids.each(function(id){
        checkboxForEntityId(id).checked = false;
        rowForEntityId(id).removeClassName('multiSelected');
    });
}

function rowForEntityId(id) {
    return $('row-'+id);
}

function checkboxForEntityId(id) {
    return $$('#row-' + id + ' input')[0];
}

function callUrlWithIds(url, ids, searchContainerKey, searchId) {
    var params = new Object();
    for (var i = 0; i < ids.length; i++) {
        params['ids['+i+']'] = ids[i];
    }
    params.searchId = searchId;
    params.searchContainerKey = searchContainerKey;
    getResponse(url, "post", params);
}

function displayNumSelected(numSelected) {
    $('numSelectedItems').update(numSelected);
}

function checkHeaderBoxIfAllItemsAreSelected() {
    var numCheckedBoxes = 0;
    itemIdsOnCurrentPage.each(function(id) {
        if (checkboxForEntityId(id).checked) {
            numCheckedBoxes++;
        }
    });
    $('selectAllBox').checked = (numCheckedBoxes == itemIdsOnCurrentPage.length);
}

function displaySelectionNotice(html) {
    $$('.selectionNotice').each(function(element) {
        element.update(html);
    });
}

function addEntireResultToSelection(searchContainerKey, searchId) {
    callUrlWithKeyAndId(addEntireSearchToSelectionUrl, searchContainerKey, searchId);
}

function clearSelection(searchContainerKey, searchId) {
    showRowsUnselected(itemIdsOnCurrentPage);
    callUrlWithKeyAndId(clearSelectionUrl, searchContainerKey, searchId);
}

function callUrlWithKeyAndId(url, searchContainerKey, searchId) {
    var params = new Object();
    params.searchContainerKey = searchContainerKey;
    params.searchId = searchId;
    checkHeaderBoxIfAllItemsAreSelected();
    getResponse(url, "post", params);
}

function numSelectedItems() {
    return parseInt($('numSelectedItems').innerHTML);
}

document.observe('dom:loaded', function() {
    itemIdsOnCurrentPage.each(function(id) {
        if (checkboxForEntityId(id).checked) {
            showRowsSelected([id])
        }
    });
});