from PageObjectLibrary import PageObject

class ObservationsPage(PageObject):
    PAGE_URL = "/fieldid/w/setup/observationCounts"
      
    _locators = {
        
        "observation_group_dropdown": "//select[@name='observationCountGroup']/../div/a",
        "observation_group_dropdown_value": "//select[@name='observationCountGroup']/../div//ul[@class='chzn-results']/li[text()='%s']",
        "observation_count_result_checkbox": "name:useObservationCountForResult",
        "observation_section_totals_checkbox": "name:displayScoreSectionTotals",
        "observation_percentage_checkbox": "name:displayObservationPercentage",
        "observation_count_fail_dropdown": "//select[@name='observationCountFail']/../div/a",
        "observation_count_fail_dropdown_value": "//select[@name='observationCountFail']/../div//ul[@class='chzn-results']/li[text()='%s']",
        "observation_count_pass_dropdown": "//select[@name='observationCountPass']/../div/a",
        "observation_count_pass_dropdown_value": "//select[@name='observationCountPass']/../div//ul[@class='chzn-results']/li[text()='%s']",
        "fail_range_value1_textbox": "name:failRangePanel:value1",
        "fail_range_value2_textbox": "name:failRangePanel:enclosureContainer:value2",
        "pass_range_value1_textbox": "name:passRangePanel:value1",
        "pass_range_value2_textbox": "name:passRangePanel:enclosureContainer:value2",
        "save_observations_button": "link:Save"
               
     }
    
    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
    
    def select_observation_group_dropdown(self, observation_group):
        if observation_group != "":
            self.se2lib.wait_until_element_is_visible(self.locator.observation_group_dropdown)
            self.se2lib.click_element(self.locator.observation_group_dropdown)
            self.se2lib.driver.find_element_by_xpath(self.locator.observation_group_dropdown_value % observation_group).click()
            
    def select_observation_count_fail_dropdown(self, score):
        if score != "":
            self.se2lib.wait_until_element_is_visible(self.locator.observation_count_fail_dropdown)
            self.se2lib.click_element(self.locator.observation_count_fail_dropdown)
            self.se2lib.driver.find_element_by_xpath(self.locator.observation_count_fail_dropdown_value % score).click()
            
    def select_observation_count_pass_dropdown(self, score):
        if score != "":
            self.se2lib.wait_until_element_is_visible(self.locator.observation_count_pass_dropdown)
            self.se2lib.click_element(self.locator.observation_count_pass_dropdown)
            self.se2lib.driver.find_element_by_xpath(self.locator.observation_count_pass_dropdown_value % score).click()
         

    def input_fail_range_value1_textbox(self, fail_range_value1):
        self.se2lib.wait_until_element_is_visible(self.locator.fail_range_value1_textbox)
        self.se2lib.input_text(self.locator.fail_range_value1_textbox, fail_range_value1)
        
    def input_fail_range_value2_textbox(self, fail_range_value2):
        self.se2lib.wait_until_element_is_visible(self.locator.fail_range_value2_textbox)
        self.se2lib.input_text(self.locator.fail_range_value2_textbox, fail_range_value2)
        
    
    def input_pass_range_value1_textbox(self, pass_range_value1):
        self.se2lib.wait_until_element_is_visible(self.locator.pass_range_value1_textbox)
        self.se2lib.input_text(self.locator.pass_range_value1_textbox, pass_range_value1)
        
    def input_pass_range_value2_textbox(self, pass_range_value2):
        self.se2lib.wait_until_element_is_visible(self.locator.pass_range_value2_textbox)
        self.se2lib.input_text(self.locator.pass_range_value2_textbox, pass_range_value2)
        
    def check_observation_count_result_checkbox(self):
        self.se2lib.wait_until_element_is_visible(self.locator.observation_count_result_checkbox)
        self.se2lib.click_element(self.locator.observation_count_result_checkbox)
        
    def check_observation_section_totals_checkbox(self):
        self.se2lib.wait_until_element_is_visible(self.locator.observation_section_totals_checkbox)
        self.se2lib.click_element(self.locator.observation_section_totals_checkbox)
        
    def check_observation_percentage_checkbox(self):
        self.se2lib.wait_until_element_is_visible(self.locator.observation_percentage_checkbox)
        self.se2lib.click_element(self.locator.observation_percentage_checkbox)
            
    def click_save_observations_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_observations_button)
        self.se2lib.click_element(self.locator.save_observations_button)
 
    def verify_observation_group_name(self, observation_group_name):
        return self.se2lib.get_text(self.locator.observation_group_dropdown)==observation_group_name
    
    def verify_observation_count_fail(self, observation_count_fail):
        return self.se2lib.get_text(self.locator.observation_count_fail_dropdown)==observation_count_fail
    
    def verify_observation_count_pass(self, observation_count_pass):
        return self.se2lib.get_text(self.locator.observation_count_pass_dropdown)==observation_count_pass
    
    def verify_fail_range_value(self, fail_range_value1, fail_range_value2):
        return self.se2lib.get_value(self.locator.fail_range_value1_textbox)==fail_range_value1 and self.se2lib.get_value(self.locator.fail_range_value2_textbox)==fail_range_value2
    
    def verify_pass_range_value(self, pass_range_value1, pass_range_value2):
        return self.se2lib.get_value(self.locator.pass_range_value1_textbox)==pass_range_value1 and self.se2lib.get_value(self.locator.pass_range_value2_textbox)==pass_range_value2            