package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.web.helper.SessionUser;

import com.n4systems.util.DateHelper;
import com.n4systems.util.StringListingPair;

public class InfoOptionInput {
	public static String COMBOBOX_DYNAMIC_VALUE_MARKER = "!";
		
	private Long uniqueID;
	private String name;
	
	private Long weight;
	
	private Long infoFieldIndex;

	private boolean deleted;
	
	private Long infoFieldId;
	
	private String uniqueIDString;
		
	public InfoOptionInput() {
		this( null, null );
	}
	
	public InfoOptionInput( InfoOptionBean infoOption ) {
		this( infoOption, null );
		
	}
	
	public InfoOptionInput( InfoOptionBean infoOption, InfoFieldBean infoField ) {
		super();
		deleted = false;
		
		if( infoOption != null ) {
			if( infoOption.isStaticData() ) {
				setUniqueID(infoOption.getUniqueID());
			} else {
				if( infoField.getFieldType().equals( InfoFieldBean.COMBOBOX_FIELD_TYPE ) 
						|| infoField.getFieldType().equals( InfoFieldBean.SELECTBOX_FIELD_TYPE ) ) {
					setUniqueIDString( COMBOBOX_DYNAMIC_VALUE_MARKER + infoOption.getName() );
				}
			}
			
			name = infoOption.getName();
			weight = infoOption.getWeight();
		}
				
		if( infoField != null ) {
			infoFieldId = infoField.getUniqueID();
		}
	}
	
	public boolean isBlank() {
		return (  ( name == null || name.equals("") ) && ( uniqueIDString == null || uniqueIDString.equals("0") || uniqueIDString.equals("!") ) ) ;
	}
	
	public InfoOptionBean convertToInfoOptionBean( InfoFieldBean field, SessionUser user ) {
		// blank options arn't saved.
		if( isBlank() ) {
			return null;
		}
		
		if( field.hasStaticInfoOption() && !uniqueIDString.startsWith( COMBOBOX_DYNAMIC_VALUE_MARKER ) ) {
			for (InfoOptionBean infoOptionBean : field.getUnfilteredInfoOptions() ) {
				Long infoOptionId = Long.parseLong( uniqueIDString );
				
				if( infoOptionBean.getUniqueID().equals( infoOptionId ) ) {
					return infoOptionBean;
				}
			}
		}
				
		InfoOptionBean infoOption = new InfoOptionBean();
				
		if( field.getFieldType().equals( InfoFieldBean.COMBOBOX_FIELD_TYPE )  || 
				field.getFieldType().equals( InfoFieldBean.SELECTBOX_FIELD_TYPE ) ) {
			infoOption.setName( uniqueIDString.substring( 1 ) );
		}else if (field.getFieldType().equals( InfoFieldBean.DATEFIELD_FIELD_TYPE) ){ 
				String dateFormatStr = field.isIncludeTime() ? user.getDateTimeFormat() : user.getDateFormat();
				Date date = DateHelper.string2DateTime(dateFormatStr, name, user.getTimeZone());
				infoOption.setName( Long.toString(date.getTime()) );
		} else {
			infoOption.setName( name );
		}
		infoOption.setInfoField( field );
		
		
		return infoOption;
	}
	
	public Long getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(Long uniqueID) {
		this.uniqueID = uniqueID;
		if( uniqueID == null ) {
			uniqueIDString = null;
		} else {
			uniqueIDString = uniqueID.toString();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getWeight() {
		if( weight == null ) {
			weight = 0L;
		}
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public Long getInfoFieldIndex() {
		return infoFieldIndex;
	}

	public void setInfoFieldIndex(Integer infoFieldIndex) {
		this.infoFieldIndex = infoFieldIndex.longValue();
	}
	
	public void setInfoFieldIndex(Long infoFieldIndex) {
		this.infoFieldIndex = infoFieldIndex;
	}

	public Long getInfoFieldId() {
		return infoFieldId;
	}

	public void setInfoFieldId(Long infoFieldId) {
		this.infoFieldId = infoFieldId;
	}
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getUniqueIDString() {
		return uniqueIDString;
	}

	public void setUniqueIDString(String uniqueIDString) {
		this.uniqueIDString = uniqueIDString;
	}


	public static List<InfoOptionBean> convertInputInfoOptionsToInfoOptions( List<InfoOptionInput> inputs, Collection<InfoFieldBean> fieldsToLookFor, SessionUser user ) {
		List<InfoOptionBean> newInfoOptions = new ArrayList<InfoOptionBean>();
		if( fieldsToLookFor == null || inputs == null ){ return newInfoOptions; }
		for( InfoOptionInput input : inputs ) {
			if( input != null ) {  // some of the inputs can be null due to the retired info fields.

				// TODO DD WEB-2157 : this method is very slow.  turn the largest collection into a set/map. 
				//  and use contains(?)
				
				for( InfoFieldBean field : fieldsToLookFor ) {
					if( field.getUniqueID().equals( input.getInfoFieldId() ) ) {
						
						InfoOptionBean option = input.convertToInfoOptionBean( field, user );
						if( option != null ) {
							newInfoOptions.add( option );
						}
					}
				}
			}
		}
		return newInfoOptions;
	}
	
	public static List<InfoOptionInput> convertInfoOptionsToInputInfoOptions( List<InfoOptionBean> options, Collection<InfoFieldBean> fieldsToLookFor, SessionUser user ) {
		List<InfoOptionInput> inputs = new ArrayList<InfoOptionInput>();
		if( fieldsToLookFor == null ){ return inputs; }
		for( InfoFieldBean field : fieldsToLookFor ) {
			if(!field.isRetired()) {
				InfoOptionInput input = null;
				if( options != null ) {	
					for( InfoOptionBean option : options ) {
						if( option.getInfoField().getUniqueID().equals( field.getUniqueID() ) ) {
							input = new InfoOptionInput( option, field );
							
							if(option.getInfoField().getFieldType().equals(InfoFieldBean.DATEFIELD_FIELD_TYPE)) {
								Date utcDate = new Date(Long.parseLong(input.getName()));
								SessionUserDateConverter dateConverter = user.createUserDateConverter();
								
								if (option.getInfoField().isIncludeTime()) {
									input.setName( dateConverter.convertDateTime(utcDate) );
								} else {
									input.setName( dateConverter.convertDate(utcDate) );
								}
							}
							break;
						}
					}
				}
				if( input == null ) {
					input = new InfoOptionInput( null, field );
				}
				inputs.add( input );
			}
		}
		return inputs;
	}
	
	public static StringListingPair convertInfoOptionToLP( InfoOptionBean option ) {
		if( option.isStaticData() ) {
			return new StringListingPair( option.getUniqueID().toString(), option.getName() );
		} else {
			return new StringListingPair( InfoOptionInput.COMBOBOX_DYNAMIC_VALUE_MARKER + option.getName(), option.getName() );
		}
	}
}
