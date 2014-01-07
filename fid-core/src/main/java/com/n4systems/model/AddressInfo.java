package com.n4systems.model;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.n4systems.model.parents.AbstractEntity;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "addressinfo")
public class AddressInfo extends AbstractEntity {

	private String streetAddress = "";
	private String city = "";
	private String state = "";
	private String country = "";
	private String zip = "";
	private String phone1 = "";
	private String phone2 = "";
	private String fax1 = "";
    private GpsLocation gpsLocation = new GpsLocation();

    private @Transient String formattedAddress;

    public AddressInfo() { }

    public AddressInfo(AddressInfo addressInfo) {
		streetAddress = addressInfo.streetAddress;
		city = addressInfo.city;
		state = addressInfo.state;
		country = addressInfo.country;
		zip = addressInfo.zip;
		phone1 = addressInfo.phone1;
		phone2 = addressInfo.phone2;
		fax1 = addressInfo.fax1;
        formattedAddress = addressInfo.formattedAddress;
        gpsLocation = addressInfo.gpsLocation==null ? new GpsLocation() : addressInfo.gpsLocation;
	}

	public String getStreetAddress() {
		return streetAddress;
	}
	
	public void setStreetAddress(String address) {
		this.streetAddress = (address != null) ? address : "";
		this.streetAddress = streetAddress.trim();
	}
	
	public void setStreetAddress(String addressLine1, String addressLine2) {
		setStreetAddress(((addressLine1 != null) ? addressLine1 : "") + " " + ((addressLine2 != null) ? addressLine2 : ""));
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getZip() {
		return zip;
	}
	
	public void setZip(String postalCode) {
		this.zip = postalCode;
	}
	
	public String getPhone1() {
		return phone1;
	}
	
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}
	
	public String getPhone2() {
		return phone2;
	}
	
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	
	public String getFax1() {
		return fax1;
	}
	
	public void setFax1(String faxNumber) {
		this.fax1 = faxNumber;
	}

    public String getDisplay() {
        return getDisplay(true);
    }

	public String getDisplay(boolean includePhone) {
		String display = "";

        if(streetAddress != null && !streetAddress.isEmpty())
            display += streetAddress + "\n";

        if (city != null && !city.isEmpty())
			display +=city;

        if(state != null && !state.isEmpty())
            display+= ", " + state + "\n";

        if(country != null && !country.isEmpty())
			display+= country + "\n";

        if(zip != null && !zip.isEmpty())
			display += zip;

        if(includePhone) {
            if(phone1 != null && !phone1.isEmpty()) {
                display += "\nPhone 1: " + phone1;
            }

            if(phone2 != null && !phone2.isEmpty()) {
                display += "\nPhone 2: " + phone2;
            }

            if(fax1 != null && !fax1.isEmpty()) {
                display += "\nFax: " + fax1;
            }
        }
		return display;
	}
	
	public void copyFieldsTo(AddressInfo other) {
		other.streetAddress = streetAddress;
		other.city = city;
		other.state = state;
		other.country = country;
		other.zip = zip;
		other.phone1 = phone1;
		other.phone2 = phone2;
		other.fax1 = fax1;
	}

    public GpsLocation getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(GpsLocation gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public String getFormattedAddress() {
        if (formattedAddress==null) {
            Predicate<String> skipBlank = new Predicate<String>() {
                @Override public boolean apply(String input) {
                    return StringUtils.isNotBlank(input);
                }
            };
            Iterable<String> nonBlankFields = Iterables.filter(Lists.newArrayList(streetAddress, city, state, country, zip), skipBlank);
            return Joiner.on(',').join(nonBlankFields);
        }
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    @Override
    public String toString() {
        return "AddressInfo{" +
                "city='" + city + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", zip='" + zip + '\'' +
                ", phone1='" + phone1 + '\'' +
                ", phone2='" + phone2 + '\'' +
                ", fax1='" + fax1 + '\'' +
                ", gpsLocation=" + gpsLocation +
                '}';
    }

}
