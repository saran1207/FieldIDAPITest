from PageObjectLibrary import PageObject

class CommonMenuBar(PageObject):
    #PAGE_URL = "/fieldid/w/identify"

    _locators = {
        "new_asset": "xpath=//a[@href='./assetImport?initialTab=addWithOrder']",
        "set_up": "link:Setup",
        "asset_and_events": "link:Assets & Events",
        "asset_type_groups": "link:Asset Type Groups",
        "owners_users_locations":  "link:Owners, Users & Locations",
        "users":  "link:Users"
    
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def click_new_asset(self):
        self.se2lib.wait_until_element_is_visible(self.locator.new_asset)
        self.se2lib.click_element(self.locator.new_asset)
        
    def click_asset_type_groups(self):
        self.se2lib.wait_until_element_is_visible(self.locator.set_up)
        self.se2lib.mouse_over(self.locator.set_up)
        self.se2lib.wait_until_element_is_visible(self.locator.asset_and_events)
        self.se2lib.mouse_over(self.locator.asset_and_events)
        self.se2lib.wait_until_element_is_visible(self.locator.asset_type_groups)
        self.se2lib.click_element(self.locator.asset_type_groups)
        
    def click_users(self):
        self.se2lib.wait_until_element_is_visible(self.locator.set_up)
        self.se2lib.mouse_over(self.locator.set_up)
        self.se2lib.wait_until_element_is_visible(self.locator.owners_users_locations)
        self.se2lib.mouse_over(self.locator.owners_users_locations)
        self.se2lib.wait_until_element_is_visible(self.locator.users)
        self.se2lib.click_element(self.locator.users)
        