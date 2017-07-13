package is.citizen.citizenapi.enums;

public enum GenderType {
    MALE("M"),
    FEMALE("F");

    private String gender;

    private GenderType(final String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return gender;
    }
}
