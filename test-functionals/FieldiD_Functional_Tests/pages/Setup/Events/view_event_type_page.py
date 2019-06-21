from Setup.Events.event_types_base_page import EventTypesBasePage

class ViewEventTypePage(EventTypesBasePage):
    PAGE_URL = "/fieldid/w/setup/eventTypeView"
      
    _locators = EventTypesBasePage._locators.copy()
    _locators.update({
        
        
     })
    
    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True