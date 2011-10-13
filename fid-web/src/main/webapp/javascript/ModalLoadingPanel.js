function positionModalContainer(modalPanelId, componentToCoverId) {
	var modelPanel = $("#"+modalPanelId);
	var componentToCover = $("#"+componentToCoverId);
	
	translate(modelPanel, componentToCover, 0, 0);
	modelPanel.css({
		'width': componentToCover.width() + "px",
		'height': componentToCover.height() + "px"});
}