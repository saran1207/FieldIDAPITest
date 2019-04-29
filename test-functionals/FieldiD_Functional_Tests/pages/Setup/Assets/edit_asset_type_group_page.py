from Setup.Assets.asset_type_group_base_page import AssetTypeGroupBasePage

class EditAssetTypeGroupPage(AssetTypeGroupBasePage):
   
    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True
    
    def set_page_url(self, groupid):
        self.PAGE_URL = "&uniqueID=" + str(groupid)