require "saved_report_criteria"

class ChangeSavedReportCriteriaFromProductToAsset < ActiveRecord::Migration

  def self.up
    SavedReportCriteria.update_all({ :mapkey => "assetType" },  { :mapkey => "productType" })
    SavedReportCriteria.update_all({ :mapkey => "assetStatus" },  { :mapkey => "productStatus" })
    SavedReportCriteria.update_all({ :mapkey => "assetTypeGroup" },  { :mapkey => "productTypeGroup" })
  end
  
  def self.down
    SavedReportCriteria.update_all({ :mapkey => "productType" },  { :mapkey => "assetType" })
    SavedReportCriteria.update_all({ :mapkey => "productStatus" },  { :mapkey => "assetStatus" })
    SavedReportCriteria.update_all({ :mapkey => "productTypeGroup" },  { :mapkey => "assetTypeGroup" })
  end

end