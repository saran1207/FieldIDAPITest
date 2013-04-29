var defaults={};

function createLabel(annotation) {
    var value = annotation.text;
    var direction = annotation.x < .5 ? 'arrow-left' : 'arrow-right';
    var type = annotation.cssStyle;
    var span = $(document.createElement('span')).addClass('readonly').addClass('note').addClass(direction).addClass(type);
    var icon = $('<span/>').addClass('icon').appendTo(span);
    var viewPanel = $('<span>'+value+'</span>').addClass("noteText").appendTo(span);
    viewPanel.css('width',(value.length + 1) * 6 + 'px');
    return span;
}

function annotateLockingImages() {
    $('.lockingTimeline div.media-image').each(
        function(index, element) {
            $(element).addAnnotations(
                function(annotation) {
                    return createLabel(annotation);
                }, [ isolationAnnotations[index] ], defaults);
        });
}

function annotateUnlockingImages() {
    $('.unlockingTimeline div.media-image').each(
        function(index, element) {
            $(element).addAnnotations(
                function(annotation) {
                    return createLabel(annotation);
                }, [ isolationAnnotations[isolationAnnotations.length - 1 - index] ], defaults);
        });
}


var lockTimeoutCheck = null;
var unlockTimeoutCheck = null;

function waitForLockingTimelineToLoadThenAnnotate() {
    lockTimeoutCheck = window.setInterval(annotateLockAfterLoad, 200);
}

function waitForUnlockingTimelineToLoadThenAnnotate() {
    unlockTimeoutCheck = window.setInterval(annotateUnlockAfterLoad, 200);
}

function annotateLockAfterLoad() {
    if ($('.lockingTimeline div.media-image').size() > 0) {
        annotateLockingImages();
        window.clearInterval(lockTimeoutCheck);
    }
}

function annotateUnlockAfterLoad() {
    if ($('.unlockingTimeline div.media-image').size() > 0) {
        annotateUnlockingImages();
        window.clearInterval(unlockTimeoutCheck);
    }
}