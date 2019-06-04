from PageObjectLibrary import PageObject

class AssetSummaryPage(PageObject):
    PAGE_URL = "/fieldid/w/assetSummary"

    _locators = {
        "events_button": "name:eventHistoryLink",
        "start_event_link": "name:startEventLink",
        "summary_button":  "name:summaryLink",
        "schedules_list": "//div[@name='upcomingEventsList'][%s]",
        "schedules_event_start": "//div[@name='upcomingEventsList'][%s]//div[@class='_defaultActionButtonContainer']/a[1]",
        "asset_id_label": "name:assetId",
        "link_new_asset_link": "link:Link New Asset",
        "link_asset_textbox": "id:linkedAssetAutoComplete",
        "link_assetname": "link:%s",
        "add_linked_asset_button": "//a[@class='mattButton addLinkedAssetButton']"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def click_events_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.events_button)
        self.se2lib.click_element(self.locator.events_button)
        
    def click_start_event_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.start_event_link)
        self.se2lib.click_element(self.locator.start_event_link)
        
    def wait_for_summary_button(self):
         self.se2lib.wait_until_element_is_visible(self.locator.summary_button)
         
    def get_schedule(self, listNum):
        self.se2lib.wait_until_element_is_visible(self.locator.schedules_list % listNum)
        return self.se2lib.get_text(self.locator.schedules_list % listNum)
    
    def get_assetId(self):
        self.se2lib.wait_until_element_is_visible(self.locator.asset_id_label)
        return self.se2lib.get_text(self.locator.asset_id_label)
    
    def start_scheduled_event(self, listNum):
        self.se2lib.wait_until_element_is_visible(self.locator.schedules_event_start % listNum)
        self.se2lib.click_element(self.locator.schedules_event_start % listNum)
        
    def click_link_new_asset_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.link_new_asset_link)
        self.se2lib.click_element(self.locator.link_new_asset_link)
        
    def input_and_select_linked_asset_name(self, assetName):
        self.se2lib.wait_until_element_is_visible(self.locator.link_asset_textbox)
        self.se2lib.input_text(self.locator.link_asset_textbox, assetName)
        self.se2lib.wait_until_element_is_visible(self.locator.link_assetname % assetName)
        self.se2lib.click_element(self.locator.link_assetname % assetName)
        self.se2lib.wait_until_element_is_not_visible(self.locator.link_assetname % assetName)
        
    def click_add_linked_asset_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.add_linked_asset_button)
        self.se2lib.click_element(self.locator.add_linked_asset_button)
        self.se2lib.wait_until_element_is_not_visible(self.locator.add_linked_asset_button)
    