from PageObjectLibrary import PageObject

class ManageAssetTypeGroupsPage(PageObject):
    PAGE_URL = "/fieldid/w/setup/assetTypeGroupsList"
    TOGGLE_UP_BUTTON="//td/div/a[@href='./editAssetTypeGroup?uniqueID=%s']/../div[@class='dropdown']/button"
    DELETE_LINK="xpath=//a[@href='./confirmDeleteAssetTypeGroup?uniqueID=%s']"
    VIEW_LINK="xpath=//td/a[@href='./viewAssetTypeGroup?uniqueID=%s']"
    EDIT_LINK= "xpath=//td/div/a[@href='./editAssetTypeGroup?uniqueID=%s']"

    _locators = {
        "add_button": "xpath=//a[@href='./addAssetTypeGroup']",
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
        link = self.EDIT_LINK % (group_id) 
        self.se2lib.wait_until_element_is_visible(link)
        self.se2lib.click_element(link)
        
    def click_delete_asset_group_link(self, group_id):
        toggle_up_button=self.TOGGLE_UP_BUTTON % (group_id)
        self.se2lib.wait_until_element_is_visible(toggle_up_button)
        self.se2lib.click_element(toggle_up_button)
        delete_link = self.DELETE_LINK % (group_id)
        self.se2lib.wait_until_element_is_visible(delete_link)
        self.se2lib.click_element(delete_link)