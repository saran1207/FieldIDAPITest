require 'file_content_replacer'
class UpdateCustomReportPropertiesForInspectionDateColumn < ActiveRecord::Migration
      
  @@config_dir_path = "/var/fieldid/private/conf"
  @@inspection_report_file = "com.n4systems.fieldid.actions.search/InspectionReportAction.properties"
  
  
  def self.up
    conf_dir = Dir.new(@@config_dir_path)
    
    conf_dir.each do |tenant_with_customized_report_columns|
      if !tenant_with_customized_report_columns.starts_with? '.'
        file_name = @@config_dir_path + '/' + tenant_with_customized_report_columns + '/' + @@inspection_report_file
        FileContentReplacer.replace_content(file_name, 'inspection_search_inspectiondate', 'inspection_search_date_performed') 
      end
    end
    
  end
  
  def self.down
    conf_dir = Dir.new(@@config_dir_path)
    
    conf_dir.each do |tenant_with_customized_report_columns|
      if !tenant_with_customized_report_columns.starts_with? '.'
        file_name = @@config_dir_path + '/' + tenant_with_customized_report_columns + '/' + @@inspection_report_file
        FileContentReplacer.replace_content(file_name, 'inspection_search_date_performed', 'inspection_search_inspectiondate') 
      end
    end
  end
  
end