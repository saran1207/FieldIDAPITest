from PageObjectLibrary import PageObject

class TenantEditPage(PageObject):
    PAGE_URL = "/fieldid/admin/organizationEdit.action"
   
    _locators = {
        "view_all_users_link": "link:View All Users"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

   
    def click_view_all_users(self):
        self.se2lib.wait_until_element_is_visible(self.locator.view_all_users_link)
        self.se2lib.click_element(self.locator.view_all_users_link)