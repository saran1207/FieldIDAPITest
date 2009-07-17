require "eula"
class AddFirstEula < ActiveRecord::Migration
  def self.up
    eula = Eula.new
    eula.legaltext = "This Statement of Rights and Responsibilities MUST CHANGE
    
    change this!!
    change this!!
    
    change this!!
    change this!!
    change this!!
    
    change this!!
    
    change this!!
    change this!!
    
    
    change this!!
    change this!!
    change this!!
    
    change this!!
    change this!!
    
    
    change this!!
    change this!!
    
    change this!!
    change this!!
    change this!!
    change this!!
    
    
    change this!!
    change this!!
    change this!!
    change this!!
    
    change this!!
    change this!!
    change this!!
    
    change this!!
    change this!!
    change this!!
    change this!!
    change this!!
    change this!!
    change this!!
    
    change this!!
    change this!!
    
    change this!!
    change this!!
    change this!!
    change this!!
    change this!!
    
    change this!!
    
    change this!!change this!!
    
    change this!!
    change this!!
    change this!!
    change this!!
    
    change this!!
    change this!!
    
    
    change this!!
    change this!!
    
    change this!!
    
    change this!!
    change this!!
    change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!change this!!
    
    change this!!
    change this!!
    change this!!
    change this!!change this!!
    
    change this!!
    
    change this!!
    change this!!change this!!change this!!change this!!change this!!change this!!
    
    
    "
    
    eula.version="0.1"
    eula.effectivedate=Time.new
    eula.created=Time.new
    eula.modified=Time.new
    eula.save
  end
  
  def self.down
  end
end
