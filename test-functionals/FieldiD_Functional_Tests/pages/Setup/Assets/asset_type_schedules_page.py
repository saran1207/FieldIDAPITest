from PageObjectLibrary import PageObject

class AssetTypeSchedulesPage(PageObject):
    PAGE_URL = "/fieldid/w/setup/assetTypeSchedules"
    
    _locators = {
         "create_new_rule_button": "link:Create New Rule",
        "event_triggered_link": "link:Event Triggered",
        "Recurring_link": "link:Recurring",
        "event_type_dropdown":  "//select[@name='eventType']/../div/a",
        "recurring_event_type_dropdown": "//select[@name='createContainer:eventType']/../div/a",
        "event_type": "//ul[@class='chzn-results']/li[text()='%s']",
        "frequency_days_field": "//input[@name='frequencyInDays']",
        "create_event_frequency_button": "link:Create Event Frequency",
        "create_recurring_event_button": "link:Create Recurring Event",
        "remove_schedule": "//a[text()='Remove']",
        "remove_schedule_list": "(//a[text()='Remove'])[%s]"
        
    
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def input_frequency_in_days(self, numOfDays):
        self.se2lib.wait_until_element_is_visible(self.locator.frequency_days_field)
        self.se2lib.input_text(self.locator.frequency_days_field, numOfDays)

    def click_create_new_rule_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.create_new_rule_button)
        self.se2lib.click_element(self.locator.create_new_rule_button)
        
    def click_event_triggered_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.event_triggered_link)
        self.se2lib.click_element(self.locator.event_triggered_link)
        
    def click_recurring_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.Recurring_link)
        self.se2lib.click_element(self.locator.Recurring_link)
        
    def click_create_event_frequency_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.create_event_frequency_button)
        self.se2lib.click_element(self.locator.create_event_frequency_button) 
        self.se2lib.wait_until_element_is_not_visible(self.locator.create_event_frequency_button)
        
    def click_create_recurring_event_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.create_recurring_event_button)
        self.se2lib.click_element(self.locator.create_recurring_event_button) 
        self.se2lib.wait_until_element_is_not_visible(self.locator.create_recurring_event_button)  
        
    def click_remove_schedule_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.remove_schedule)
        self.se2lib.click_element(self.locator.remove_schedule) 
        
    def select_recurring_event_type_dropdown(self, event_type):
        if  event_type != "":
            self.se2lib.wait_until_element_is_visible(self.locator.recurring_event_type_dropdown)
            self.se2lib.click_element(self.locator.recurring_event_type_dropdown)
            self.se2lib.wait_until_element_is_visible(self.locator.event_type % event_type) 
            self.se2lib.click_element(self.locator.event_type % event_type)
            
    def click_remove_schedule_link_till_present(self):
        remove_list = len(self.se2lib.driver.find_elements_by_xpath(self.locator.remove_schedule))
        while remove_list > 0:
            self.se2lib.wait_until_element_is_visible(self.locator.remove_schedule_list % remove_list)
            self.se2lib.click_element(self.locator.remove_schedule_list % remove_list)
            self.se2lib.wait_until_element_is_not_visible(self.locator.remove_schedule_list % remove_list)
            remove_list = remove_list - 1   