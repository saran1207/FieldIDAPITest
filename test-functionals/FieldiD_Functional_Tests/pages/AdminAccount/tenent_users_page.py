from PageObjectLibrary import PageObject

class TenantUserPage(PageObject):
    PAGE_URL = "/fieldid/admin/tenantUsers"
    sudo_any_user = "Sudo"
   
    _locators = {
        "sudo_any_user": "link:Sudo",
        "login_button":  "link:Login"
     }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def click_sudo_any_user(self):
        if  len(self.se2lib.driver.find_elements_by_link_text(self.sudo_any_user))>0:
                self.se2lib.click_element(self.locator.sudo_any_user)
               
    def click_login(self):
        self.se2lib.wait_until_element_is_visible(self.locator.login_button)
        self.se2lib.click_element(self.locator.login_button)