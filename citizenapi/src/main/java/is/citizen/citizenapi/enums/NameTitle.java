package is.citizen.citizenapi.enums;


public enum NameTitle
{
    MR("Mr"),
    MRS("Mrs"),
    MS("MS"),
    DR("DR"),
    SIR("SIR");

    private String title;

    private NameTitle(final String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
