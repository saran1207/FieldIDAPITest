class CleanUpSavedReportChargeColumn < ActiveRecord::Migration
  def self.up
    execute "delete from savedreports_columns where savedreports_id in (5,6,53,54) and element='inspection_search_charge'"
    execute "update savedreports_columns set element='inspection_search_unirope_charge' where element='inspection_search_charge'"
  end
end