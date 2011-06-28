class ChangeDatefieldcriteriaresultDateToNullable < ActiveRecord::Migration

  def self.up  
  	execute "alter table datefield_criteriaresults modify value datetime"
  end
  
  def self.down    
	execute "alter table datefield_criteriaresults modify value datetime not null"
  end
  
end