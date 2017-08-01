package is.citizen.citizenapi.resource;

import java.io.Serializable;

public class Name implements Serializable {

    private static final long serialVersionUID = -5044195424439519082L;

    private String title;

    private String firstName;

    private String lastName;

    private String middleName;

    private String gender;

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
