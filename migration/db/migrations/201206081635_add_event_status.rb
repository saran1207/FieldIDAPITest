require "tenant"
require "event_status"

class AddEventStatus < ActiveRecord::Migration

  def self.up

    create_table "eventstatus", :primary_key => "id" do |t|
      create_entity_with_tenant_fields_on(t)
      t.string :name, :null => false
      t.string :state, :null => false
    end

    Tenant.all.each do |tenant|
      EventStatus.create(:tenant_id => tenant.id, :created => Time.new, :modified => Time.new, :name =>"Could Not Inspect", :state=>"ACTIVE")
      EventStatus.create(:tenant_id => tenant.id, :created => Time.new, :modified => Time.new, :name =>"Not In Use", :state=>"ACTIVE")
      EventStatus.create(:tenant_id => tenant.id, :created => Time.new, :modified => Time.new, :name =>"Destroyed", :state=>"ACTIVE")
    end

  end

  def self.down
    drop_table "eventstatus"
  end


end