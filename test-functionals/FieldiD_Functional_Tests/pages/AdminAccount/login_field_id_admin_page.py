from PageObjectLibrary import PageObject

class LoginFieldidAdminPage(PageObject):
    PAGE_URL = "/fieldid/admin/signIn.action"
    INVALID_LOGIN_MSG = "Your login information is incorrect. "

    _locators = {
        "username_field": "id=loginForm_username",
        "password_field": "id=loginForm_password",
        "login_button": "id=loginForm_0"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def input_username(self, username):
        self.se2lib.wait_until_element_is_visible(self.locator.username_field)
        self.se2lib.input_text(self.locator.username_field, username)

    def return_invalid_login_message(self):
        return self.INVALID_LOGIN_MSG
           
    def input_password(self, password):
        self.se2lib.wait_until_element_is_visible(self.locator.password_field)
        self.se2lib.input_text(self.locator.password_field, password)

    def submit_credentials(self):
        self.se2lib.wait_until_element_is_visible(self.locator.login_button)
        self.se2lib.click_element(self.locator.login_button)