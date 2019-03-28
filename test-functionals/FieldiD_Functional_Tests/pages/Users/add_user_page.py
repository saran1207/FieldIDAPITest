from PageObjectLibrary import PageObject

class AddUserPage(PageObject):
    PAGE_URL = "/fieldid/w/setup/addUser"

    _locators = {
        "user_name_field": "name:accountPanel:username",
        "password_field": "name:accountPanel:newAccountFields:password",
        "verify_password_field": "name:accountPanel:newAccountFields:confirmPassword",
        "owner_field": "name:identifiersPanel:ownerPicker:text",
        "n4_owner_option":"link:N4 Systems Inc.",
        "email_field": "name:identifiersPanel:email",
        "first_name_field": "name:identifiersPanel:firstname",
        "last_name_field": "name:identifiersPanel:lastname",
        "all_on_button": "xpath=//input[@value='All On']",
        "save_button": "link:Save"
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def input_user_name_field(self, username):
        self.se2lib.wait_until_element_is_visible(self.locator.user_name_field)
        self.se2lib.input_text(self.locator.user_name_field, username)

    def input_password_field(self, password):
        self.se2lib.wait_until_element_is_visible(self.locator.password_field)
        self.se2lib.input_text(self.locator.password_field, password)
        
    def input_verify_password_field(self, verify_password):
        self.se2lib.wait_until_element_is_visible(self.locator.verify_password_field)
        self.se2lib.input_text(self.locator.verify_password_field, verify_password)
        
    def input_owner_field(self, owner):
        self.se2lib.wait_until_element_is_visible(self.locator.owner_field)
        self.se2lib.input_text(self.locator.owner_field, owner)
    
    def select_n4_owner_option(self):
        self.se2lib.wait_until_element_is_visible(self.locator.n4_owner_option)
        self.se2lib.click_element(self.locator.n4_owner_option)
            
    def input_email_field(self, email):
        self.se2lib.wait_until_element_is_visible(self.locator.email_field)
        self.se2lib.input_text(self.locator.email_field, email)
        
    def input_first_name_field(self, first_name):
        self.se2lib.wait_until_element_is_visible(self.locator.first_name_field)
        self.se2lib.input_text(self.locator.first_name_field, first_name)
        
    def input_last_name_field(self, last_name):
        self.se2lib.wait_until_element_is_visible(self.locator.last_name_field)
        self.se2lib.input_text(self.locator.last_name_field, last_name)
        
    def click_all_on_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.all_on_button)
        self.se2lib.click_element(self.locator.all_on_button)
        
    def click_save_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_button)
        self.se2lib.click_element(self.locator.save_button)