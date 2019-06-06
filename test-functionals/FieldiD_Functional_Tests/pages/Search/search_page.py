from PageObjectLibrary import PageObject

class SearchPage(PageObject):
    PAGE_URL = "/fieldid/w/search"

    _locators = {
        "serial_number_field": "name:filters:identifiers:containedPanel:identifier",
        "rfid_number_field": "name:filters:identifiers:containedPanel:rfidNumber",
        "ref_number_field": "name:filters:identifiers:containedPanel:referenceNumber",
        "search_button": "xpath=//a[@name='submitSearch']",
        "display_columns": "//a[@title='Select columns to display']",
        "description_column_checkbox": "//label[text()='Description']/../input"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def input_serial_number(self, serialNumber):
        if serialNumber!= "":
            self.se2lib.wait_until_element_is_visible(self.locator.serial_number_field)
            self.se2lib.input_text(self.locator.serial_number_field, serialNumber)

    def input_ref_number(self, rfidNumber):
        if rfidNumber != "":
            self.se2lib.wait_until_element_is_visible(self.locator.rfid_number_field)
            self.se2lib.input_text(self.locator.rfid_number_field, rfidNumber)
        
    def input_rfid_number(self, refNumber):
        if refNumber != "":
            self.se2lib.wait_until_element_is_visible(self.locator.ref_number_field)
            self.se2lib.input_text(self.locator.ref_number_field, refNumber)

    def click_search_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.search_button)
        self.se2lib.click_element(self.locator.search_button)
        
    def click_display_column_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.display_columns)
        self.se2lib.click_element(self.locator.display_columns)
        
    def select_description_column_checkbox(self):
        self.se2lib.wait_until_element_is_visible(self.locator.description_column_checkbox)
        self.se2lib.click_element(self.locator.description_column_checkbox)       