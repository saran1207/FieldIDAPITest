require "user"
class MoveUsersToNewTimeZones < ActiveRecord::Migration
  def self.up
      timezones = { "EST" => "America/New_York",
                    "CST" => "America/Chicago",
                    "MST" => "America/Denver",
                    "PST" => "America/Los_Angeles",
                    "US/Alaska" => "America/Anchorage",
                    "US/Hawaii" => "America/Adak"}
     
     for user in User.find(:all) 
       if user.r_tenant == 15511453
         user.timezoneid = "Australia/Sydney"
       elsif user.r_tenant == 15511515
         user.timezoneid = "Australia/Adelaide"
       else
         user.timezoneid = timezones[user.timezoneid]
       end
       user.timezoneid = timezones["EST"] if user.timezoneid.nil?
       
       user.save
     end
  end
  
  def self.down
  end
end