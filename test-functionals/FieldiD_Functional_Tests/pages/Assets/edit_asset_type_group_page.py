from PageObjectLibrary import PageObject

class EditAssetTypeGroupPage(PageObject):
    NAME_REQUIRED_ERROR_MSG = "Field 'name' is required."
    UNIQUE_NAME_ERROR_MSG = "Asset Type Group name has already been used. Please choose another."

    _locators = {
        "view_all_button": "xpath=//a[@href='./assetTypeGroupsList']",
        "save_button": "xpath=//button[@type='submit']",
        "cancel_button": "xpath=//a[@href='./assetTypeGroupsList']",
        "asset_type_group_name_field": "xpath=//input[@name='name']"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
    
    def set_page_url(self, groupid):
        self.PAGE_URL = "&uniqueID=" + str(groupid)

    def click_view_all_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.view_all_button)
        self.se2lib.click_element(self.locator.view_all_button)

    def click_save_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_button)
        self.se2lib.click_element(self.locator.save_button)

    def click_cancel_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.cancel_button)
        self.se2lib.click_element(self.locator.cancel_button)

    def input_asset_type_group_name(self, assetTypeGroupName):
        self.se2lib.wait_until_element_is_visible(self.locator.asset_type_group_name_field)
        self.se2lib.input_text(self.locator.asset_type_group_name_field, assetTypeGroupName)
        
    def get_name_required_error_msg(self):
        return self.NAME_REQUIRED_ERROR_MSG
    
    def get_unique_name_error_msg(self):
        return self.UNIQUE_NAME_ERROR_MSG