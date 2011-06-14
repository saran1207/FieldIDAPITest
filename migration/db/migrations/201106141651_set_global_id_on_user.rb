require "user"

class SetGlobalIdOnUser < ActiveRecord::Migration
  
 def self.up
    execute "update users set globalId = UUID()";
  end


  def self.down
    execute "update users set globalId = null";
  end

end