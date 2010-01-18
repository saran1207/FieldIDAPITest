require "csv"
require "tenant"
require "customer_org"
require "secondary_org"
require "primary_org"
class CustomerImporting 
  
  
  def import(file_name, tenant)
   job_site_infos = readCSV(file_name)
   job_site_infos.each do |job_site_info|
      now = Time.new
      parent_org = SecondaryOrg.find(:first, :conditions => { :org_base => { :name => job_site_info[:organizational_unit], :tenant_id => tenant.id } }, :joins => [:baseOrg])
      puts job_site_info[:organizational_unit] + "is nil" if parent_org.nil?
      
      address = Addressinfo.create(:streetaddress => job_site_info[:address], :phone1 => job_site_info[:phone], :city => job_site_info[:city], :state => job_site_info[:state], :created => now, :modified => now)
      base_org = BaseOrg.create(:name => job_site_info[:name], :addressinfo_id => address.id, :tenant => tenant, :created => now, :modified => now)
      customer_org = CustomerOrg.create(:code => job_site_info[:job_site_id], :parent => parent_org.baseOrg, :baseOrg => base_org)
    end
  end
  
  private
    def readCSV(fileName)
      file_data = CSV::Reader.parse(File.new("db/maintenance/"+fileName, 'r'))
      titles = [:name, :organizational_unit, :address, :job_site_id, :city,  :state, :phone]
  
      job_site_info = [] 
      file_data.each do |row|
        job_site = {};
        titles.each_index do |index|
          job_site[titles[index]] = row[index]
        end
        job_site_info << job_site
      end
      
      job_site_info
    end
end