from PageObjectLibrary import PageObject

class DeleteAssetTypeGroupPage(PageObject):
    NAME_REQUIRED_ERROR_MSG = "Field 'name' is required."
    UNIQUE_NAME_ERROR_MSG = "Asset Type Group name has already been used. Please choose another."

    _locators = {
        "view_all_tab": "link:View All",
        "delete_button": "xpath=//button[@name='deleteButton']",
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
        
        
    def click_delete_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.delete_button)
        self.se2lib.click_element(self.locator.delete_button)