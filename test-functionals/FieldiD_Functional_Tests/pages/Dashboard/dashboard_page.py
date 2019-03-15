from PageObjectLibrary import PageObject
#from Menu.common_menu_bar import CommonMenuBar

class DashboardPage(PageObject):
    PAGE_URL = "/fieldid/w/dashboard"

    _locators = {
                
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
           