package is.citizen.citizenapi.resource;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import is.citizen.citizenapi.enums.CountryName;

public class Person implements Serializable {

    private static final long serialVersionUID = 8701594527413117519L;

    @JsonView({CitizenView.User.Register.class, CitizenView.User.Login.class})
    private String id;

    @JsonView({CitizenView.User.Login.class})
    private String title;

    @JsonView({CitizenView.User.Login.class})
    private String firstName;

    @JsonView({CitizenView.User.Login.class})
    private String middleName;

    @JsonView({CitizenView.User.Login.class})
    private String lastName;

    @JsonView({CitizenView.User.Login.class})
    private String gender;

    @JsonView({CitizenView.User.Login.class})
    @JsonSerialize(using = DateTimeSerializer.class)
    private DateTime dateOfBirth;

    @JsonView({CitizenView.User.Login.class})
    private String placeOfBirth;

    @JsonView({CitizenView.User.Login.class})
    private CountryName countryNationality;

    @JsonView({CitizenView.User.Login.class})
    private String profilePicId;

    @JsonView({CitizenView.User.Login.class})
    private Phone phone;

    @JsonView({CitizenView.User.Login.class})
    private Address address;

    @JsonView({CitizenView.User.Login.class})
    private List<Address> addressHistory = new ArrayList<>();

    @JsonView({CitizenView.User.Login.class})
    private String entityId;

    public List<Address> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<Address> addressHistory) {
        this.addressHistory = addressHistory;
    }

    public String getProfilePicId() { return profilePicId; }

    public void setProfilePicId(String profilePicId) { this.profilePicId = profilePicId; }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public DateTime getDateOfBirth() { return dateOfBirth; }

    public void setDateOfBirth(DateTime dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPlaceOfBirth() { return placeOfBirth; }

    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }

    public CountryName getCountryNationality() { return countryNationality; }

    public void setCountryNationality(CountryName countryNationality) { this.countryNationality = countryNationality; }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public boolean equals(Object o)
        {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Person person = (Person) o;

        if (id != null ? !id.equals(person.id) : person.id != null)
            return false;
        if (title != null ? !title.equals(person.title) : person.title != null)
            return false;
        if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null)
            return false;
        if (middleName != null ? !middleName.equals(person.middleName) : person.middleName != null)
            return false;
        if (lastName != null ? !lastName.equals(person.lastName) : person.lastName != null)
            return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(person.dateOfBirth) : person.dateOfBirth != null)
            return false;
        if (placeOfBirth != null ? !placeOfBirth.equals(person.placeOfBirth) : person.placeOfBirth != null)
            return false;
        if (countryNationality != null ? !countryNationality.equals(person.countryNationality) : person.countryNationality != null)
            return false;
        if (profilePicId != null ? !profilePicId.equals(person.profilePicId) : person.profilePicId != null)
            return false;
        if (phone != null ? !phone.equals(person.phone) : person.phone != null)
            return false;
        return address != null ? address.equals(person.address) : person.address == null;

        }

    @Override
    public int hashCode()
        {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (placeOfBirth != null ? placeOfBirth.hashCode() : 0);
        result = 31 * result + (countryNationality != null ? countryNationality.hashCode() : 0);
        result = 31 * result + (profilePicId != null ? profilePicId.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
        }

    @Override
    public String toString()
        {
        return "Person{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", placeOfBirth='" + placeOfBirth + '\'' +
                ", countryNationality='" + countryNationality + '\'' +
                ", profilePicId='" + profilePicId + '\'' +
                ", phone=" + phone +
                ", address=" + address +
                '}';
        }
}
