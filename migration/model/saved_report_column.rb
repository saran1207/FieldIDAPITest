require 'composite_primary_keys'
require "saved_report"

class SavedReportColumn < ActiveRecord::Base
  set_table_name :savedreports_columns
  set_primary_keys  :savedreports_id, :mapkey
  
  belongs_to  :savedReport, :foreign_key => 'savedreports_id',  :class_name => 'SavedReport'
  
  def displayString
    "#{savedreports_id.to_s} [#{mapkey} = #{element.to_s}]"
  end
  
end