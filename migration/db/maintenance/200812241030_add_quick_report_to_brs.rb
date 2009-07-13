require "organization"
require "report"
require "report_manufacture"
class AddQuickReportToBrs < ActiveRecord::Migration
  def self.up
    Organization.transaction do 
      bosieRigging = Organization.find( :first, :conditions => { :name => "brs" } )
      quickReport = Report.find( :first, :conditions => { :reportkey => "inspectionSummaryQuick" } )
      link = ReportManufacture.new( { :r_tenant => bosieRigging.id, :r_report => quickReport.uniqueid, :weight => 2 } )
      link.save
    end
  end
end