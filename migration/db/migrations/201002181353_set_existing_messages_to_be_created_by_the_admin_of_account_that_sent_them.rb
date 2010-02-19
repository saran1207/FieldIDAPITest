require "message"
require "message_command"
require "user"
require "base_org"

class SetExistingMessagesToBeCreatedByTheAdminOfAccountThatSentThem < ActiveRecord::Migration
  def self.up
    Message.transaction do 
      Message.all.each do |message|
        unless message.command.processed
          tenant_id = findSendingOrgOfMessage(message)
          admin = User.find(:first, :conditions => {:tenant_id => tenant_id, :admin => true})
          raise Exception.new("null admin for tenant : " + tenant_id.to_s) unless !admin.nil?
          message.command.createdby = admin.id
          message.command.save
          
        end
      end
    end
    
  end
  
  
  def self.findSendingOrgOfMessage(message)
    message.command.parameters.each do |org|
      org = BaseOrg.find(org.element)
      if org.tenant_id != message.owner.tenant_id
        return org.tenant_id
      end
    end
    
    raise Exception.new("both orgs from same company")
  end
end
