from Setup.Assets.asset_type_group_base_page import AssetTypeGroupBasePage

class CreateAssetTypeGroupPage(AssetTypeGroupBasePage):
    PAGE_URL = "/fieldid/w/setup/addAssetTypeGroup"
   
    def _is_current_page(self):
        location = self.se2lib.get_location()
        if not self.PAGE_URL in location:
            message = "Expected location to end with " + self.PAGE_URL + " but it did not"
            raise Exception(message)
        return True