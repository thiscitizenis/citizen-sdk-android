package is.citizen.citizenapi.resource;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Objects;

import is.citizen.citizenapi.enums.AddressType;
import is.citizen.citizenapi.enums.CountryName;

public class Address implements Serializable {

    private static final long serialVersionUID = -5630453515676082265L;

    @JsonView({CitizenView.User.Login.class})
    private String id;

    @JsonView({CitizenView.User.Login.class})
    private String personId;

    @JsonView({CitizenView.User.Login.class})
    private String addressLine1;

    @JsonView({CitizenView.User.Login.class})
    private String addressLine2;

    @JsonView({CitizenView.User.Login.class})
    private String addressLine3;

    @JsonView({CitizenView.User.Login.class})
    private String city;

    @JsonView({CitizenView.User.Login.class})
    private String state;

    @JsonView({CitizenView.User.Login.class})
    private CountryName countryName;

    @JsonView({CitizenView.User.Login.class})
    private AddressType addressType;

    @JsonView({CitizenView.User.Login.class})
    private String postCode;

    @JsonView({CitizenView.User.Login.class})
    private DateTime validTo;

    @JsonView({CitizenView.User.Login.class})
    private DateTime validFrom;

    @JsonView({CitizenView.User.Login.class})
    private String confirmCode;

    @JsonView({CitizenView.User.Login.class})
    private boolean confirmedByCode;

    @JsonView({CitizenView.User.Login.class})
    private String addressLatitude;

    @JsonView({CitizenView.User.Login.class})
    private String addressLongitude;

    @JsonView({CitizenView.User.Login.class})
    private boolean confirmedByLocation;

    @JsonView({CitizenView.User.Login.class})
    private String confirmedLatitude;

    @JsonView({CitizenView.User.Login.class})
    private String confirmedLongitude;

    public Address() {} //blank constructor for MapStruct

    public String getId() {
    return id;
    }

    public void setId(String id) {
    this.id = id;
}

    public String getPersonId() { return personId; }

    public void setPersonId(String personId) { this.personId = personId; }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
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

    public CountryName getCountryName() {
        return countryName;
    }

    public void setCountryName(CountryName countryName) {
        this.countryName = countryName;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public DateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(DateTime validTo) {
        this.validTo = validTo;
    }

    public DateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(DateTime validFrom) {
        this.validFrom = validFrom;
    }

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public boolean isConfirmedByCode() {
        return confirmedByCode;
    }

    public void setConfirmedByCode(boolean confirmedByCode) {
        this.confirmedByCode = confirmedByCode;
    }

    public String getAddressLatitude() {
        return addressLatitude;
    }

    public void setAddressLatitude(String addressLatitude) {
        this.addressLatitude = addressLatitude;
    }

    public String getAddressLongitude() {
        return addressLongitude;
    }

    public void setAddressLongitude(String addressLongitude) {
        this.addressLongitude = addressLongitude;
    }

    public boolean isConfirmedByLocation() {
        return confirmedByLocation;
    }

    public void setConfirmedByLocation(boolean confirmedByLocation) {
        this.confirmedByLocation = confirmedByLocation;
    }

    public String getConfirmedLatitude() {
        return confirmedLatitude;
    }

    public void setConfirmedLatitude(String confirmedLatitude) {
        this.confirmedLatitude = confirmedLatitude;
    }

    public String getConfirmedLongitude() {
        return confirmedLongitude;
    }

    public void setConfirmedLongitude(String confirmedLongitude) {
        this.confirmedLongitude = confirmedLongitude;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects
            .hash(addressLine1, addressLine2, addressLine3, city, state, countryName, addressType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        final Address other = (Address) obj;
        return Objects.equals(this.addressLine1, other.addressLine1) &&
               Objects.equals(this.addressLine2, other.addressLine2) &&
               Objects.equals(this.addressLine3, other.addressLine3) &&
               Objects.equals(this.city, other.city) &&
               Objects.equals(this.state, other.state) &&
               Objects.equals(this.countryName, other.countryName) &&
               Objects.equals(this.addressType, other.addressType);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
