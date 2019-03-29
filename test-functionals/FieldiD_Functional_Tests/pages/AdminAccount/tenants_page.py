from PageObjectLibrary import PageObject

class TenantPage(PageObject):
    PAGE_URL = "/fieldid/admin/organizations.action"
   
    _locators = {
        "n4tenant_link": "link:N4 Systems Inc."
     }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
   
    def click_n4tenant(self):
        self.se2lib.wait_until_element_is_visible(self.locator.n4tenant_link)
        self.se2lib.click_element(self.locator.n4tenant_link)