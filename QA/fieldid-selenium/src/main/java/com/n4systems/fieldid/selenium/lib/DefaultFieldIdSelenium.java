package com.n4systems.fieldid.selenium.lib;

import com.thoughtworks.selenium.Selenium;

public class DefaultFieldIdSelenium implements FieldIdSelenium {

	private final Selenium delegateSelenium;

	public DefaultFieldIdSelenium(Selenium delegateSelenium) {
		super();
		this.delegateSelenium = delegateSelenium;
	}
	
	
	public void waitForAjax(String timeout) throws InterruptedException {
		delegateSelenium.waitForCondition("selenium.browserbot.getCurrentWindow().Ajax.activeRequestCount == 0;", timeout);
	}
	
	public void waitForElementToBePresent(String locator, String timeout) throws InterruptedException {
		delegateSelenium.waitForCondition("var value = selenium.isElementPresent( '" + locator + "'); value == true", timeout);
	}

	
	
	
	/**
	 * DELEGATE METHODS
	 */
	public void addCustomRequestHeader(String key, String value) {
		delegateSelenium.addCustomRequestHeader(key, value);
	}

	public void addLocationStrategy(String strategyName, String functionDefinition) {
		delegateSelenium.addLocationStrategy(strategyName, functionDefinition);
	}

	public void addScript(String scriptContent, String scriptTagId) {
		delegateSelenium.addScript(scriptContent, scriptTagId);
	}

	public void addSelection(String locator, String optionLocator) {
		delegateSelenium.addSelection(locator, optionLocator);
	}

	public void allowNativeXpath(String allow) {
		delegateSelenium.allowNativeXpath(allow);
	}

	public void altKeyDown() {
		delegateSelenium.altKeyDown();
	}

	public void altKeyUp() {
		delegateSelenium.altKeyUp();
	}

	public void answerOnNextPrompt(String answer) {
		delegateSelenium.answerOnNextPrompt(answer);
	}

	public void assignId(String locator, String identifier) {
		delegateSelenium.assignId(locator, identifier);
	}

	public void attachFile(String fieldLocator, String fileLocator) {
		delegateSelenium.attachFile(fieldLocator, fileLocator);
	}

	public void captureEntirePageScreenshot(String filename, String kwargs) {
		delegateSelenium.captureEntirePageScreenshot(filename, kwargs);
	}

	public String captureEntirePageScreenshotToString(String kwargs) {
		return delegateSelenium.captureEntirePageScreenshotToString(kwargs);
	}

	public String captureNetworkTraffic(String type) {
		return delegateSelenium.captureNetworkTraffic(type);
	}

	public void captureScreenshot(String filename) {
		delegateSelenium.captureScreenshot(filename);
	}

	public String captureScreenshotToString() {
		return delegateSelenium.captureScreenshotToString();
	}

	public void check(String locator) {
		delegateSelenium.check(locator);
	}

	public void chooseCancelOnNextConfirmation() {
		delegateSelenium.chooseCancelOnNextConfirmation();
	}

	public void chooseOkOnNextConfirmation() {
		delegateSelenium.chooseOkOnNextConfirmation();
	}

	public void click(String locator) {
		delegateSelenium.click(locator);
	}

	public void clickAt(String locator, String coordString) {
		delegateSelenium.clickAt(locator, coordString);
	}

	public void close() {
		delegateSelenium.close();
	}

	public void contextMenu(String locator) {
		delegateSelenium.contextMenu(locator);
	}

	public void contextMenuAt(String locator, String coordString) {
		delegateSelenium.contextMenuAt(locator, coordString);
	}

	public void controlKeyDown() {
		delegateSelenium.controlKeyDown();
	}

	public void controlKeyUp() {
		delegateSelenium.controlKeyUp();
	}

	public void createCookie(String nameValuePair, String optionsString) {
		delegateSelenium.createCookie(nameValuePair, optionsString);
	}

	public void deleteAllVisibleCookies() {
		delegateSelenium.deleteAllVisibleCookies();
	}

	public void deleteCookie(String name, String optionsString) {
		delegateSelenium.deleteCookie(name, optionsString);
	}

	public void deselectPopUp() {
		delegateSelenium.deselectPopUp();
	}

	public void doubleClick(String locator) {
		delegateSelenium.doubleClick(locator);
	}

	public void doubleClickAt(String locator, String coordString) {
		delegateSelenium.doubleClickAt(locator, coordString);
	}

	public void dragAndDrop(String locator, String movementsString) {
		delegateSelenium.dragAndDrop(locator, movementsString);
	}

	public void dragAndDropToObject(String locatorOfObjectToBeDragged, String locatorOfDragDestinationObject) {
		delegateSelenium.dragAndDropToObject(locatorOfObjectToBeDragged, locatorOfDragDestinationObject);
	}

	public void dragdrop(String locator, String movementsString) {
		delegateSelenium.dragdrop(locator, movementsString);
	}

	public void fireEvent(String locator, String eventName) {
		delegateSelenium.fireEvent(locator, eventName);
	}

	public void focus(String locator) {
		delegateSelenium.focus(locator);
	}

	public String getAlert() {
		return delegateSelenium.getAlert();
	}

	public String[] getAllButtons() {
		return delegateSelenium.getAllButtons();
	}

	public String[] getAllFields() {
		return delegateSelenium.getAllFields();
	}

	public String[] getAllLinks() {
		return delegateSelenium.getAllLinks();
	}

	public String[] getAllWindowIds() {
		return delegateSelenium.getAllWindowIds();
	}

	public String[] getAllWindowNames() {
		return delegateSelenium.getAllWindowNames();
	}

	public String[] getAllWindowTitles() {
		return delegateSelenium.getAllWindowTitles();
	}

	public String getAttribute(String attributeLocator) {
		return delegateSelenium.getAttribute(attributeLocator);
	}

	public String[] getAttributeFromAllWindows(String attributeName) {
		return delegateSelenium.getAttributeFromAllWindows(attributeName);
	}

	public String getBodyText() {
		return delegateSelenium.getBodyText();
	}

	public String getConfirmation() {
		return delegateSelenium.getConfirmation();
	}

	public String getCookie() {
		return delegateSelenium.getCookie();
	}

	public String getCookieByName(String name) {
		return delegateSelenium.getCookieByName(name);
	}

	public Number getCursorPosition(String locator) {
		return delegateSelenium.getCursorPosition(locator);
	}

	public Number getElementHeight(String locator) {
		return delegateSelenium.getElementHeight(locator);
	}

	public Number getElementIndex(String locator) {
		return delegateSelenium.getElementIndex(locator);
	}

	public Number getElementPositionLeft(String locator) {
		return delegateSelenium.getElementPositionLeft(locator);
	}

	public Number getElementPositionTop(String locator) {
		return delegateSelenium.getElementPositionTop(locator);
	}

	public Number getElementWidth(String locator) {
		return delegateSelenium.getElementWidth(locator);
	}

	public String getEval(String script) {
		return delegateSelenium.getEval(script);
	}

	public String getExpression(String expression) {
		return delegateSelenium.getExpression(expression);
	}

	public String getHtmlSource() {
		return delegateSelenium.getHtmlSource();
	}

	public String getLocation() {
		return delegateSelenium.getLocation();
	}

	public Number getMouseSpeed() {
		return delegateSelenium.getMouseSpeed();
	}

	public String getPrompt() {
		return delegateSelenium.getPrompt();
	}

	public String getSelectedId(String selectLocator) {
		return delegateSelenium.getSelectedId(selectLocator);
	}

	public String[] getSelectedIds(String selectLocator) {
		return delegateSelenium.getSelectedIds(selectLocator);
	}

	public String getSelectedIndex(String selectLocator) {
		return delegateSelenium.getSelectedIndex(selectLocator);
	}

	public String[] getSelectedIndexes(String selectLocator) {
		return delegateSelenium.getSelectedIndexes(selectLocator);
	}

	public String getSelectedLabel(String selectLocator) {
		return delegateSelenium.getSelectedLabel(selectLocator);
	}

	public String[] getSelectedLabels(String selectLocator) {
		return delegateSelenium.getSelectedLabels(selectLocator);
	}

	public String getSelectedValue(String selectLocator) {
		return delegateSelenium.getSelectedValue(selectLocator);
	}

	public String[] getSelectedValues(String selectLocator) {
		return delegateSelenium.getSelectedValues(selectLocator);
	}

	public String[] getSelectOptions(String selectLocator) {
		return delegateSelenium.getSelectOptions(selectLocator);
	}

	public String getSpeed() {
		return delegateSelenium.getSpeed();
	}

	public String getTable(String tableCellAddress) {
		return delegateSelenium.getTable(tableCellAddress);
	}

	public String getText(String locator) {
		return delegateSelenium.getText(locator);
	}

	public String getTitle() {
		return delegateSelenium.getTitle();
	}

	public String getValue(String locator) {
		return delegateSelenium.getValue(locator);
	}

	public boolean getWhetherThisFrameMatchFrameExpression(String currentFrameString, String target) {
		return delegateSelenium.getWhetherThisFrameMatchFrameExpression(currentFrameString, target);
	}

	public boolean getWhetherThisWindowMatchWindowExpression(String currentWindowString, String target) {
		return delegateSelenium.getWhetherThisWindowMatchWindowExpression(currentWindowString, target);
	}

	public Number getXpathCount(String xpath) {
		return delegateSelenium.getXpathCount(xpath);
	}

	public void goBack() {
		delegateSelenium.goBack();
	}

	public void highlight(String locator) {
		delegateSelenium.highlight(locator);
	}

	public void ignoreAttributesWithoutValue(String ignore) {
		delegateSelenium.ignoreAttributesWithoutValue(ignore);
	}

	public boolean isAlertPresent() {
		return delegateSelenium.isAlertPresent();
	}

	public boolean isChecked(String locator) {
		return delegateSelenium.isChecked(locator);
	}

	public boolean isConfirmationPresent() {
		return delegateSelenium.isConfirmationPresent();
	}

	public boolean isCookiePresent(String name) {
		return delegateSelenium.isCookiePresent(name);
	}

	public boolean isEditable(String locator) {
		return delegateSelenium.isEditable(locator);
	}

	public boolean isElementPresent(String locator) {
		return delegateSelenium.isElementPresent(locator);
	}

	public boolean isOrdered(String locator1, String locator2) {
		return delegateSelenium.isOrdered(locator1, locator2);
	}

	public boolean isPromptPresent() {
		return delegateSelenium.isPromptPresent();
	}

	public boolean isSomethingSelected(String selectLocator) {
		return delegateSelenium.isSomethingSelected(selectLocator);
	}

	public boolean isTextPresent(String pattern) {
		return delegateSelenium.isTextPresent(pattern);
	}

	public boolean isVisible(String locator) {
		return delegateSelenium.isVisible(locator);
	}

	public void keyDown(String locator, String keySequence) {
		delegateSelenium.keyDown(locator, keySequence);
	}

	public void keyDownNative(String keycode) {
		delegateSelenium.keyDownNative(keycode);
	}

	public void keyPress(String locator, String keySequence) {
		delegateSelenium.keyPress(locator, keySequence);
	}

	public void keyPressNative(String keycode) {
		delegateSelenium.keyPressNative(keycode);
	}

	public void keyUp(String locator, String keySequence) {
		delegateSelenium.keyUp(locator, keySequence);
	}

	public void keyUpNative(String keycode) {
		delegateSelenium.keyUpNative(keycode);
	}

	public void metaKeyDown() {
		delegateSelenium.metaKeyDown();
	}

	public void metaKeyUp() {
		delegateSelenium.metaKeyUp();
	}

	public void mouseDown(String locator) {
		delegateSelenium.mouseDown(locator);
	}

	public void mouseDownAt(String locator, String coordString) {
		delegateSelenium.mouseDownAt(locator, coordString);
	}

	public void mouseDownRight(String locator) {
		delegateSelenium.mouseDownRight(locator);
	}

	public void mouseDownRightAt(String locator, String coordString) {
		delegateSelenium.mouseDownRightAt(locator, coordString);
	}

	public void mouseMove(String locator) {
		delegateSelenium.mouseMove(locator);
	}

	public void mouseMoveAt(String locator, String coordString) {
		delegateSelenium.mouseMoveAt(locator, coordString);
	}

	public void mouseOut(String locator) {
		delegateSelenium.mouseOut(locator);
	}

	public void mouseOver(String locator) {
		delegateSelenium.mouseOver(locator);
	}

	public void mouseUp(String locator) {
		delegateSelenium.mouseUp(locator);
	}

	public void mouseUpAt(String locator, String coordString) {
		delegateSelenium.mouseUpAt(locator, coordString);
	}

	public void mouseUpRight(String locator) {
		delegateSelenium.mouseUpRight(locator);
	}

	public void mouseUpRightAt(String locator, String coordString) {
		delegateSelenium.mouseUpRightAt(locator, coordString);
	}

	public void open(String url) {
		delegateSelenium.open(url);
	}

	public void openWindow(String url, String windowID) {
		delegateSelenium.openWindow(url, windowID);
	}

	public void refresh() {
		delegateSelenium.refresh();
	}

	public void removeAllSelections(String locator) {
		delegateSelenium.removeAllSelections(locator);
	}

	public void removeScript(String scriptTagId) {
		delegateSelenium.removeScript(scriptTagId);
	}

	public void removeSelection(String locator, String optionLocator) {
		delegateSelenium.removeSelection(locator, optionLocator);
	}

	public String retrieveLastRemoteControlLogs() {
		return delegateSelenium.retrieveLastRemoteControlLogs();
	}

	public void rollup(String rollupName, String kwargs) {
		delegateSelenium.rollup(rollupName, kwargs);
	}

	public void runScript(String script) {
		delegateSelenium.runScript(script);
	}

	public void select(String selectLocator, String optionLocator) {
		delegateSelenium.select(selectLocator, optionLocator);
	}

	public void selectFrame(String locator) {
		delegateSelenium.selectFrame(locator);
	}

	public void selectPopUp(String windowID) {
		delegateSelenium.selectPopUp(windowID);
	}

	public void selectWindow(String windowID) {
		delegateSelenium.selectWindow(windowID);
	}

	public void setBrowserLogLevel(String logLevel) {
		delegateSelenium.setBrowserLogLevel(logLevel);
	}

	public void setContext(String context) {
		delegateSelenium.setContext(context);
	}

	public void setCursorPosition(String locator, String position) {
		delegateSelenium.setCursorPosition(locator, position);
	}

	public void setExtensionJs(String extensionJs) {
		delegateSelenium.setExtensionJs(extensionJs);
	}

	public void setMouseSpeed(String pixels) {
		delegateSelenium.setMouseSpeed(pixels);
	}

	public void setSpeed(String value) {
		delegateSelenium.setSpeed(value);
	}

	public void setTimeout(String timeout) {
		delegateSelenium.setTimeout(timeout);
	}

	public void shiftKeyDown() {
		delegateSelenium.shiftKeyDown();
	}

	public void shiftKeyUp() {
		delegateSelenium.shiftKeyUp();
	}

	public void showContextualBanner() {
		delegateSelenium.showContextualBanner();
	}

	public void showContextualBanner(String className, String methodName) {
		delegateSelenium.showContextualBanner(className, methodName);
	}

	public void shutDownSeleniumServer() {
		delegateSelenium.shutDownSeleniumServer();
	}

	public void start() {
		delegateSelenium.start();
	}

	public void start(Object optionsObject) {
		delegateSelenium.start(optionsObject);
	}

	public void start(String optionsString) {
		delegateSelenium.start(optionsString);
	}

	public void stop() {
		delegateSelenium.stop();
	}

	public void submit(String formLocator) {
		delegateSelenium.submit(formLocator);
	}

	public void type(String locator, String value) {
		delegateSelenium.type(locator, value);
	}

	public void typeKeys(String locator, String value) {
		delegateSelenium.typeKeys(locator, value);
	}

	public void uncheck(String locator) {
		delegateSelenium.uncheck(locator);
	}

	public void useXpathLibrary(String libraryName) {
		delegateSelenium.useXpathLibrary(libraryName);
	}

	public void waitForCondition(String script, String timeout) {
		delegateSelenium.waitForCondition(script, timeout);
	}

	public void waitForFrameToLoad(String frameAddress, String timeout) {
		delegateSelenium.waitForFrameToLoad(frameAddress, timeout);
	}

	public void waitForPageToLoad(String timeout) {
		delegateSelenium.waitForPageToLoad(timeout);
	}

	public void waitForPopUp(String windowID, String timeout) {
		delegateSelenium.waitForPopUp(windowID, timeout);
	}

	public void windowFocus() {
		delegateSelenium.windowFocus();
	}

	public void windowMaximize() {
		delegateSelenium.windowMaximize();
	}


	
	
	
	
	
	
}
