
class LowercaseUploadedFileFolders < ActiveRecord::Migration
  def self.up
       
    directories_to_scan = ["images", "projects/attachments", "productTypes/images", "productTypes/attachments",
        "inspections/attachments", "inspections/chartimages", "inspections/prooftests", "conf"]
    base_directory = "/var/fieldid/private/"
    for directory_name in directories_to_scan
      directory_to_fix = base_directory + directory_name
      directory = Dir.new directory_to_fix
      puts directory_to_fix
      directory.entries.each do |tenant_directory |
        puts tenant_directory
        if tenant_directory != "." && tenant_directory != ".." && File.exist?(directory_to_fix + "/" + tenant_directory)
          File.rename( directory_to_fix + "/" + tenant_directory, directory_to_fix + "/" + tenant_directory.downcase)
	  puts tenant_directory
        end
      end
      
    end
    
  end
  
  def self.down
    raise ActiveRecord::IrreversibleMigration
  end
end
