package is.citizen.citizenapi.resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Objects;

import is.citizen.citizenapi.enums.CountryCode;
import is.citizen.citizenapi.enums.PhoneType;

public class Phone implements Serializable {

    private static final long serialVersionUID = -4069931751998981052L;

    @JsonView({CitizenView.User.Login.class})
    private String id;

    @JsonView({CitizenView.User.Login.class})
    private String personId;

    @JsonView({CitizenView.User.Login.class})
    private CountryCode countryCode;

    @JsonView({CitizenView.User.Login.class})
    private String phoneNumber;

    @JsonView({CitizenView.User.Login.class})
    private String smsConfirmCode;

    @JsonView({CitizenView.User.Login.class})
    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime smsConfirmTime;

    @JsonView({CitizenView.User.Login.class})
    private boolean smsConfirmed = false;

    @JsonView({CitizenView.User.Login.class})
    private PhoneType phoneType;

    public Phone() {} //blank constructor for MapStruct

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getPersonId() { return personId; }

    public void setPersonId(String personId) { this.personId = personId; }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }


    public DateTime getSmsConfirmTime() { return smsConfirmTime; }

    public void setSmsConfirmTime(DateTime smsConfirmTime) { this.smsConfirmTime = smsConfirmTime; }

    public boolean getSmsConfirmed() { return smsConfirmed; }

    public void setSmsConfirmed(boolean smsConfirmed) { this.smsConfirmed = smsConfirmed; }

    public String getSmsConfirmCode() {
    return smsConfirmCode;
    }

    public void setSmsConfirmCode(String smsConfirmCode) { this.smsConfirmCode = smsConfirmCode; }

    public void formatPhoneNumber() {
        if (countryCode != null && phoneNumber != null) {
            phoneNumber = "+" + this.countryCode.getDialingCode() + phoneNumber;
        }
    }


    @Override
    public int hashCode() {
        return Objects.hash(countryCode, phoneNumber, phoneType, smsConfirmCode, smsConfirmTime, smsConfirmed, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Phone other = (Phone) obj;
        return Objects.equals(this.countryCode, other.countryCode) &&
               Objects.equals(this.phoneNumber, other.phoneNumber) &&
               Objects.equals(this.id, other.id) &&
               Objects.equals(this.phoneType, other.phoneType) && Objects.equals(this.smsConfirmCode, other.smsConfirmCode) &&
               Objects.equals(this.smsConfirmTime, other.smsConfirmTime) &&
               Objects.equals(this.smsConfirmed, other.smsConfirmed);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
