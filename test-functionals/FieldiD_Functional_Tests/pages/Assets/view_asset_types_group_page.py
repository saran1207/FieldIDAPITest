from PageObjectLibrary import PageObject

class ViewAssetTypeGroupPage(PageObject):
    PAGE_BASE_URL = "&uniqueID="
    PAGE_URL= "fieldid/w/setup/viewAssetTypeGroup?"
    
    _locators = {
        "add_asset_type": "link:Add a new Asset Type to this group."
     }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        
        if not self.PAGE_BASE_URL in location:
            message = "Expected location to end with " + self.PAGE_BASE_URL + " but it did not"
            raise Exception(message)
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
    
    def set_page_url(self, groupid):
        self.PAGE_BASE_URL = "&uniqueID=" + str(groupid)
                
    def click_add_asset_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.add_asset_type)
        self.se2lib.click_element(self.locator.add_asset_type)