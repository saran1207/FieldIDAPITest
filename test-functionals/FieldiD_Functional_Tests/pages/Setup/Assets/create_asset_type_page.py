from PageObjectLibrary import PageObject
import os

class CreateAssetTypePage(PageObject):
    PAGE_URL = "/fieldid/w/setup/assetTypeForm"
    PAGE_EDIT_URL = "/fieldid/w/setup/assetTypeEdit"
    PAGE_COPY_URL = "/fieldid/w/setup/assetTypeCopy"
    
    
    _locators = {
        "asset_type_name_field": "xpath=//input[@name='name']",
        "save_button": "xpath=//a[@class='btn btn-green']",
        "asset_group_dropdown":  "xpath=//select[@name='group']/../div/a",
        "asset_group_list": "//ul[@class='chzn-results']",
        "asset_group": "//ul[@class='chzn-results']/li[text()='%s']",
        "delete_button": "link:Delete",
        "schedules_link": "link:Schedules",
        "event_type_associations_link": "link:Event Type Associations",
        "add_attribute_button": "//span[text()='Add Attribute']",
        "attribute_name_textbox": "//input[@name='attributes:existingAttributesContainer:existingAttributes:%s:attributeName']",
        "attribute_datatype_dropdown": "//select[@name='attributes:existingAttributesContainer:existingAttributes:%s:attributeType']/..",
        "attribute_datatype_dropdown_field": "//select[@name='attributes:existingAttributesContainer:existingAttributes:%s:attributeType']/../div/a",
        "attribute_datatype_value": "(//li[text()='%s'])[%s]",
        "select_combo_box_textbox": "//input[@name='attributes:existingAttributesContainer:existingAttributes:%s:selectOptions']",
        "select_unit_of_measure_dropdown": "//select[@name='attributes:existingAttributesContainer:existingAttributes:%s:unitOfMeasureChoice']/..",
        "unit_of_measure_value": "//li[text()='%s']",
        "more_information_link": "link:More Information",
        "allow_assset_linking_checkbox": "name: moreInfo:linkable",
        "description_template_textbox": "//legend[text()='Description Template']/../div/input",
        "attach_files": "//input[@name='moreInfo:attachments:uploadAttachmentForm:attachmentUpload']"
        
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if (not self.PAGE_URL in location) and (not self.PAGE_EDIT_URL in location)and (not self.PAGE_COPY_URL in location):
            message = "Expected location to end with " + self.PAGE_URL + " or " + self.PAGE_EDIT_URL + " or " + self.PAGE_COPY_URL + " but it did not"
            raise Exception(message)
        return True
  
    def input_asset_type_name(self, assetTypeName):
        self.se2lib.wait_until_element_is_visible(self.locator.asset_type_name_field)
        self.se2lib.input_text(self.locator.asset_type_name_field, assetTypeName)

    def click_save_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_button)
        self.se2lib.click_element(self.locator.save_button)
        
    def click_delete_asset_type_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.delete_button)
        self.se2lib.click_element(self.locator.delete_button)
        
    def click_schedules_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.schedules_link)
        self.se2lib.click_element(self.locator.schedules_link)
        
    def click_event_type_associations_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.event_type_associations_link)
        self.se2lib.click_element(self.locator.event_type_associations_link)
        
    def select_asset_group_dropdown(self, asset_group):
        if  asset_group != "":
            self.se2lib.wait_until_element_is_visible(self.locator.asset_group_dropdown)
            self.se2lib.click_element(self.locator.asset_group_dropdown)
            self.se2lib.wait_until_element_is_visible(self.locator.asset_group % asset_group) 
            self.se2lib.click_element(self.locator.asset_group % asset_group)
                    
    def get_asset_group_dropdown_list(self):
        self.se2lib.wait_until_element_is_visible(self.locator.asset_group_dropdown)
        self.se2lib.click_element(self.locator.asset_group_dropdown)
        self.se2lib.wait_until_element_is_visible(self.locator.asset_group_list)
        asset_group=self.se2lib.driver.find_element_by_xpath(self.locator.asset_group_list)
        asset_group = asset_group.find_elements_by_tag_name("li")
        asset_group_list=[]
        for item in asset_group:
              asset_group_list.append(item.text)
        return asset_group_list
            
    def click_add_attribute_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.add_attribute_button)
        self.se2lib.click_element(self.locator.add_attribute_button)
        
    def input_attribute_name(self, attributeName, row):
        row=int(row)-1
        self.se2lib.wait_until_element_is_visible(self.locator.attribute_name_textbox % row)
        self.se2lib.input_text(self.locator.attribute_name_textbox % row, attributeName)
        
    def select_attribute_datatype_dropdown(self, datatype, row):
        if  datatype != "":
            row=int(row)-1
            self.se2lib.wait_until_element_is_visible(self.locator.attribute_datatype_dropdown % row)
            self.se2lib.click_element(self.locator.attribute_datatype_dropdown % row)
            row=int(row)+1
            self.se2lib.wait_until_element_is_visible(self.locator.attribute_datatype_value % (datatype, row)) 
            self.se2lib.click_element(self.locator.attribute_datatype_value % (datatype, row))
            
    def input_select_combo_box_values(self, value, row):
        row=int(row)-1
        self.se2lib.wait_until_element_is_visible(self.locator.select_combo_box_textbox % row)
        self.se2lib.input_text(self.locator.select_combo_box_textbox % row, value)
        
    def select_unit_of_measure_dropdown(self, unit, row):
        if  unit != "":
            row=int(row)-1
            self.se2lib.wait_until_element_is_visible(self.locator.select_unit_of_measure_dropdown % row)
            self.se2lib.click_element(self.locator.select_unit_of_measure_dropdown % row)
            row=int(row)+1
            self.se2lib.wait_until_element_is_visible(self.locator.unit_of_measure_value % unit) 
            self.se2lib.click_element(self.locator.unit_of_measure_value % unit)
            
    def click_more_information_link(self):
        self.se2lib.wait_until_element_is_visible(self.locator.more_information_link)
        self.se2lib.click_element(self.locator.more_information_link)
        
    def click_allow_assset_linking_checkbox(self):
        self.se2lib.wait_until_element_is_visible(self.locator.allow_assset_linking_checkbox)
        self.se2lib.click_element(self.locator.allow_assset_linking_checkbox)
        
    def input_description_template(self, descriptionTemp):
        self.se2lib.wait_until_element_is_visible(self.locator.description_template_textbox)
        self.se2lib.input_text(self.locator.description_template_textbox, descriptionTemp)
        
          
    def verify_if_input_attribute_is_present(self, attributeName, row):
        row=int(row)-1
        return len(self.se2lib.driver.find_elements_by_xpath(self.locator.attribute_name_textbox % row))>0 and self.se2lib.get_value(self.locator.attribute_name_textbox % row)==attributeName
    
    def verify_if_select_attribute_datatype_dropdown_is_present(self, datatype, row):
        if  datatype != "":
            row=int(row)-1
            return self.se2lib.driver.find_elements_by_xpath(self.locator.attribute_datatype_dropdown % row) and self.se2lib.get_text(self.locator.attribute_datatype_dropdown_field % row)==datatype
        
    def get_selected_asset_group(self):
        return self.se2lib.get_text(self.locator.asset_group_dropdown)
    
    def get_description_template(self):
        self.se2lib.wait_until_element_is_visible(self.locator.description_template_textbox)
        return self.se2lib.get_value(self.locator.description_template_textbox)
    
    def input_file_name(self, fileName):
        self.se2lib.wait_until_element_is_visible(self.locator.attach_files)
        self.se2lib.driver.find_element_by_xpath(self.locator.attach_files).send_keys(os.getcwd()+"/test-functionals/FieldiD_Functional_Tests/Assets/"+fileName)