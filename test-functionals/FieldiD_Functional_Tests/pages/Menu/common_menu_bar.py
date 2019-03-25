from PageObjectLibrary import PageObject

class CommonMenuBar(PageObject):
    
    _locators = {
        "new_asset_link": "link:New Asset",
        "set_up_link": "link:Setup",
        "asset_and_events_link": "link:Assets & Events",
        "asset_type_groups_link": "link:Asset Type Groups",
        "owners_users_locations_link":  "link:Owners, Users & Locations",
        "users_link":  "link:Users",
        "sign_out_link": "link:Sign Out"
    }

    def click_new_asset(self):
        self.se2lib.wait_until_element_is_visible(self.locator.new_asset_link)
        self.se2lib.click_element(self.locator.new_asset_link)
        
    def click_asset_type_groups(self):
        self.se2lib.wait_until_element_is_visible(self.locator.set_up_link)
        self.se2lib.mouse_over(self.locator.set_up_link)
        self.se2lib.wait_until_element_is_visible(self.locator.asset_and_events_link)
        self.se2lib.mouse_over(self.locator.asset_and_events_link)
        self.se2lib.wait_until_element_is_visible(self.locator.asset_type_groups_link)
        self.se2lib.click_element(self.locator.asset_type_groups_link)
        
    def click_users(self):
        self.se2lib.wait_until_element_is_visible(self.locator.set_up)
        self.se2lib.mouse_over(self.locator.set_up)
        self.se2lib.wait_until_element_is_visible(self.locator.owners_users_locations_link)
        self.se2lib.mouse_over(self.locator.owners_users_locations_link)
        self.se2lib.wait_until_element_is_visible(self.locator.owners_users_locations_link)
        self.se2lib.click_element(self.locator.users_link)
        
    def click_sign_out(self):
        self.se2lib.wait_until_element_is_visible(self.locator.sign_out_link)
        self.se2lib.click_element(self.locator.sign_out_link) 