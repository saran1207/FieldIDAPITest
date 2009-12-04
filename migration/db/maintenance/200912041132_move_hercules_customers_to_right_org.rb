require "csv"
require "customer_org"

class MoveHerculesCustomersToRightOrg < ActiveRecord::Migration 
	def self.up
		secondary_org_ids = [15511100, 15511150, 15511200, 15511201, 15511250, 15511352, 15511401, 15511403, 15511487, 15511539, 15535991, 15535992]
		
		secondary_org_ids.each do |s_id|
			print "For organization id " + s_id.to_s + "\n"
		
			parsed_file = CSV::Reader.parse(File.open("db/maintenance/hercules_customers/" + s_id.to_s + ".csv", "rb"))
			parsed_file.each do |row|
				print "Fixing " + row[1] + "......." unless row[1].nil?
				
				unless row[0].nil? 
					customer = CustomerOrg.find(:first, :conditions => { :legacy_id => row[0]})
					unless customer.nil?
						customer.parent_id = s_id
						customer.save				
						print "fixed\n"
					else
						print "could not find\n"	
					end
				end
			end
		end
	
	end
	
	def self.down
	end
end