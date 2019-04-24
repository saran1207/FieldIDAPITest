from PageObjectLibrary import PageObject

class PerformEventPage(PageObject):
    PAGE_URL_PERFORM = "/fieldid/w/performEvent"
    PAGE_URL_EDIT = "/fieldid/w/editEvent"
    

    _locators = {
        "unscheduled_event_link": "link:%s",
        "event_result_option": "//ul[@class='chzn-results']/li[text()='%s']",
        "event_result_dropdown": "//select[@name='eventResult']/../div",
        "comments_field": "name:comments:commentText",
        "owner_field": "name:ownerSection:orgPicker:text",
        "owner_option": "link:%s",
        "save_button": "name:saveButton",
        "score_radio_button": "//div[@class='scoreEditContainer']/span/span[text()=%s]/../input"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if (not self.PAGE_URL_PERFORM in location) and (not self.PAGE_URL_EDIT in location):
            message = "Expected location to end with " + self.PAGE_URL_PERFORM + " or " + self.PAGE_URL_EDIT + " but it did not"
            raise Exception(message)
        return True

    def select_event_result(self, event_result):
        self.se2lib.wait_until_element_is_visible(self.locator.event_result_dropdown)
        self.se2lib.click_element(self.locator.event_result_dropdown)
        self.se2lib.wait_until_element_is_visible(self.locator.event_result_option % event_result)
        self.se2lib.click_element(self.locator.event_result_option % event_result)
        
    def input_comments(self, comments):
        self.se2lib.wait_until_element_is_visible(self.locator.comments_field)
        self.se2lib.input_text(self.locator.comments_field, comments)
        
    def input_owner_field(self, owner):
        self.se2lib.wait_until_element_is_visible(self.locator.owner_field)
        self.se2lib.input_text(self.locator.owner_field, owner)
        self.se2lib.wait_until_element_is_visible(self.locator.owner_option % owner)
        self.se2lib.click_element(self.locator.owner_option % owner)
        
    def click_save_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_button)
        self.se2lib.click_element(self.locator.save_button)
        
    def select_score(self, score_value):
        self.se2lib.wait_until_element_is_visible(self.locator.score_radio_button % score_value)
        self.se2lib.click_element(self.locator.score_radio_button % score_value)
        