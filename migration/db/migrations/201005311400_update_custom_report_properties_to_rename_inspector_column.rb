class UpdateCustomReportPropertiesToRenameInspectorColumn < ActiveRecord::Migration
  @@config_dir_path = "/var/fieldid/private/conf"
  @@inspection_report_file = "com.n4systems.fieldid.actions.search/InspectionReportAction.properties"
  
  
  def self.up
    conf_dir = Dir.new(@@config_dir_path)
    
    conf_dir.each do |tenant_with_customized_report_columns|
      if !tenant_with_customized_report_columns.starts_with? '.'
        file_name = @@config_dir_path + '/' + tenant_with_customized_report_columns + '/' + @@inspection_report_file
        replace_content(file_name, 'inspection_search_inspector', 'inspection_search_performed_by') 
      end
    end
    
  end
  
  def self.down
    conf_dir = Dir.new(@@config_dir_path)
    
    conf_dir.each do |tenant_with_customized_report_columns|
      if !tenant_with_customized_report_columns.starts_with? '.'
        file_name = @@config_dir_path + '/' + tenant_with_customized_report_columns + '/' + @@inspection_report_file
        replace_content(file_name, 'inspection_search_performed_by', 'inspection_search_inspector') 
      end
    end
  end
  
  
  
  def self.replace_content(file_name, find, replace)
    if File.file? file_name
      file = File.new(file_name)
      lines = file.readlines
      file.close
  
      changes = false
      lines.each do |line|
        changes = true if line.gsub!(/#{find}/, replace)
      end
  
      # Don't write the file if there are no changes
      if changes
        file = File.new(file_name,'w')
        lines.each do |line|
          file.write(line)
        end
        file.close
      end
    end
  end
end