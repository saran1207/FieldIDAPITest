require "user"

class ConsolidateUserTypeFlagsIntoAUsertypeEnumColumn < ActiveRecord::Migration

  def self.up
      add_column(:users, :usertype, :string, {:null=> false})

      User.reset_column_information
      User.update_all({:usertype=>"SYSTEM"},  { :system => true })
      User.update_all({:usertype=>"ADMIN"},  { :admin => true })
      User.update_all({:usertype=>"EMPLOYEES"},  { :employee => true, :system => false, :admin => false  })
      User.update_all({:usertype=>"READONLY"},  { :employee => false , :system => false, :admin => false})
  end

  def self.down
     remove_column(:users, :usertype)
  end

end