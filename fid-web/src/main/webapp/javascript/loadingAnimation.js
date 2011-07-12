function positionModalContainer() {
	var modelPanel = $('modalLoadingPanel');
	var parentContainer = $(modelPanel.parentNode.parentNode);
	
	translate(modelPanel, parentContainer, 0, 0);
	modelPanel.setStyle({
		'width': parentContainer.getWidth() + "px",
		'height': parentContainer.getHeight() + "px"});
}