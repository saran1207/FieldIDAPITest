from PageObjectLibrary import PageObject

class ScoringPage(PageObject):
    PAGE_URL = "/fieldid/w/setup/scoreResults"
      
    _locators = {
        
        "score_dropdown": "//select[@name='scoreCalculationType']/../div/a",
        "score_dropdown_value": "//select[@name='scoreCalculationType']/../div//ul[@class='chzn-results']/li[text()='%s']",
        "score_total_result_checkbox": "name:useScoreForResult",
        "score_section_totals_checkbox": "name:displayScoreSectionTotals",
        "score_percentage_checkbox": "name:displayScorePercentage",
        "fail_range_value1_textbox": "name:failRangePanel:value1",
        "fail_range_value2_textbox": "name:failRangePanel:enclosureContainer:value2",
        "pass_range_value1_textbox": "name:passRangePanel:value1",
        "pass_range_value2_textbox": "name:passRangePanel:enclosureContainer:value2",
        "save_scoring_button": "name:submitButton"
               
     }
    
    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
    
    def select_score_dropdown(self, score):
        if score != "":
            self.se2lib.wait_until_element_is_visible(self.locator.score_dropdown)
            self.se2lib.click_element(self.locator.score_dropdown)
            self.se2lib.driver.find_element_by_xpath(self.locator.score_dropdown_value % score).click()
   
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
        
    def check_score_total_result_checkbox(self):
        self.se2lib.wait_until_element_is_visible(self.locator.score_total_result_checkbox)
        self.se2lib.click_element(self.locator.score_total_result_checkbox)
        
    def check_score_section_totals_checkbox(self):
        self.se2lib.wait_until_element_is_visible(self.locator.score_section_totals_checkbox)
        self.se2lib.click_element(self.locator.score_section_totals_checkbox)
        
    def check_score_percentage_checkbox(self):
        self.se2lib.wait_until_element_is_visible(self.locator.score_percentage_checkbox)
        self.se2lib.click_element(self.locator.score_percentage_checkbox)
            
    def click_save_scoring_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_scoring_button)
        self.se2lib.click_element(self.locator.save_scoring_button)
 
    def verify_score_name(self, score_name):
        return self.se2lib.get_text(self.locator.score_dropdown)==score_name
    
    def verify_fail_range_value(self, fail_range_value1, fail_range_value2):
        return self.se2lib.get_value(self.locator.fail_range_value1_textbox)==fail_range_value1 and self.se2lib.get_value(self.locator.fail_range_value2_textbox)==fail_range_value2
    
    def verify_pass_range_value(self, pass_range_value1, pass_range_value2):
        return self.se2lib.get_value(self.locator.pass_range_value1_textbox)==pass_range_value1 and self.se2lib.get_value(self.locator.pass_range_value2_textbox)==pass_range_value2            