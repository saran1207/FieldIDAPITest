require "user"
require 'digest/sha1'
class FixOneLowerCaseSecurityCard < ActiveRecord::Migration
  def self.up
    user = User.find(310620)
    user.hashsecuritycardnumber = Digest::SHA1.hexdigest("e00401000c2dcae8".upcase);
    user.save
  end
end    
