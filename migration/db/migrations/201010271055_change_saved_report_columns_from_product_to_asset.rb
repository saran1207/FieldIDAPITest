require "saved_report_column"

class ChangeSavedReportColumnsFromProductToAsset < ActiveRecord::Migration

  def self.up
    SavedReportColumn.update_all({ :element => "inspection_search_assettype" },  { :element => "inspection_search_producttype" })
    SavedReportColumn.update_all({ :element => "inspection_search_assetstatus" },  { :element => "inspection_search_productstatus" })
    SavedReportColumn.update_all({ :element => "inspection_search_infooption_asset_description" },  { :element => "inspection_search_infooption_product_description" })
    SavedReportColumn.update_all({ :element => "inspection_search_assettypegroup" },  { :element => "inspection_search_producttypegroup" })
  end

  def self.down
    SavedReportColumn.update_all({ :element => "inspection_search_producttype" },  { :element => "inspection_search_assettype" })
    SavedReportColumn.update_all({ :element => "inspection_search_productstatus" },  { :element => "inspection_search_assetstatus" })
    SavedReportColumn.update_all({ :element => "inspection_search_infooption_product_description" },  { :element => "inspection_search_infooption_asset_description" })
    SavedReportColumn.update_all({ :element => "inspection_search_producttypegroup" },  { :element => "inspection_search_assettypegroup" })
  end

end