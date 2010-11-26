require 'saved_report'

class ChangeReportSortColumnsForEntityRenames < ActiveRecord::Migration

  def self.up
    SavedReport.update_all({ :sortcolumn => "asset.assetStatus.name" },  { :sortcolumn => "product.productStatus.name" })
    SavedReport.update_all({ :sortcolumn => "asset.serialNumber" },  { :sortcolumn => "product.serialNumber" })
    SavedReport.update_all({ :sortcolumn => "asset.type.name" },  { :sortcolumn => "product.type.name" })
    SavedReport.update_all({ :sortcolumn => "asset.customerRefNumber" },  { :sortcolumn => "product.customerRefNumber" })
  end

  def self.down
    SavedReport.update_all({ :sortcolumn => "product.productStatus.name" },  { :sortcolumn => "asset.assetStatus.name" })
    SavedReport.update_all({ :sortcolumn => "product.serialNumber" },  { :sortcolumn => "asset.serialNumber" })
    SavedReport.update_all({ :sortcolumn => "product.type.name" },  { :sortcolumn => "asset.type.name" })
    SavedReport.update_all({ :sortcolumn => "product.customerRefNumber" },  { :sortcolumn => "asset.customerRefNumber" })
  end

end