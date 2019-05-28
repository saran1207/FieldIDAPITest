from PageObjectLibrary import PageObject

class CreateAssetPage(PageObject):
    PAGE_URL = "/fieldid/w/identify"

    _locators = {
        "serial_number_field": "//input[@name='singleIdentifyContainer:identifierContainer:identifier']",
        "rfid_number_field": "//input[@name='singleIdentifyContainer:rfidContainer:rfidNumber']",
        "save_button": "//input[@name='actionsContainer:saveButton']",
        "asset_type_dropdown": "(//a[@class='chzn-single'])[1]",
        "asset_type": "(//ul[@class='chzn-results'])[1]/li[text()='%s']",
        "owner_field": "name:owner:text",
        "owner_option": "//ins[@class='jstree-icon']/../a[text()='%s']",
        "attribute_select_box": "//select[@name='attributesPanel:attributes:%s:attributeEditor:select']",
        "attribute_select_box_value": "//select[@name='attributesPanel:attributes:%s:attributeEditor:select']/option[text()='%s']",
        "attribute_text_field": "//input[@name='attributesPanel:attributes:%s:attributeEditor:text']",
        "attribute_combo_box": "//select[@name='attributesPanel:attributes:%s:attributeEditor:combo']",
        "attribute_combo_box_value": "//select[@name='attributesPanel:attributes:%s:attributeEditor:combo']/option[text()='%s']",
        "attribute_date_field": "//input[@name='attributesPanel:attributes:%s:attributeEditor:datePicker:dateField']",
        "unit_of_measure_image": "//img[@src='/fieldid/images/unit_of_measure.png']",
        "attribute_unit_of_measure": "//input[@name='attributesPanel:attributes:%s:attributeEditor:editorContainer:primaryValue']",
        "store_unit_of_measure_link": "link: Store",
        "identify_assets_label": "//div[@class='header']/h1/span"
        
    }

    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True

    def input_asset_serial_number(self, identifier):
        self.se2lib.wait_until_element_is_visible(self.locator.serial_number_field)
        self.se2lib.input_text(self.locator.serial_number_field, identifier)

    def input_rfid_number(self, rfidNumber):
        if rfidNumber != "":
            self.se2lib.wait_until_element_is_visible(self.locator.rfid_number_field)
            self.se2lib.input_text(self.locator.rfid_number_field, rfidNumber)
            
    def input_owner_field(self, owner):
         if owner != "":
             self.se2lib.wait_until_element_is_visible(self.locator.owner_field)
             self.se2lib.input_text(self.locator.owner_field, owner)
             self.se2lib.wait_until_element_is_visible(self.locator.owner_option % owner)
             self.se2lib.click_element(self.locator.owner_option % owner)

    def click_save_button(self):
        self.se2lib.wait_until_element_is_visible(self.locator.save_button)
        self.se2lib.click_element(self.locator.save_button)
        
    def select_asset_type(self, asset_type):
         if asset_type != "":
            self.se2lib.wait_until_element_is_visible(self.locator.asset_type_dropdown)
            self.se2lib.click_element(self.locator.asset_type_dropdown)
            self.se2lib.wait_until_element_is_visible(self.locator.asset_type % asset_type) 
            self.se2lib.click_element(self.locator.asset_type % asset_type)
            self.se2lib.click_element(self.locator.identify_assets_label)
    
    def input_text_field_attribute(self, textAttribute, row):
        if textAttribute != "":
            row=int(row)-1
            self.se2lib.wait_until_element_is_visible(self.locator.attribute_text_field % row )
            self.se2lib.input_text(self.locator.attribute_text_field % row, textAttribute)
            
    def input_date_field_attribute(self, dateAttribute, row):
        if dateAttribute != "":
            row=int(row)-1
            self.se2lib.wait_until_element_is_visible(self.locator.attribute_date_field % row )
            self.se2lib.input_text(self.locator.attribute_date_field % row, dateAttribute)
            
    def select_select_box_value(self, value, row):
         if value != "":
             row=int(row)-1
             self.se2lib.wait_until_element_is_visible(self.locator.attribute_select_box % row)
             self.se2lib.click_element(self.locator.attribute_select_box % row)
             self.se2lib.wait_until_element_is_visible(self.locator.attribute_select_box_value % (row,value))
             self.se2lib.click_element(self.locator.attribute_select_box_value % (row,value) )
                        
    def select_combo_box_value(self, value, row):
         if value != "":
            row=int(row)-1
            self.se2lib.wait_until_element_is_visible(self.locator.attribute_combo_box % row)
            self.se2lib.click_element(self.locator.attribute_combo_box % row)
            self.se2lib.wait_until_element_is_visible(self.locator.attribute_combo_box_value % (row,value))
            self.se2lib.click_element(self.locator.attribute_combo_box_value % (row,value) )
                       
    def input_unit_of_measure(self, value, row):
         if value != "":
             row=int(row)-1
             self.se2lib.wait_until_element_is_visible(self.locator.unit_of_measure_image)
             self.se2lib.click_element(self.locator.unit_of_measure_image)
             self.se2lib.wait_until_element_is_not_visible(self.locator.unit_of_measure_image)
             self.se2lib.wait_until_element_is_visible(self.locator.attribute_unit_of_measure % row )
             self.se2lib.input_text(self.locator.attribute_unit_of_measure % row, value)
             self.se2lib.wait_until_element_is_visible(self.locator.store_unit_of_measure_link)
             self.se2lib.click_element(self.locator.store_unit_of_measure_link)
            