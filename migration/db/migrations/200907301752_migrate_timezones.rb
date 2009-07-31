require "user"

class MigrateTimezones < ActiveRecord::Migration
  
  def self.up
    
    users = User.find(:all)
    
    users.each do |user|
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
      when "Australia/Sydney":      return "Australia:Sydney"
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
      else raise "Unknown TimeZone [" + name + "]"
    end
  end
  
end