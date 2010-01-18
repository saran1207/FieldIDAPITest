require "csv_loader"
require "org_loader"
require "addressinfo"

class ImportSievertCustomerInfo < ActiveRecord::Migration
  def self.up
	addrData = CSVLoader.new("db/maintenance/data/sievert_division_addresses.csv").load()
	    
    addrData.each do |row|
    	begin
   			org = OrgLoader.new().load_org_by_name_map(row)
   			puts "Found Org: #{org.orgPath}"
   			
   			addressInfo = org.baseOrg.addressInfo

   			if (addressInfo.nil?)
   				addressInfo = Addressinfo.create
   				org.baseOrg.addressInfo = addressInfo
   				org.baseOrg.save
   			end

			addressInfo.set_fields_from_map(row)
			addressInfo.save

   		rescue OrgNotFoundException => message
   			$stderr.puts "WARN: #{message}"
   		end
    end
  end
 
end