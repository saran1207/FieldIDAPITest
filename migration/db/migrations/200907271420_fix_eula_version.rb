require "eula"
class FixEulaVersion < ActiveRecord::Migration
  def self.up
    eula = Eula.find(:first)
    eula.version = "1.0"
    eula.save
  end
end