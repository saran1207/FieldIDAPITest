require "product"
require "tenant"
class PublishAllMsaAssets < ActiveRecord::Migration
  def self.up
    msa = Tenant.find(:first, :conditions => { :name => "msa" } )
    Product.update_all("published = TRUE ", :tenant_id => msa.id )
  end
end