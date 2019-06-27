from PageObjectLibrary import PageObject

class ManageEventTypesPage(PageObject):
    PAGE_URL = "/fieldid/w/setup/eventTypeList"

    _locators = {
        "add_event_type_button": "link:Add Event Type",
        "name_filter_field": "name:nameFilter",
        "filter_button": "//input[@value='Filter']",
        "click_or_add_event_type_link": "link:%s",
        "edit_event_type": "//td/a[@href='./eventTypeForm?uniqueID=%s']",
        "copy_event_type": "//td/a[@href='./eventTypeForm?uniqueID=%s']/../a[text()='Copy']"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
    
    def get_link_from_name(self, name):
        return "link: " + name

    def click_add_event_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.add_event_type_button)
        self.se2lib.click_element(self.locator.add_event_type_button)
        
    def input_filter_name(self, eventTypeName):
        self.se2lib.wait_until_element_is_visible(self.locator.name_filter_field)
        self.se2lib.input_text(self.locator.name_filter_field, eventTypeName)
        
    def click_filter_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.filter_button)
        self.se2lib.click_element(self.locator.filter_button)
        
    def click_or_add_event_type_link(self, eventTypeName):
        self.se2lib.wait_until_element_is_visible(self.locator.click_or_add_event_type_link % eventTypeName)
        self.se2lib.click_element(self.locator.click_or_add_event_type_link % eventTypeName)
        
    def click_edit_event_type_link(self, event_type_id):
        self.se2lib.wait_until_element_is_visible(self.locator.edit_event_type % (event_type_id))
        self.se2lib.click_element(self.locator.edit_event_type % (event_type_id))
        
    def click_copy_event_type_link(self, event_type_id):
        self.se2lib.wait_until_element_is_visible(self.locator.copy_event_type % (event_type_id))
        self.se2lib.click_element(self.locator.copy_event_type % (event_type_id))