from PageObjectLibrary import PageObject

class SelectUserTypePage(PageObject):
    PAGE_URL = "/fieldid/w/setup/selectUserType"

    _locators = {
        "add_administration_user": "link:Add Administration User"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def click_add_administration_userbutton(self):
        self.se2lib.wait_until_element_is_visible(self.locator.add_administration_user)
        self.se2lib.click_element(self.locator.add_administration_user)