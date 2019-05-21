from PageObjectLibrary import PageObject

class CreateAssetTypePage(PageObject):
    PAGE_ADD_URL = "/fieldid/w/setup/assetTypeForm"
    PAGE_EDIT_URL = "/fieldid/w/setup/assetTypeEdit"
    
    
    _locators = {
        "asset_type_name_field": "xpath=//input[@name='name']",
        "save_button": "xpath=//a[@class='btn btn-green']",
        "asset_group_dropdown":  "xpath=//select[@name='group']/../div/a",
        "asset_group_list": "//ul[@class='chzn-results']",
        "asset_group": "//ul[@class='chzn-results']/li[text()='%s']",
        "delete_button": "link:Delete"
        
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if (not self.PAGE_ADD_URL in location) and (not self.PAGE_EDIT_URL in location):
            message = "Expected location to end with " + self.PAGE_ADD_URL + " but it did not"
            raise Exception(message)
        return True

    def input_asset_type_name(self, assetTypeName):
        self.se2lib.wait_until_element_is_visible(self.locator.asset_type_name_field)
        self.se2lib.input_text(self.locator.asset_type_name_field, assetTypeName)

    def click_save_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_button)
        self.se2lib.click_element(self.locator.save_button)
        
    def click_delete_asset_type_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.delete_button)
        self.se2lib.click_element(self.locator.delete_button)
        
    def select_asset_group_dropdown(self, asset_group):
        if  asset_group != "":
            self.se2lib.wait_until_element_is_visible(self.locator.asset_group_dropdown)
            self.se2lib.click_element(self.locator.asset_group_dropdown)
            self.se2lib.wait_until_element_is_visible(self.locator.asset_group % asset_group) 
            self.se2lib.click_element(self.locator.asset_group % asset_group)
                    
    def get_asset_group_dropdown_list(self):
        self.se2lib.wait_until_element_is_visible(self.locator.asset_group_dropdown)
        self.se2lib.click_element(self.locator.asset_group_dropdown)
        self.se2lib.wait_until_element_is_visible(self.locator.asset_group_list)
        asset_group=self.se2lib.driver.find_element_by_xpath(self.locator.asset_group_list)
        asset_group = asset_group.find_elements_by_tag_name("li")
        asset_group_list=[]
        for item in asset_group:
              asset_group_list.append(item.text)
        return asset_group_list