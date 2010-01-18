require "tenant"
require "base_org"
require "primary_org"
require "secondary_org"
require "customer_org"
require "division_org"

class OrgType
  PRIMARY=0
  SECONDARY=1
  CUSTOMER=2
  DIVISION=3
end

class OrgNotFoundException < RuntimeError
end


class OrgLoader
	
	def load_org_by_name_map(nameMap)
		return load_org_by_name(nameMap["tenant"], nameMap["primary_name"], nameMap["seconday_name"], nameMap["customer_name"], nameMap["division_name"])
	end
	
	def load_org_by_name(tenantName, primaryOrgName = nil, secondaryOrgName = nil, customerOrgName = nil, divisionOrgName = nil)
		tenant = load_tenant(tenantName)
		
		# handle the primary separately since it won't be needed for subsequent queries  
		if (!primaryOrgName.nil? && secondaryOrgName.nil? && customerOrgName.nil? && divisionOrgName.nil?)
			return load_primary(primaryOrgName, tenant)
		end

		secondary = load_secondary(secondaryOrgName, tenant)
		customer = load_customer(customerOrgName, tenant, secondary)
		division = load_division(divisionOrgName, tenant, secondary, customer)
		
		if (!division.nil?)
			return division
		elsif (!customer.nil?)
			return customer
		else
			return secondary
		end
	end
	
	def load_tenant(name)
		if name.nil?
			raise "tenantName must not be null"
		end
		
		tenant = Tenant.find(:first, :conditions => {:name => name})
		
		if (tenant.nil?)
			raise "Could not find tenant for name [#{name}]"
		end
		
		return tenant
	end
		
	def load_primary(name, tenant)
		baseId = load_base_org_id_by_name(name, tenant.id, OrgType::PRIMARY)
		
		return (!baseId.nil?) ? PrimaryOrg.find(baseId) : nil
	end
	
	def load_secondary(name, tenant)
		baseId = load_base_org_id_by_name(name, tenant.id, OrgType::SECONDARY)
		
		return (!baseId.nil?) ? SecondaryOrg.find(baseId) : nil
	end
	
	def load_customer(name, tenant, secondary = nil)
		baseId = load_base_org_id_by_name(name, tenant.id, OrgType::CUSTOMER, org_id_or_nil(secondary))
		
		return (!baseId.nil?) ? CustomerOrg.find(baseId) : nil
	end
	
	def load_division(name, tenant, secondary, customer)
		baseId = load_base_org_id_by_name(name, tenant.id, OrgType::DIVISION, org_id_or_nil(secondary), customer.org_id)
		
		return (!baseId.nil?) ? DivisionOrg.find(baseId) : nil
	end
		
	private

		def load_base_org_id_by_name(name, tenant_id, orgTypeTarget, secondary_id = nil, customer_id = nil)
			if (name.nil?)
				return nil
			end
		
			base = nil
			targetName = ""		
			case orgTypeTarget
				when OrgType::PRIMARY
					targetName = "PrimaryOrg"
					base = BaseOrg.select_id.for_tenant(tenant_id).named(name).primaries_only.find(:first)
				when OrgType::SECONDARY
					targetName = "SecondaryOrg"
					base = BaseOrg.select_id.for_tenant(tenant_id).named(name).secondaries_only.find(:first)
				when OrgType::CUSTOMER
					targetName = "CustomerOrg"
					base = BaseOrg.select_id.for_tenant(tenant_id).named(name).customers_only(secondary_id).find(:first)
				when OrgType::DIVISION
					targetName = "DivisionOrg"
					base = BaseOrg.select_id.for_tenant(tenant_id).named(name).divisions_only(customer_id).find(:first)
				else
					raise "Unknown OrgType #{orgTypeTarget}"
			end
			
			if (base.nil?)
				raise OrgNotFoundException, "Could not find #{targetName} [#{name}] under tenant_id [#{tenant_id}], secondary_id [#{secondary_id}], customer_id [#{customer_id}]", caller
			end
			
			return base.id
		end

		def org_id_or_nil(secondary)
			return (!secondary.nil?) ? secondary.org_id : nil
		end
end