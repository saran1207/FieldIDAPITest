require "tenant"

class TagOption < ActiveRecord::Base
  set_table_name :tagoptions
  
  belongs_to  :tenant,      :foreign_key => 'tenant_id',   :class_name => 'Tenant'
  
  def self.resolveNewKeyName(legacyKey)
    case legacyKey
      when "ordernumber":         return "SHOPORDER"
      when "reelid":              return "REELID"
      when "prereelid":           return "PREREELID"
      when "customerordernumber": return "CUSTOMERORDER"
      else raise "TagOption key mapping for [" + legacyKey + "]"
    end
  end
  
  def displayString
    "#{key} (#{id.to_s})"
  end
end
