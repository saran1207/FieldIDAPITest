from PageObjectLibrary import PageObject

class ManageAssetTypeGroupsPage(PageObject):
    PAGE_URL = "/fieldid/w/setup/assetTypeGroupsList"
   
    _locators = {
        "add_button": "xpath=//a[@href='./addAssetTypeGroup']",
        "toggle_up_button": "//td/div/a[@href='./editAssetTypeGroup?uniqueID=%s']/../div[@class='dropdown']/button",
        "delete_link": "//a[@href='./confirmDeleteAssetTypeGroup?uniqueID=%s']",
        "view_link": "//td/a[@href='./viewAssetTypeGroup?uniqueID=%s']",
        "edit_link": "//td/div/a[@href='./editAssetTypeGroup?uniqueID=%s']",
        "group_list":  "//tbody/tr[@class='groupList']/td[1]/a",
        "created_by": "/../../td[@class='createdBy notranslate']",
        "created_on": "/../../td[@class='createdDate notranslate']",
        "modified_by": "/../../td[@class='modifiedBy notranslate']",
        "last_modified": "/../../td[@class='modifiedDate notranslate']"
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
        self.se2lib.wait_until_element_is_visible(self.locator.edit_link % (group_id))
        self.se2lib.click_element(self.locator.edit_link % (group_id))
        
    def click_delete_asset_group_link(self, group_id):
        self.se2lib.wait_until_element_is_visible(self.locator.toggle_up_button % (group_id))
        self.se2lib.click_element(self.locator.toggle_up_button % (group_id))
        self.se2lib.wait_until_element_is_visible(self.locator.delete_link % (group_id))
        self.se2lib.click_element(self.locator.delete_link % (group_id))
        
    def click_view_asset_group_link(self, group_id):
        self.se2lib.wait_until_element_is_visible(self.locator.view_link  % (group_id))
        self.se2lib.click_element(self.locator.view_link  % (group_id))
        
    def get_asset_group_list(self):
        asset_group = self.se2lib.driver.find_elements_by_xpath(self.locator.group_list)
        asset_group_list=[]
        for item in asset_group:
            asset_group_list.append(item.text)
        return asset_group_list
    
    def get_create_by_username(self, group_id):
        self.se2lib.wait_until_element_is_visible(self.locator.view_link  % (group_id)+self.locator.created_by)
        return self.se2lib.get_text(self.locator.view_link  % (group_id)+self.locator.created_by)
    
    def get_create_on_date(self, group_id):
        self.se2lib.wait_until_element_is_visible(self.locator.view_link  % (group_id)+self.locator.created_on)
        return self.se2lib.get_text(self.locator.view_link  % (group_id)+self.locator.created_on)
    
    def get_modified_by_username(self, group_id):
        self.se2lib.wait_until_element_is_visible(self.locator.view_link  % (group_id)+self.locator.modified_by)
        return self.se2lib.get_text(self.locator.view_link  % (group_id)+self.locator.modified_by)
    
    def get_last_modified_date(self, group_id):
        self.se2lib.wait_until_element_is_visible(self.locator.view_link  % (group_id)+self.locator.last_modified)
        return self.se2lib.get_text(self.locator.view_link  % (group_id)+self.locator.last_modified)