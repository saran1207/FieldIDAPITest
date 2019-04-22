from PageObjectLibrary import PageObject

class ThingEventSummaryPage(PageObject):
    PAGE_URL = "/fieldid/w/thingEventSummary"

    _locators = {
        "print_link": "link:Print",
        "edit_link": "link:Edit"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
    
    

   