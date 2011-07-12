function positionModalContainer(modalPanelId, componentToCoverId) {
	var modelPanel = $(modalPanelId);
	var componentToCover = $(componentToCoverId);
	
	translate(modelPanel, componentToCover, 0, 0);
	modelPanel.setStyle({
		'width': componentToCover.getWidth() + "px",
		'height': componentToCover.getHeight() + "px"});
}