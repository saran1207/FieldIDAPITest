from PageObjectLibrary import PageObject

class CreateAssetTypePage(PageObject):
    PAGE_URL = "/fieldid/w/setup/assetTypeForm"
    ASSET_GROUP_LIST = "//ul[@class='chzn-results']"

    _locators = {
        "asset_type_name_field": "xpath=//input[@name='name']",
        "save_button": "xpath=//a[@class='btn btn-green']",
        "asset_group_dropdown":  "xpath=//select[@name='group']/../div/a",
        "asset_group_list": "xpath=//ul[@class='chzn-results']" 
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
        
    def select_asset_group_dropdown(self, asset_group):
        if  asset_group != "":
            self.se2lib.wait_until_element_is_visible(self.locator.asset_group_dropdown)
            self.se2lib.click_element(self.locator.asset_group_dropdown)
            self.se2lib.wait_until_element_is_visible(self.locator.asset_group_list)
            asset_group_list = self.se2lib.driver.find_element_by_xpath(self.ASSET_GROUP_LIST)
            items = asset_group_list.find_elements_by_tag_name("li")
            for item in items:
                print item.text
                if  asset_group == item.text:
                    item.click()
                    
    def get_asset_group_list(self):
        self.se2lib.wait_until_element_is_visible(self.locator.asset_group_dropdown)
        self.se2lib.click_element(self.locator.asset_group_dropdown)
        self.se2lib.wait_until_element_is_visible(self.locator.asset_group_list)
        asset_group=self.se2lib.driver.find_element_by_xpath(self.ASSET_GROUP_LIST)
        asset_group = asset_group.find_elements_by_tag_name("li")
        asset_group_list=[0]
        for item in asset_group:
              asset_group_list.append(item.text)
        return asset_group_list