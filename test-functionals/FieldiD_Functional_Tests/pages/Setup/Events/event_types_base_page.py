from PageObjectLibrary import PageObject

class EventTypesBasePage(PageObject):
      
    _locators = {
        "view_all_button": "link:View All",
        "view_link": "link:View",
        "edit_link": "link:Edit",
        "event_form_link": "link:Event Form",
        "observations_link": "link:Observations",
        "scoring_link": "link:Scoring",
        "rules_link": "link:Rules",
        "asset_type_associations": "//a/span[text()= 'Asset Type Associations']"
    }

    def click_view_all_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.view_all_button)
        self.se2lib.click_element(self.locator.view_all_button)
        
    def click_asset_type_association_tab(self):
        self.se2lib.wait_until_element_is_visible(self.locator.asset_type_associations)
        self.se2lib.click_element(self.locator.asset_type_associations)
        
    def click_observations_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.observations_link)
        self.se2lib.click_element(self.locator.observations_link)
        
    def click_scoring_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.scoring_link)
        self.se2lib.click_element(self.locator.scoring_link)