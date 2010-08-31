package com.n4systems.importing;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.exceptions.InfoFieldNotFoundException;
import com.n4systems.exceptions.InfoOptionNotFoundException;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.ProductType;
import com.n4systems.model.user.User;
import com.n4systems.util.FuzzyResolver;

public class AutoAttributeParser {
	private static final int HEADER_LINE = 0;
	
	/** Raw auto-attribute data */
	private List<String[]> attributeData = new ArrayList<String[]>();
	/** ProductType for these attributes, set once in the constructor */
	private ProductType productType;
	/** A User to set as the modifiedBy user on the generated objects */
	private User modifiedBy;
	/** 
	 * List of {@link AAColumn}s appearing in the same order as in <code>attributeData<code>.  
	 * Set by {@link #getCriteria()} and used in {@link #getDefinitions()} 
	 */
	private List<AAColumn> columns = new ArrayList<AAColumn>();
//	/** The {@link AutoAttributeCriteria} parsed from */
//	private AutoAttributeCriteria criteria;
	
	/**
	 * Intermediate enum used to discriminate between input and output fields.
	 * Also holds the header prefix for the column.
	 */
	private enum ColumnType {
		INPUT("I:"), OUTPUT("O:");
		
		String prefix;
		ColumnType(String prefix) {
			this.prefix = prefix;
		}
	};
	
	/**
	 * Intermediate data structure used to hold an InfoFieldBean and it's ColumnType
	 */
	private class AAColumn {
		public InfoFieldBean field;
		public ColumnType type;
		AAColumn(InfoFieldBean field, ColumnType type) {
			this.field = field;
			this.type = type;
		}
	}
	
	/**
	 * Constructs the AutoAttributeParser, setting the raw Auto-Attribute data and the ProductType<br />
	 * The first (index 0) <code>attributeData</code> <u>MUST</u> be a header line listing the Input/Output InfoFieldBean names. (eg I:Bleh, O:Asdf)<br />
	 * All elements after the first represent a static or dynamic info field.
	 * @see #AutoAttributeParser(List, ProductType, User)
	 * @param attributeData		Raw auto-atribute data
	 * @param productType		ProductType for this AutoAttributeCriteria
	 */
	public AutoAttributeParser(List<String[]> attributeData, ProductType productType) {
		this(attributeData, productType, null);
	}
	
	/**
	 * Constructs the AutoAttributeParser, setting the raw Auto-Attribute data and the ProductType<br />
	 * The first (index 0) <code>attributeData</code> <u>MUST</u> be a header line listing the Input/Output InfoFieldBean names. (eg I:Bleh, O:Asdf)<br />
	 * All elements after the first represent a static or dynamic info field.
	 * @param attributeData		Raw auto-atribute data
	 * @param productType		ProductType for this AutoAttributeCriteria
	 * @param modifiedBy		A user to set as the modifiedBy User on the AutoAttributeCriteria and AutoAttributeDefinitions
	 */
	public AutoAttributeParser(List<String[]> attributeData, ProductType productType, User modifiedBy) {
		this.attributeData = attributeData;
		this.productType = productType;
		this.modifiedBy = modifiedBy;
	}
	
	/**
	 * @return The raw Auto-Attribute data set in {@link #AutoAttributeParser(List, ProductType)}
	 */
	public List<String[]> getAttributeData() {
		return attributeData;
	}
	
	/**
	 * @see ProductType
	 * @return The ProductType set by {@link #AutoAttributeParser(List, ProductType)}
	 */
	public ProductType getProductType() {
		return productType;
	}
	
	/**
	 * Parses the first line of <code>attributeData</code> and constructs an AutoAttributeCriteria object.<br/>
	 * This <b><u>MUST</u></b> be called prior to calling {@link #getDefinitions()}.
	 * @see #getDefinitions()
	 * @see #parseHeaderColumn(String, AutoAttributeCriteria)
	 * @return 					the parsed AutoAttributeCriteria
	 * @throws ParseException	If the header data is not properly formatted.
	 */
	public AutoAttributeCriteria getCriteria() throws ParseException, InfoFieldNotFoundException {
		String[] headers = attributeData.get(HEADER_LINE);
		
		// construct the criteria and set defaults
		AutoAttributeCriteria criteria = new AutoAttributeCriteria();
		criteria.setTenant(productType.getTenant());
		criteria.setProductType(productType);
		criteria.setModifiedBy(modifiedBy);
		criteria.setInputs(new ArrayList<InfoFieldBean>());
		criteria.setOutputs(new ArrayList<InfoFieldBean>());
		
		// add the column data (input/output fields)
		for(String header: headers) {
			parseHeaderColumn(header, criteria);
		}
		
		return criteria;
	}
	
	/**
	 * Parses a String header column and adds it to the proper {@link AutoAttributeCritiera} list (ie input/output).  Header lines <u>MUST</u> begin with <code>INPUT_PREFIX</code> or <code>OUTPUT_PREFIX</code>
	 * and <u>MUST</u> resolve the name of an {@link InfoFieldBean} on <code>productType</code>.  All matches are case and whitespace insensitive.
	 * Also adds the field to the <code>columns</code> list for resolution in {@link #getDefinitions(AutoAttributeCriteria)}.
	 * @see #getCriteria()
	 * @param header						A single String header.
	 * @param criteria						An AutoAttributeCriteria
	 * @throws ParseException				If the header data is not properly formatted.
	 * @throws InfoFieldNotFoundException	If the header does not resolve to an <code>infoField</code> on the <code>productType</code>
	 */
	private void parseHeaderColumn(String header, AutoAttributeCriteria criteria) throws ParseException, InfoFieldNotFoundException {
		String upperHeader = header.trim().toUpperCase();
		
		// figure out what prefix we're working with
		String fieldName;
		ColumnType type;
		if(upperHeader.startsWith(ColumnType.INPUT.prefix)) {
			// take the part after the prefix and trim to remove any leading whitespace (eg "I: fieldname")
			fieldName = upperHeader.substring(ColumnType.INPUT.prefix.length()).trim();
			type = ColumnType.INPUT;
		} else if(upperHeader.startsWith(ColumnType.OUTPUT.prefix)) {
			// take the part after the prefix and trim to remove any leading whitespace (eg "O: fieldname")
			fieldName = upperHeader.substring(ColumnType.OUTPUT.prefix.length()).trim();
			type = ColumnType.OUTPUT;
		} else {
			// header is invlaid
			throw new ParseException("Invalid header syntax [" + header + "].  Headers must start with either '" + ColumnType.INPUT.prefix + "' or '" + ColumnType.OUTPUT.prefix + "'", 0);
		}
		
		// now we need to try an find an InfoField for this header
		InfoFieldBean infoField = null;
		try {
			// try and resolve using simple fuzzy rules
			infoField = FuzzyResolver.resolve(fieldName, productType.getAvailableInfoFields(), "name");
			
			if(infoField == null) {
				throw new InfoFieldNotFoundException(fieldName, productType);
			}
		} catch (InfoFieldNotFoundException i) {
			throw i;
		} catch (Exception e) {
			throw new InfoFieldNotFoundException(fieldName, productType, e);
		}
		
		// now we need to add it to either the input or output list
		switch(type) {
			case INPUT:
				// before we do this, we need to check that the input field is the proper type.
				// only fields having static options can be input fields (ie select box, combo box)
				if(!infoField.hasStaticInfoOption()) {
					throw new InfoFieldNotFoundException("Invalid input field [" + infoField.getName() + "]. Only InfoFields with static InfoOptions can be used as AutoAttribute input fields.");
				}
				
				criteria.getInputs().add(infoField);
				break;
			case OUTPUT:
				// anything can be an output field
				criteria.getOutputs().add(infoField);
				break;
		}
		
		// finally we will add this field and type to our columns list
		columns.add(new AAColumn(infoField, type));
	}
	
	/**
	 * Parses the <code>attributeData</code> and returns a list of {@link AutoAttributeDefinition AutoAttributeDefinitions}<br />
	 * {@link #getCriteria()} <b><u>MUST</u></b> be called before calling this method.
	 * @see #getCriteria()
	 * @param criteria			The AutoAttributeCriteria to associate with these AutoAttributeDefinitions
	 * @return					A list of AutoAttributeDefinitions
	 * @throws ParseException
	 */
	public List<AutoAttributeDefinition> getDefinitions(AutoAttributeCriteria criteria) throws ParseException, InfoOptionNotFoundException {
		List<AutoAttributeDefinition> attribs = new ArrayList<AutoAttributeDefinition>();
		
		AutoAttributeDefinition attrib;
		// parse each data line (after the header line)
		for(int i = HEADER_LINE + 1; i < attributeData.size(); i++) {
			// initialize a new Definition and set the defaults
			attrib = new AutoAttributeDefinition();
			attrib.setTenant(productType.getTenant());
			attrib.setModifiedBy(modifiedBy);
			attrib.setCriteria(criteria);
			attrib.setInputs(new ArrayList<InfoOptionBean>());
			attrib.setOutputs(new ArrayList<InfoOptionBean>());

			// setup the input/output fields
			parseDataRow(attributeData.get(i), attrib);
			
			// append to our master list
			attribs.add(attrib);
		}
		
		return attribs;
	}
	
	/**
	 * Parses a single row of raw input and adds the resolved infooptions to the attribs input and output lists
	 * @param row		a row of raw auto-attribute data
	 * @param attrib	an AutoAttributeDefinition to set the InfoOptions on
	 * @throws InfoOptionNotFoundException	If an InfoOption failed resolution
	 */
	private void parseDataRow(String[] row, AutoAttributeDefinition attrib) throws InfoOptionNotFoundException {
		AAColumn columnDef;
		// parse out the info option of each column
		for(int i = 0; i < row.length; i++) {
			columnDef = columns.get(i);
			switch(columnDef.type) {
				case INPUT:
					attrib.getInputs().add(resolveInputInfoOption(row[i], columnDef.field));
					break;
				case OUTPUT:
					attrib.getOutputs().add(resolveOutputInfoOption(row[i], columnDef.field));
					break;
			}
		}
	}
	
	/**
	 * Resolves the static InfoOptionBean for an Input field.
	 * @param name		Name of the InfoOption
	 * @param field		The InfoField to search for InfoOptions on.
	 * @return			The resolved InfoOption
	 * @throws InfoOptionNotFoundException	If the InfoOption could not be resolved by name
	 */
	private InfoOptionBean resolveInputInfoOption(String name, InfoFieldBean field) throws InfoOptionNotFoundException {
		// Input info options MUST resolve to a static field.
		InfoOptionBean option = resolveInfoOption(name, field);
		
		if(option == null) {
			throw new InfoOptionNotFoundException(name, field);
		}
		
		return option;
	}
	
	/**
	 * Resolves or Creates an InfoOptionBean for an Output field.  If the <code>field</code> does not have static options (textfields), a new dynamic option is created.
	 * If the <code>field</code> has dynamic options (select/combo boxes) a static option is resolved from the <code>field</code>.
	 * If a static option could not be found and the <code>field</code> accepts dynamic options (comboboxes), a new dynamic option is created, otherwise (selectboxes)
	 * an InfoOptionNotFoundException is thrown.
	 * @param name		Name of the InfoOption
	 * @param field		The InfoField to search for InfoOptions on
	 * @return			A resolved static or created dynamic InfoOption
	 * @throws InfoOptionNotFoundException	If resolution of a static option failed and the field does not accept dynamic options
	 */
	private InfoOptionBean resolveOutputInfoOption(String name, InfoFieldBean field) throws InfoOptionNotFoundException {
		InfoOptionBean option;
		
		if(field.hasStaticInfoOption()) {
			// The option has static options, we will try and resolve a static one first.
			option = resolveInfoOption(name, field);
			
			if(option == null) {
				// if we didn't find one, we may have a few options
				if(field.acceptsDyanmicInfoOption()) {
					// if the field also accepts dynamic options, then we can create a new one now
					option = field.createDynamicInfoOption(name);
				} else {
					// we couldn't find a static and it doesn't accept dynamic options ... we're screwed .. 
					throw new InfoOptionNotFoundException(name, field);
				}
			}
		} else {
			// this field does not have static info options, we must create a new dynamic info option
			option = field.createDynamicInfoOption(name);
		}
		
		return option;
	}
	
	/**
	 * Resolves a static info option against an info field.
	 * @param name		Static info option name
	 * @param field		InfoFieldBean to resolve from 
	 * @return			A resolved InfoOptionBean or null if no option could be found.
	 * @throws InfoOptionNotFoundException	If something happened during name resolution
	 */
	private InfoOptionBean resolveInfoOption(String name, InfoFieldBean field) throws InfoOptionNotFoundException {
		InfoOptionBean option = null;
		try {
			// try and resolve using simple fuzzy rules
			option = FuzzyResolver.resolve(name, field.getUnfilteredInfoOptions(), "name");
		} catch (Exception e) {
			throw new InfoOptionNotFoundException(name, field, e);
		}
		
		return option;
	}
}
