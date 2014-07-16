function createAnnotation(data) {
    var direction = data.x < .5 ? 'arrow-left' : 'arrow-right';
    var el = $('<span class="readonly note '+ direction + ' ' + data.cssStyle + '"></span>');
    $('<span class="icon"></span>').appendTo(el);
    $('<input/>').attr({type:'text', value:data.text}).attr('disabled',true).appendTo(el);
    return el;
}
function annotateImages( json ) {
    $.each( json.images,function(index,data) {
        $('#'+data.id).parent().addAnnotations( createAnnotation, data.annotations, {xPosition:"left"} );
    });
}