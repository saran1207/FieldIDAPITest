require "user"
require "primary_org"
class FixTimeZones < ActiveRecord::Migration
  def self.up
   User.update_all("timezoneid = 'Australia:South Australia - Adelaide'",  { :timezoneid => "Australia:Adelaide"} )
   users = User.find(:all, :conditions => { :timezoneid => nil })
   users.each do |user| 
     user.timezoneid = 'United States:New York - New York'
     user.save
   end
  end
end