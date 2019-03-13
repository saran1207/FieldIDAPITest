from PageObjectLibrary import PageObject

class AddAssetTypePage(PageObject):
    PAGE_URL = "/fieldid/w/setup/assetTypeForm"

    _locators = {
        "asset_type_name_field": "xpath=//input[@name='name']",
        "save_button": "xpath=//a[@class='btn btn-green']"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def input_asset_type_name(self, assetTypeName):
        self.se2lib.wait_until_element_is_visible(self.locator.asset_type_name_field)
        self.se2lib.input_text(self.locator.asset_type_name_field, assetTypeName)

    def click_save_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_button)
        self.se2lib.click_element(self.locator.save_button)