var ordersUrl = "";
var marryOrderUrl = "";
var marryOrderTitle = "";

function selectOrder(assetId, lineItemId) {

    var url = marryOrderUrl + "?uniqueID="+assetId+"&lineItemId="+lineItemId;
    getResponse( url );
    setTimeout("forceReload()", 1000);
}


function setupMarryOrder() {
    $('#orderForm').bind('submit', submitAjaxForm);
}


function submitAjaxForm(event) {
    // block default form submit
    event.preventDefault();

    $.get(
        ordersUrl ,
        $('#orderForm').serialize(),
        function (data) {
            $.colorbox({html:data, title: marryOrderTitle,  onComplete: setupMarryOrder});
        }
    );
    return false;
}
