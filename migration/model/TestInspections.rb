require 'faster_csv'
require 'test_helpers'
require 'product'
require 'abstract_inspection'
require 'inspection_info_option'
require 'sub_product'
require 'criteria_result'
require 'inspection_schedule'
require 'criteriaresults_deficiencies'
require 'inspection_file_attachment'
require 'sub_inspections'
require 'inspectionmaster'


#Prerequisites: Serialnumbers are unique.
#Note: Assets may have multiple inspections, thus why inspection ids are 
#retrieved in arrays. In general, inspections performed earlier are at
#the start of the array.
#
#Current test input retrieved from 190TestNewInspections.csv

describe "An Inspection" do

  #Setup
  before(:all) do

    #Create a connection
    ENV[ "DBCONFIG" ] ||= 'c:\workspace\web\migration\config\database.yml'
    ActiveRecord::Base.configurations = YAML::load( File.open( ENV[ "DBCONFIG" ] ) )
    ActiveRecord::Base.establish_connection( ActiveRecord::Base.configurations[ "development" ] )

    #Read in the csv contents.
    @inspection_csv_contents = FasterCSV.read("190TestNewInspections.csv", :quote_char => '"', :col_sep =>',', :row_sep =>:auto)
  
  end

  it "of standard type with: forms, inspection attributes, picture; persists when created (Test #1)" do
 
    (master_inspection_ids = getMasterInspectionIds(@inspection_csv_contents[1][1])).empty?.should_not be true
    
    #Test inspection attributes.
    #Note: I know there is only one inspection in the array for this asset, reference it directly.
    verify_inspection_attributes(master_inspection_ids[0], [@inspection_csv_contents[1][4], @inspection_csv_contents[1][5]]).should_not be false

    #Test Criteria Form buttons
    verify_criteria_forms_buttons(master_inspection_ids[0], [@inspection_csv_contents[1][8]]).should_not be false
    
    #Test observation states
    verify_criteria_forms_observations(master_inspection_ids[0],
      [@inspection_csv_contents[1][12],@inspection_csv_contents[1][13]],
      [@inspection_csv_contents[1][14],@inspection_csv_contents[1][15]]).should_not be false

    #Test picture exists.
    verify_pictures(master_inspection_ids[0], Integer(@inspection_csv_contents[1][16])).should_not be false

  end

  it "of master type, with forms and picture but no observations or attributes, persists when created (Test #2)" do
    
    (master_inspection_ids = getMasterInspectionIds(@inspection_csv_contents[2][1])).empty?.should_not be true
    
    verify_criteria_forms_buttons(master_inspection_ids[0],
      [@inspection_csv_contents[2][8], @inspection_csv_contents[2][9]]).should_not be false

    verify_pictures(master_inspection_ids[0], Integer(@inspection_csv_contents[2][16])).should_not be false

  end

  it "test that a previously created asset is correctly attached and inspected(Test #3)" do

    sub_product_id = Product.find_by_serialnumber(@inspection_csv_contents[3][3]).id
    sub_product_ids = getSubProductIds(@inspection_csv_contents[3][1])
    
    #Check to see whether it was attached
    sub_product_ids.include?(sub_product_id).should_not be false

    #Check to see whether it's inspection exists.
    (sub_inspections = getSubInspectionIds(@inspection_csv_contents[3][3])).empty?.should be false

  end

  it "of a created subcomponent persists when attached to a master (Test #4)" do

    sub_product_id = Product.find_by_serialnumber(@inspection_csv_contents[4][3]).id
    sub_product_ids = getSubProductIds(@inspection_csv_contents[4][1])

    #Check to see whether it was attached
    sub_product_ids.include?(sub_product_id).should_not be false

    #Check to see whether it's inspection exists.
    (sub_inspections= getSubInspectionIds(@inspection_csv_contents[4][3])).empty?.should be false
  end

  it "of a subcomponent is not removed when asset is attached to a master and inspected again (Test #5)" do

    sub_product_id = Product.find_by_serialnumber(@inspection_csv_contents[5][1]).id
    sub_product_ids = getSubProductIds(@inspection_csv_contents[5][3])

    #Check to see whether it was attached
    sub_product_ids.include?(sub_product_id).should_not be false

    #Check to see whether it's previous inspection still exists.
    (previous_inspections = getMasterInspectionIds(@inspection_csv_contents[5][1])).empty?.should be false
  end

  it "of standard component attached to master, has correct inspection attributes (Test #6)" do

    #Retrive subinspection. Note: If it's subinspection doesn't exist, it's not properly attached to a master!
    (sub_inspection_ids = getSubInspectionIds(@inspection_csv_contents[6][1])).should_not be nil

    verify_inspection_attributes(sub_inspection_ids[0], [@inspection_csv_contents[6][4], @inspection_csv_contents[6][5]]).should_not be false
 
  end

  it "was correctly added to a new book (Test #7)" do
    master_inspection_id = getMasterInspectionIds(@inspection_csv_contents[7][1])
    Inspection.first(:include => :book, :conditions=>{:inspection_id=>master_inspection_id[0]}).book.name.should == @inspection_csv_contents[7][4]

  end

  it "multi event inspections work (Test #8)" do
    (master_inspection_id = getMasterInspectionIds(@inspection_csv_contents[8][1])).count.should be 3
  end

  #==============================================================================================================================

end

describe "A Schedule" do

  #Setup
  before(:all) do
    @schedule_csv_contents = FasterCSV.read("190TestNewSchedules.csv", :quote_char => '"', :col_sep =>',', :row_sep =>:auto)

  end

  #Check against product id for "scheduled" schedules, and against inspection ids for completed schedules. Incomplete schedules have nil inpsection_ids
  it "multiple, persists when created (Test #1)" do
  
    product_id = Product.find_by_serialnumber(@schedule_csv_contents[1][1]).id
      
    (inspection_schedule_set = InspectionSchedule.all(:include => :product, :conditions=> {:product_id => product_id})).count.should be 2

  end

  it "on an attached subcomponent exists when created (Test #2)" do

    product_id = Product.find_by_serialnumber(@schedule_csv_contents[2][1]).id

    (inspection_schedule_set = InspectionSchedule.all(:include => :product, :conditions=> {:product_id => product_id})).empty?.should be false
    
  end

  #NOTE: Completed schedules aren't updated on the web. 
  it "persists when completed"
   
end
