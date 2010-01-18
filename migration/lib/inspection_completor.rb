require "abstract_inspection"
require "inspection_type"
class InspectionCompletor 
  
  def initialize(tenant, skip_state, button_group)
    super()
    @tenant = tenant
    @skip_state = skip_state
    @button_group = button_group
  end
  
  def process
    InspectionType.find(:all, :conditions => { :tenant_id => @tenant.id }, :order => :id).each do |inspectionType|
      complete_all_inspection_of_type inspectionType
    end
    
  end
  
  def complete_all_inspection_of_type(inspectionType) 
    AbstractInspection.find_in_batches(:conditions => { :type_id => inspectionType.id }) do |inspections|
      inspections.each do |inspection|
        fill_out inspection, inspectionType
        puts inspection.id.to_s
        inspection.save
      end
    end
  end
  
  def fill_out(inspection, inspectionType)
    unless inspection.formversion != inspectionType.formversion  
      criteria_list = Array.new
      inspectionType.sections.each do |section|
        criteria_list.concat(section.criterias)
      end
       
      missing_criteria = criteria_list.select do |criteria|
        found_results = inspection.results.select do |item|
          
          item.criteria_id == criteria.id.to_i
        end
        found_results.empty?
      end
      
      missing_criteria.each do |criteria|
        if criteria.states.id != @button_group.id 
          raise Exception.new("criteria does not have the correct button group! [" + criteria.displaytext + "]  [" + inspectionType.name + "] + [" + @button_group.name + "] [" +criteria.states.id.to_s + "] [" + @button_group.id.to_s + "]" )
        end
        filled_in_result = CriteriaResult.new
        filled_in_result.tenant_id = @tenant.id
        filled_in_result.state_id = @skip_state.id
        filled_in_result.criteria_id = criteria.id
        filled_in_result.inspection_id = inspection.id
        filled_in_result.created = Time.new
        filled_in_result.modified = Time.new
        puts criteria.displaytext
        inspection.results << filled_in_result
      end
      
    end
  end
  
end
