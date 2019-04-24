from PageObjectLibrary import PageObject

class CreateAssetPage(PageObject):
    PAGE_URL = "/fieldid/w/identify"

    _locators = {
        "serial_number_field": "//input[@name='singleIdentifyContainer:identifierContainer:identifier']",
        "rfid_number_field": "//input[@name='singleIdentifyContainer:rfidContainer:rfidNumber']",
        "save_button": "//input[@name='actionsContainer:saveButton']",
        "asset_type_dropdown": "(//a[@class='chzn-single'])[1]",
        "asset_type": "(//ul[@class='chzn-results'])[1]/li[text()='%s']"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def input_asset_serial_number(self, identifier):
        self.se2lib.wait_until_element_is_visible(self.locator.serial_number_field)
        self.se2lib.input_text(self.locator.serial_number_field, identifier)

    def input_rfid_number(self, rfidNumber):
        self.se2lib.wait_until_element_is_visible(self.locator.rfid_number_field)
        self.se2lib.input_text(self.locator.rfid_number_field, rfidNumber)

    def click_save_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_button)
        self.se2lib.click_element(self.locator.save_button)
        
    def select_asset_type(self, asset_type):
         if asset_type != "":
            self.se2lib.wait_until_element_is_visible(self.locator.asset_type_dropdown)
            self.se2lib.click_element(self.locator.asset_type_dropdown)
            self.se2lib.wait_until_element_is_visible(self.locator.asset_type % asset_type) 
            self.se2lib.click_element(self.locator.asset_type % asset_type)
            self.se2lib.click_element(self.locator.rfid_number_field)