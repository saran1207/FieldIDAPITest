var defaults={};

function createLabel(annotation) {
    var value = annotation.text;
    var direction =  annotation.x < .5 ? 'arrow-left' : 'arrow-right';
    var type = annotation.cssStyle;
    var span = $(document.createElement('span')).addClass('readonly').addClass('note').addClass(direction).addClass(type);
    var icon = $('<span/>').addClass('icon').appendTo(span);
    var viewPanel = $('<span>'+value+'</span>').addClass("noteText").appendTo(span);
    viewPanel.css('width',(value.length + 1) * 6 + 'px');
    return span;
}


function annotateLockingImages() {
    $('span.note').remove();
    $('.timelineContainer div.media-image').each(
        function(index, element) {
            var currentAnnotation = isolationAnnotations[index];
            $(element).addAnnotations(
                function(annotation) {
                    return createLabel(annotation);
                }, [ currentAnnotation ], { xPosition:"left" } );
        });
}

function annotateUnlockingImages() {
    $('span.note').remove();
    $('.timelineContainer div.media-image').each(
        function(index, element) {
            var currentAnnotation = isolationAnnotations[isolationAnnotations.length - 1 - index];
            $(element).addAnnotations(
                function(annotation) {
                    return createLabel(annotation);
                }, [ currentAnnotation ], { xPosition:"left" });
        });
}

var unlockingState = false;

var waitForTimelineToShowUp = null;

function waitForTimelineToLoadThenAnnotate() {
    waitForTimelineToShowUp = window.setInterval(checkIfTimelineIsThereYet, 150);
}

function redrawAnnotations() {
    if (unlockingState) {
        annotateUnlockingImages();
    } else {
        annotateLockingImages();
    }
}

function checkIfTimelineIsThereYet() {
    if ($('.timelineContainer div.media-image img').size() > 0) {

        var firstImage = $($('.timelineContainer div.media-image img')[0]);
        var firstContainer = $(firstImage).parent();

        if (firstContainer.width() > 0) {
            if (firstImage.width()>0) {
                redrawAnnotations();
            }
            firstImage.load(function() { redrawAnnotations() });
            window.clearInterval(waitForTimelineToShowUp);
        }
    }
}

$(document).ready(function() {
    waitForTimelineToLoadThenAnnotate();
    $('.timelineContainer').bind("UPDATE", redrawAnnotations);
});
