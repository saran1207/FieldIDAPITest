function showList() {
    $("#layoutList").toggle("slide");
    $('#menuButton').hide();
    $('#hideMenuImage').show();
}

function hideList() {
    $("#layoutList").toggle("slide");
    $('#menuButton').show();
    $('#hideMenuImage').hide();
}