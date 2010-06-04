require 'Product'
require 'abstract_inspection'
require 'inspection_info_option'
require 'criteria_result'
require 'inspection_file_attachment'
require "product_status"

def getMasterInspectionIds(serialnumber)
  #Get the corresponding product_id
  product_id = Product.find_by_serialnumber(serialnumber).id

  #Retrieve all master inspection matching this id
  result_set =  AbstractInspection.all(:include => :master_inspections, :conditions => {:product_id => product_id})
  id_set=Array.new

  #Extract master inspection ids from each row.
  result_set.each do |abstract_inspection_set|
    abstract_inspection_set.master_inspections.each do |master_inspection|
      id_set << master_inspection.inspection_id
    end
  end

  id_set

end

def getSubInspectionIds(serialnumber)

  product_id = Product.find_by_serialnumber(serialnumber).id
  id_set = Array.new

  result_set = AbstractInspection.all(:include => :sub_inspections, :conditions =>{:product_id => product_id})

  result_set.each do |abstract_inspection_set|
    abstract_inspection_set.sub_inspections.each do |sub_inspection|
      id_set << sub_inspection.inspection_id
    end
  end

  id_set

end

def getSubProductIds(master_serialnumber)

  #Retrive all subproducts joined on this master product serialnumber.
  sub_products= Product.first(:include=>:subProducts, :conditions=>{:serialnumber=>master_serialnumber})
  sub_product_ids = Array.new

  sub_products.subProducts.each do |sub_product|
    sub_product_ids << sub_product.product_id
  end

  sub_product_ids
end


def verify_inspection_attributes(inspection_id, attributes_list)

  result_set = Array.new

  #Join the tables.
  inspection_info_options = InspectionInfoOption.find_all_by_inspections_id(inspection_id)

  #Extract info_option element
  inspection_info_options.each do |info_option| result_set << info_option.element end

  #Very the two lists of attributes are the same
  result_set.sort.eql?(attributes_list.sort)

end

#Check button states and values
def verify_criteria_forms_buttons(inspection_id, forms_list)

  state_set_recommendations = Array.new

  #Join the tables.
  result_set = CriteriaResult.all(:include => :state, :conditions => {:inspection_id => inspection_id})

  #Extract state text values
  result_set.each do |form_state| state_set_recommendations << form_state.state.displaytext end

  #Verify
  state_set_recommendations.sort.eql?(forms_list.sort)


end
#
#  #Check observation states and values.
#  #TODO verify typed comments as well.
#  def verify_criteria_forms_observations(inspection_id, recommendation_states, deficiency_states)
#
#    state_set_recommendations = Array.new
#    state_set_deficiencies = Array.new
#
#    #Extract the two recommendation states for this inspection.
#    result_set = CriteriaResult.all(:include => {:recommendations=>:observation}, :conditions => {:inspection_id=>inspection_id })
#    result_set.each do |recommendation_set|
#
#      #this array bullshit is causing problems
#   p   state_set_recommendations <<  recommendation_set.recommendations[0].observation.state
#      state_set_recommendations <<  recommendation_set.recommendations[1].observation.state
#    end
#
#    #Extract the two deficiency states for this inspection
#    result_set = CriteriaResult.all(:include => {:deficiencies => :observation}, :conditions => {:inspection_id=>inspection_id })
#    result_set.each do |deficiency_set|
#
#      state_set_deficiencies <<  deficiency_set.deficiencies[0].observation.state
#      state_set_deficiencies <<  deficiency_set.deficiencies[1].observation.state
#    end
#
#    #Verify recommendation states and observation states match their respective csv values..
#    state_set_recommendations.sort.eql?(recommendation_states.sort) and state_set_deficiencies.sort.eql?(deficiency_states.sort)
#
#  end


#Check observation states and values.
#TODO verify typed comments as well.
def verify_criteria_forms_observations(inspection_id, recommendation_states, deficiency_states)

  state_set_recommendations = Array.new
  state_set_deficiencies = Array.new

  #Extract the two recommendation states for this inspection.
  result_set = CriteriaResult.all(:include => {:recommendations=>:observation}, :conditions => {:inspection_id=>inspection_id })
  result_set.each do |recommendation_set|
    #p  recommendation_set
    recommendation_set.each do |state_set|
      p state_set.recommendations
    end
    #p recommendation_set.recommendations
    #this array bullshit is causing problems
    state_set_recommendations <<  recommendation_set.recommendations[0].observation.state unless recommendation_set.recommendations==[]
    state_set_recommendations <<  recommendation_set.recommendations[1].observation.state unless recommendation_set.recommendations==[]
  end

  #Extract the two deficiency states for this inspection
  result_set = CriteriaResult.all(:include => {:deficiencies => :observation}, :conditions => {:inspection_id=>inspection_id })
  result_set.each do |deficiency_set|
    p deficiency_set
    state_set_deficiencies <<  deficiency_set.deficiencies[0].observation.state
    state_set_deficiencies <<  deficiency_set.deficiencies[1].observation.state
  end

  #Verify recommendation states and observation states match their respective csv values..
  state_set_recommendations.sort.eql?(recommendation_states.sort) and state_set_deficiencies.sort.eql?(deficiency_states.sort)

end

def verify_pictures(inspection_id, number_of_pictures)
  result_set = InspectionFileAttachment.find_by_inspections_id(inspection_id)
  if result_set.kind_of? Array then result_set.count == number_of_pictures else 1 == number_of_pictures end
end


def verify_product_attributes(serialnumber, attributes_list)

  #Retrieve attributes.
  info_options = Product.first(:include =>:infoOptionsFK, :conditions =>{:serialnumber =>serialnumber}).infoOptions
  attributes = Array.new
  info_options.each do |attribute|
    attributes << attribute.name
  end

  attributes.sort.eql?(attributes_list.sort)

end

def verify_identified_date(serialnumber, date)
  #  Product.find_by_serialnumber(serialnumber).date
end

#Note to self, remove [0] index when rp10 duplication issus is removed.
def verify_product_status(serialnumber, status)
  #.name doesn't return what I need. Do another lookup.
  uniqueid = Product.all(:include =>:product_statuses, :conditions =>
      { :serialnumber => serialnumber})[1].productstatus_uniqueid
  ProductStatus.find_by_uniqueid(uniqueid).name.should == status
end

def verify_order_number(serialnumber, order_number)
  result_set = Product.first(:include =>{:tenant =>:orders}, :conditions =>{:serialnumber =>serialnumber}).tenant.orders
  order_numbers=Array.new

  result_set.each do |order|
    order_numbers << order.ordernumber
  end

  order_numbers.include?(order_number)

end