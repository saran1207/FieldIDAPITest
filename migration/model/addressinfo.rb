class Addressinfo < ActiveRecord::Base
  set_table_name :addressinfo
  
  def set_fields_from_map(map)
  	self.streetaddress = map["streetaddress"]
  	self.city = map["city"]
  	self.state = map["state"]
  	self.country = map["country"]
  	self.zip = map["zip"]
  	self.phone1 = map["phone1"]
  	self.phone2 = map["phone2"]
  	self.fax1 = map["fax1"]
  end
  
  def displayString
    "#{id.to_s}: #{streetaddress}, #{city}, #{state}, #{country}, #{zip}, #{phone1}, #{phone2}, #{fax1}"
  end
end