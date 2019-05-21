from PageObjectLibrary import PageObject

class DeleteAssetTypePage(PageObject):
    PAGE_URL = "assetTypeDelete"
    
    _locators = {
        "asset_types_details": "xpath=//ul[@class='list-table']/li[@class='infoSet'][1]",
        "type_delete_field": "//input[@name='confirmationField']",
        "delete_button": "//input[@value='Delete']"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
              
    def click_delete_asset_type_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.delete_button)
        self.se2lib.wait_until_element_is_enabled(self.locator.delete_button)
        self.se2lib.click_element(self.locator.delete_button)
      
    def input_type_delete(self, deleteText):
        self.se2lib.wait_until_element_is_visible(self.locator.type_delete_field)
        self.se2lib.input_text(self.locator.type_delete_field, deleteText)