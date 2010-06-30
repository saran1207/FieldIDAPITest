require "predefined_location"
require "tenant"
class PredefinedLocationGenerator
  def self.generateFor(tenant_name, depth, nodes_per_level)
    tenant = Tenant.find(:first, :conditions => { :name => tenant_name })
    raise Exception.new("tenant could not be found") unless !tenant.nil?
    
    PredefinedLocationGenerator.new(depth, nodes_per_level, tenant).generate
  end
  
  
  def initialize(depth, nodes_per_level, tenant)  
    @depth = depth 
    @nodes_per_level = nodes_per_level
    @tenant = tenant
  end
  
  def generate() 
    create_nodes(1, nil)
  end
  
  private 
    def create_nodes(level, parent)
      return unless (level <= @nodes_per_level)
      
      parent_id = nil
      parent_id = parent.id unless parent.nil?
      
      Range.new(1, @nodes_per_level).each do |i|
        location = PredefinedLocation.new(:name => generate_name(i), :tenant_id=>@tenant.id, :parent_id => parent_id, :created => Time.now, :modified => Time.now)
        location.save
        create_nodes(level + 1, location)
      end
      
    end
  
    def generate_name(i)
      "location " + i.to_s + "__" + (0...5).map{ ('a'..'z').to_a[rand(26)] }.join
    end
  
end