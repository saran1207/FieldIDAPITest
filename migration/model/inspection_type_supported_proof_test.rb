require 'composite_primary_keys'
require 'inspection_type'
class InspectionTypeSupportedProofTest < ActiveRecord::Base
  set_primary_keys :inspectiontypes_id, :element
  set_table_name :inspectiontypes_supportedprooftests
  belongs_to :inspection_type, :foreign_key => 'inspectiontypes_id'
  
  def self.resolveProofTestName(oldName)
    case oldName
        when "chant":               "CHANT"
        when "nationalautomation":  "NATIONALAUTOMATION"
        when "roberts":             "ROBERTS"
        when "wirope":              "WIROP"
        when "wirop":               "WIROP"
        else raise "No ProofTest mapping found for [" + oldName + "]"
    end
  end
end