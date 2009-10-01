require "user"
class CleanUpBlankSecurityRfidNumbers < ActiveRecord::Migration
  def self.up
    User.update_all("hashsecuritycardnumber = null", :hashsecuritycardnumber => 'da39a3ee5e6b4b0d3255bfef95601890afd80709')
  end
  
  def self.down
  end
end