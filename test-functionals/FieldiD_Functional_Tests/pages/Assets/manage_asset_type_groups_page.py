from PageObjectLibrary import PageObject

class ManageAssetTypeGroupsPage(PageObject):
    PAGE_URL = "/fieldid/w/setup/assetTypeGroupsList"

    _locators = {
        "add_button": "xpath=//a[@href='./addAssetTypeGroup']",
        "toggle_up_button": "xpath=//a[@href='./addAssetTypeGroup']"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def click_add_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.add_button)
        self.se2lib.click_element(self.locator.add_button)
        
    def get_link_from_name(self, name):
        return "link: " + name
    
    def click_edit_asset_group_link(self, group_id):
        link = "xpath=//td/div/a[@href='./editAssetTypeGroup?uniqueID=" + (group_id) + "']"
        self.se2lib.wait_until_element_is_visible(link)
        self.se2lib.click_element(link)
        
    def click_delete_asset_group_link(self, group_id):
        link = "xpath=//td/div/a[@href='./confirmDeleteAssetTypeGroup?uniqueID=" + (group_id) + "']"
        toggle_up_button= link+"/div[@class='dropdown']/button"
        self.se2lib.wait_until_element_is_visible(toggle_up_button)
        self.se2lib.mouse_over(link)
        self.se2lib.wait_until_element_is_visible(link)
        self.se2lib.click_element(link)
    