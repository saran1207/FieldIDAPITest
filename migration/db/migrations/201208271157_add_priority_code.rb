require "tenant"
require "priority_code"

class AddPriorityCode < ActiveRecord::Migration

  def self.up

    create_table "prioritycode", :primary_key => "id" do |t|
      create_entity_with_tenant_fields_on(t)
      t.string :name, :null => false
      t.string :state, :null => false
    end

    Tenant.all.each do |tenant|
      PriorityCode.create(:tenant_id => tenant.id, :created => Time.new, :modified => Time.new, :name =>"Critical", :state=>"ACTIVE")
      PriorityCode.create(:tenant_id => tenant.id, :created => Time.new, :modified => Time.new, :name =>"High", :state=>"ACTIVE")
      PriorityCode.create(:tenant_id => tenant.id, :created => Time.new, :modified => Time.new, :name =>"Normal", :state=>"ACTIVE")
      PriorityCode.create(:tenant_id => tenant.id, :created => Time.new, :modified => Time.new, :name =>"Low", :state=>"ACTIVE")
    end

  end

  def self.down
    drop_table "prioritycode"
  end

end
