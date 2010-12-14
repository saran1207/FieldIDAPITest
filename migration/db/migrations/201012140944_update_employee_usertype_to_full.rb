require 'user'

class UpdateEmployeeUsertypeToFull < ActiveRecord::Migration
    def self.up
       User.reset_column_information
       User.update_all({:usertype=>"FULL"},  { :usertype => "EMPLOYEES" })

    end

    def self.down

    end

end