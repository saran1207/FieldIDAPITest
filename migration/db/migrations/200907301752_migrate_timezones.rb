require "user"

class MigrateTimezones < ActiveRecord::Migration
  
  def self.up
    
    users = User.find(:all)
    
    users.each do |user|
      puts user.userid
      puts user.timezoneid
      user.timezoneid = self.resolveTimeZone(user.timezoneid)
      user.save
    end
    
  end
  
  def self.down
  end
  
  def self.resolveTimeZone(zoneid)
    case zoneid
      when nil:                     return "United States:New York - New York"
      when "America/Chicago":       return "United States:Illinois - Chicago"
      when "America/St_Johns":      return "Canada:Newfoundland - St. Johns"
      when "America/Swift_Current": return "Canada:Saskatchewan - Swift Current"
      when "America/Denver":        return "United States:Colorado - Denver"
      when "America/Halifax":       return "Canada:Nova Scotia - Halifax"
      when "EST":                   return "United States:New York - New York"
      when "Australia/Sydney":      return "Australia:New South Wales - Sydney"
      when "America/Los_Angeles":   return "United States:California - Los Angeles"
      when "Australia/Adelaide":    return "Australia:Adelaide"
      when "America/New_York":      return "United States:New York - New York"
      when "America/Toronto":       return "Canada:Ontario - Toronto"
      when "America/Fort_Wayne":    return "United States:Indiana - Fort Wayne"
      when "America/Detroit":       return "United States:Michigan - Detroit"
      when "America/Cancun":        return "Mexico:Cancun"
      when "America/Montreal":      return "Canada:Quebec - Montreal"
      when "America/Adak":          return "United States:Alaska - Adak"
      when "America/Montserrat":    return "Montserrat:Montserrat"
      when "America/Boise":         return "United States:Idaho - Boise"
      when "America/Vancouver":     return "Canada:British Columbia - Vancouver"
      when "America/Kentucky/Louisville": return "United States:Kentucky - Louisville"
      when "America/Cayman":        return "Cayman Islands:Cayman Islands"
      when "America/La_Paz":        return "Bolivia:La Paz"
      when "America/Goose_Bay":     return "Canada:Nova Scotia - Halifax"
      when "America/Anchorage":     return "United States:Alaska - Anchorage"
      when "Australia/Perth":       return "Australia:Western Australia - Perth"
      when "America/Phoenix":       return "United States:Arizona - Phoenix"
      when "Australia/NSW":         return "Australia:New South Wales - Sydney"
      when "America/Mexico_City":   return "Mexico:Federal District - Mexico City"
      when "America/Tegucigalpa":   return "Honduras:Tegucigalpa"
      when "America/Edmonton":      return "Canada:Alberta - Edmonton"
      when "America/North_Dakota/Center":  return "UnitedStates:North Dakota - Bismarck"
      when "Australia:Sydney":      return "Australia:New South Wales - Sydney"
      when "Australia/Canberra":    return "Australia:New South Wales - Sydney"
      when "Europe/London":         return "United Kingdom:England - London"
      when "America/Santarem":      return "Brazil:Santarem"
      when "America/Antigua":       return "Puerto Rico:San Juan"
      when "America/Anguilla":       return "Puerto Rico:San Juan"
      when "America/Knox_IN":       return "United States:Chicago"
      when "Europe/Amsterdam":      return "Netherlands:Amsterdam"
      when "America/Moncton":       return "Canada:Nova Scotia - Halifax"

      else
        return zoneid unless (zoneid.index(":").nil?)
        
        raise "Unknown TimeZone [" + zoneid + "]"
    end
  end
  
end
